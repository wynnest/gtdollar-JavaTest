package com.gtdollar.wynn.dal;

/*
 * @author  Wynn Teo
 * @version 1.0
 * @since   2018-08-05 
 */
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gtdollar.wynn.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, Long> {

	User findUserByEmail(String email);
}
