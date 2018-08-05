package com.gtdollar.wynn.dal;

/*
 * @author  Wynn Teo
 * @version 1.0
 * @since   2018-08-05 
 */
import java.util.List;

import com.gtdollar.wynn.model.Transaction;

public interface CommonDAL {

	public long getNextUserIdSequence(String sequenceId);

	public List<Transaction> findAllTransactionByEmail(String email);
}
