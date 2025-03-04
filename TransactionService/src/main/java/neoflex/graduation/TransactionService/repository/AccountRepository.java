package neoflex.graduation.TransactionService.repository;

import neoflex.graduation.TransactionService.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
