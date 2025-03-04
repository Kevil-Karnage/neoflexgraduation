package neoflex.graduation.AccountService.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neoflex.graduation.AccountService.dto.request.AccountAddBalanceRq;
import neoflex.graduation.AccountService.dto.response.AccountAddBalanceRs;
import neoflex.graduation.AccountService.dto.response.AccountGetBalanceRs;
import neoflex.graduation.AccountService.exception.NotFoundAccountException;
import neoflex.graduation.AccountService.exception.TransactionNotSavedException;
import neoflex.graduation.AccountService.kafka.KafkaProducer;
import neoflex.graduation.AccountService.model.Account;
import neoflex.graduation.AccountService.model.Transaction;
import neoflex.graduation.AccountService.repository.AccountRepository;
import neoflex.graduation.AccountService.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final KafkaProducer kafka;

    public AccountGetBalanceRs getAccountBalance(long id) {
        log.info("|---| Get account balance |---|");
        Account account = accountRepository.findById(id)
                .orElseThrow(NotFoundAccountException::new);

        log.info("|---| Get account balance: Success |---|");
        return new AccountGetBalanceRs(id, account.getBalance(), new Date());
    }

    public AccountAddBalanceRs addBalanceToAccount(long id, AccountAddBalanceRq rq) {
        log.info("|---| Add balance to account with id = {} |---|", id);

        // 1) получаем аккаунт
        Account account;
        try {
            // 1.1) пытаемся получить из бд
            account =  accountRepository.findById(id)
                    .orElseThrow(NotFoundAccountException::new);
            account.setBalance(account.getBalance().add(rq.getAmount()));
        } catch (NotFoundAccountException ex) {
            // 1.2) если аккаунта с таким id нет, то создаем его:
            account = new Account(id, rq.getAmount());
        }

        // 2) создаём транзакцию пополнения
        Date date = new Date();

        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                date,
                null,
                id,
                rq.getAmount()
        );

        // 3) сохраняем обновлённые данные и возвращаем пользователю
        transaction = saveTransaction(transaction, account);

        // 4 Отправляем в кафку
        kafka.sendMessage(transaction);

        log.info("|---| Add balance to account with id = {}: Success |---|", id);
        return new AccountAddBalanceRs(
                transaction.getId(),
                account.getBalance(),
                transaction.getTimestamp()
        );
    }


    @Transactional
    private Transaction saveTransaction(Transaction transaction, Account account) {
        try {
            transaction = transactionRepository.save(transaction);
        } catch (Exception ex) {
            throw new TransactionNotSavedException();
        }
        try {
            accountRepository.save(account);
        } catch (Exception ex) {
            // если не получилось обновить данные аккаунта, то и транзакцию удаляем
            transactionRepository.deleteById(transaction.getId());
            throw new TransactionNotSavedException();
        }

        return transaction;
    }
}
