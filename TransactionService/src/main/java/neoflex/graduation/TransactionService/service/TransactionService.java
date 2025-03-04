package neoflex.graduation.TransactionService.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neoflex.graduation.TransactionService.dto.request.TransactionGetRq;
import neoflex.graduation.TransactionService.dto.request.TransactionNewRq;
import neoflex.graduation.TransactionService.dto.response.TransactionHistoryRs;
import neoflex.graduation.TransactionService.dto.response.TransactionNewRs;
import neoflex.graduation.TransactionService.dto.response.TransactionRs;
import neoflex.graduation.TransactionService.exception.AccountNotEnoughBalanceException;
import neoflex.graduation.TransactionService.exception.AccountNotFoundException;
import neoflex.graduation.TransactionService.exception.TransactionNotFoundException;
import neoflex.graduation.TransactionService.exception.TransactionSaveException;
import neoflex.graduation.TransactionService.kafka.KafkaProducer;
import neoflex.graduation.TransactionService.model.Account;
import neoflex.graduation.TransactionService.model.Transaction;
import neoflex.graduation.TransactionService.repository.AccountRepository;
import neoflex.graduation.TransactionService.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final KafkaProducer kafkaProducer;

    public TransactionNewRs createNewTransaction(TransactionNewRq rq) throws AccountNotFoundException{

        log.info("---| Trying to create new transaction |---");
        // 1) проверяем аккаунты
        Account from = accountRepository.findById(rq.getFromAccount())
                .orElseThrow(AccountNotFoundException::new);
        Account to = accountRepository.findById(rq.getToAccount())
                .orElseThrow(AccountNotFoundException::new);

        if (from.getBalance().compareTo(rq.getAmount()) < 0) {
            throw new AccountNotEnoughBalanceException();
        }

        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                new Date(),
                rq.getFromAccount(),
                rq.getToAccount(),
                rq.getAmount()
        );

        // сохраняем результат

        from.setBalance(from.getBalance().add(rq.getAmount().multiply(new BigDecimal("-1"))));
        to.setBalance(to.getBalance().add(rq.getAmount()));

        transaction = saveTransaction(transaction, from, to);
        log.info("---| Transaction created: Success |---");

        kafkaProducer.sendMessage(transaction);

        // создаем ответ и возвращаем
        return new TransactionNewRs(transaction.getId(), transaction.getTimestamp());
    }

    public TransactionRs getTransactionById(TransactionGetRq rq) {
        log.info("---| Trying to get transaction with id = {} |---", rq.getTransactionId());

        Transaction transaction = transactionRepository.findById(rq.getTransactionId())
                .orElseThrow(TransactionNotFoundException::new);

        log.info("---| Transaction creating: Success |---");
        return new TransactionRs(
                transaction.getId(),
                transaction.getTimestamp(),
                transaction.getFromAccount(),
                transaction.getToAccount(),
                transaction.getAmount());
    }

    public TransactionHistoryRs getAccountTransactions(long accountId) {
        log.info("---| Trying to get transactions history for account with id = {} |---", accountId);

        // проверяем существование аккаунта
        accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);

        // ищем список всех транзакций и собираем response
        List<TransactionRs> rsList = transactionRepository.findAllByAccountId(accountId).stream()
                .map(tr -> new TransactionRs(
                        tr.getId(),
                        tr.getTimestamp(),
                        tr.getFromAccount(),
                        tr.getToAccount(),
                        tr.getAmount()
                ))
                .toList();

        log.info("---| Get transaction history: Success |---");
        return new TransactionHistoryRs(accountId, rsList);
    }

    @Transactional
    private Transaction saveTransaction(Transaction transaction, Account from, Account to) {
        try {
            transaction = transactionRepository.save(transaction);
        } catch (Exception ex) {
            throw new TransactionSaveException();
        }

        try {
            accountRepository.save(from);
        } catch (Exception ex) {
            transactionRepository.deleteById(transaction.getId());
            throw new TransactionSaveException();
        }

        try {
            accountRepository.save(to);
        } catch (Exception ex) {
            transactionRepository.deleteById(transaction.getId());
            accountRepository.deleteById(from.getId());
            throw new TransactionSaveException();
        }

        return transaction;
    }
}
