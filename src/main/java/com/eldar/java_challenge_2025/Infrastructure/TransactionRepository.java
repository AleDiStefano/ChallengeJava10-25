package com.eldar.java_challenge_2025.Infrastructure;

import com.eldar.java_challenge_2025.Domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query(value = """
    SELECT a.account_number AS accountNumber, SUM(x.cnt) AS total
    FROM (
      SELECT t.transaction_account_id_to AS acc_id, COUNT(*) AS cnt
      FROM transactions t
      GROUP BY t.transaction_account_id_to

      UNION ALL

      SELECT t.transaction_account_id_from AS acc_id, COUNT(*) AS cnt
      FROM transactions t
      GROUP BY t.transaction_account_id_from
    ) x
    JOIN accounts a ON a.account_id = x.acc_id
    GROUP BY a.account_number
    ORDER BY total DESC
    LIMIT 10
    """, nativeQuery = true)
    List<Object[]> top10AccountsCombinedById();
}
