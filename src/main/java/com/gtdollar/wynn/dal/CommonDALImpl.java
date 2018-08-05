package com.gtdollar.wynn.dal;

/*
 * @author  Wynn Teo
 * @version 1.0
 * @since   2018-08-05 
 */
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.gtdollar.wynn.model.Sequence;
import com.gtdollar.wynn.model.Transaction;

@Repository
public class CommonDALImpl implements CommonDAL {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public long getNextUserIdSequence(String sequenceId) {
		FindAndModifyOptions options = new FindAndModifyOptions();
		Sequence tranSeq = mongoTemplate.findAndModify(new Query(Criteria.where("_id").is(sequenceId)),
				new Update().inc("seq", 1), options.returnNew(true).upsert(true), Sequence.class);
		return tranSeq.getSeq();
	}

	@Override
	public List<Transaction> findAllTransactionByEmail(String email) {

		Criteria criteria = new Criteria();
		criteria.orOperator(Criteria.where("to").is(email), Criteria.where("from").is(email));
		Query query = new Query(criteria);
		List<Transaction> transList = mongoTemplate.find(query, Transaction.class);
		if (transList != null) {
			return transList;
		} else {
			return null;
		}
	}

}
