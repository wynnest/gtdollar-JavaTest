package com.gtdollar.wynn.dal;

/*
 * @author  Wynn Teo
 * @version 1.0
 * @since   2018-08-05 
 */
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gtdollar.wynn.model.Transaction;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, Long> {

}
