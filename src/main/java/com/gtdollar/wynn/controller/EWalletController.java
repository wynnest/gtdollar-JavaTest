package com.gtdollar.wynn.controller;

/*
 * @author  Wynn Teo
 * @version 1.0
 * @since   2018-08-05 
 */
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gtdollar.wynn.dal.AccountRepository;
import com.gtdollar.wynn.dal.CommonDAL;
import com.gtdollar.wynn.dal.TransactionRepository;
import com.gtdollar.wynn.dal.UserRepository;
import com.gtdollar.wynn.model.Account;
import com.gtdollar.wynn.model.Transaction;
import com.gtdollar.wynn.model.User;

@RestController
@RequestMapping(value = "/")
public class EWalletController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	private final UserRepository userRepository;
	private final CommonDAL commonDAL;
	private final TransactionRepository transRepo;
	private final AccountRepository accountRepository;

	public EWalletController(UserRepository userRepository, CommonDAL commonDAL, TransactionRepository transRepo,
			AccountRepository accountRepository) {
		this.userRepository = userRepository;
		this.commonDAL = commonDAL;
		this.transRepo = transRepo;
		this.accountRepository = accountRepository;
	}

	@RequestMapping(value = "/insertUser", method = RequestMethod.POST)
	public Map<String, Object> insertUser(@RequestBody User u) throws Exception {
		Map<String, Object> returnObject = new HashMap<String, Object>();
		LOG.info("Create new user.");
		try {
			User user = userRepository.findUserByEmail(u.getEmail());
			if (user != null) {
				returnObject.put("success", "false");
				returnObject.put("error", "User already exist!");
			} else {
				user = new User();
				user.setEmail(u.getEmail());
				user.setCreationDate(new Date());
				user.setId(commonDAL.getNextUserIdSequence("userId"));
				userRepository.save(user);
				returnObject.put("success", "true");
				returnObject.put("error", "User added!");
			}
		} catch (Exception e) {
			returnObject.put("success", "false");
			returnObject.put("error", "An error occured!");
		}

		return returnObject;
	}

	@RequestMapping(value = "/returnUser", method = RequestMethod.POST)
	public Map<String, Object> returnUser(@RequestBody User u) throws Exception {
		Map<String, Object> returnObject = new HashMap<String, Object>();
		LOG.info("Create new userId.");
		try {
			User user = userRepository.findUserByEmail(u.getEmail());
			if (user != null) {
				returnObject.put("success", "true");
				returnObject.put("user", user);
			} else {
				returnObject.put("success", "false");
				returnObject.put("error", "User doesn't exist!");
			}
		}catch (Exception e){
			returnObject.put("success", "false");
			returnObject.put("error", "An error occured!");
		}
		return returnObject;
	}

	@RequestMapping(value = "/createAccount", method = RequestMethod.POST)
	public Map<String, Object> createAccount(@RequestBody User u) throws Exception {
		Map<String, Object> returnObject = new HashMap<String, Object>();
		LOG.info("Create new account.");
		try{
			User user = userRepository.findUserByEmail(u.getEmail());
		
			if (user == null) {
				returnObject.put("success", "false");
				returnObject.put("error", "User doesn't exist!");
			} else {
				Account acc = accountRepository.findAccountByUserId(user.getId());
				if (acc == null) {
					acc = new Account();
					acc.setBalance(1000);
					acc.setUserId(user.getId());
					acc.setId(commonDAL.getNextUserIdSequence("accountId"));
					accountRepository.save(acc);
	
					returnObject.put("success", "true");
					returnObject.put("balance", acc.getBalance());
				} else {
					returnObject.put("success", "false");
					returnObject.put("error", "Account already exist!");
				}
			}
		}catch (Exception e){
			returnObject.put("success", "false");
			returnObject.put("error", "An error occured!");
		}
		return returnObject;
	}

	@RequestMapping(value = "/retrieveBalance", method = RequestMethod.POST)
	public Map<String, Object> retrieveBalance(@RequestBody User u) throws Exception {
		Map<String, Object> returnObject = new HashMap<String, Object>();
		LOG.info("Retrieve account balance.");
		try {
			User user = userRepository.findUserByEmail(u.getEmail());
			if (user != null) {
				Account acc = accountRepository.findAccountByUserId(user.getId());
				if (acc != null) {
					returnObject.put("success", "true");
					returnObject.put("balance", acc.getBalance());
				} else {
					returnObject.put("success", "false");
					returnObject.put("error", "Account doesn't exist!");
				}
			} else {
				returnObject.put("success", "false");
				returnObject.put("error", "User doesn't exist!");
			}
		}catch (Exception e){
			returnObject.put("success", "false");
			returnObject.put("error", "An error occured!");
		}
		return returnObject;
	}

	@RequestMapping(value = "/retrieveTransactionHistory", method = RequestMethod.POST)
	public Map<String, Object> retrieveTransactionHistory(@RequestBody User u) throws Exception {
		Map<String, Object> returnObject = new HashMap<String, Object>();
		LOG.info("Retrieve transaction history.");
		try {
			User user = userRepository.findUserByEmail(u.getEmail());
			if (user != null) {
				List<Transaction> tList = commonDAL.findAllTransactionByEmail(u.getEmail());
				if (tList != null) {
					returnObject.put("success", "true");
					returnObject.put("transactions", tList);
				} else {
					returnObject.put("success", "false");
					returnObject.put("error", "No transaction record found.");
				}
			} else {
				returnObject.put("success", "false");
				returnObject.put("error", "User doesn't exist!");
			}
		}catch (Exception e){
			returnObject.put("success", "false");
			returnObject.put("error", "An error occured!");
		}
		return returnObject;
	}

	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
	public Map<String, Object> transfer(@RequestBody Map<String, ?> input) throws Exception {
		Map<String, Object> returnObject = new HashMap<String, Object>();
		LOG.info("Transfer.");
		try {
			String email = input.get("email").toString();
			String transferee = input.get("transferee").toString();
			double amount = Double.parseDouble(input.get("amount").toString());
	
			User userFrom = userRepository.findUserByEmail(email);
			User userTo = userRepository.findUserByEmail(transferee);
			Account accFrom = accountRepository.findAccountByUserId(userFrom.getId());
			Account accTo = accountRepository.findAccountByUserId(userTo.getId());
	
			if (userFrom == null || userTo == null) {
				returnObject.put("success", "false");
				returnObject.put("error", "User doesn't exist!");
			} else {
				if (accFrom == null || accTo == null) {
					returnObject.put("success", "false");
					returnObject.put("error", "Account doesn't exist!");
				} else {
					if (accFrom.getBalance() >= amount) {
						Transaction t = new Transaction();
						t.setDateTime(new Date());
						t.setAmount(amount);
						t.setFrom(email);
						t.setTo(transferee);
						t.setType("Transfer");
						t.setId(commonDAL.getNextUserIdSequence("transactionId"));
						transRepo.save(t);
	
						double newBal = accFrom.getBalance() - amount;
						accFrom.setBalance(newBal);
	
						newBal = accTo.getBalance() + amount;
						accTo.setBalance(newBal);
	
						accountRepository.save(accFrom);
						accountRepository.save(accTo);
	
						returnObject.put("success", "true");
	
					} else {
						returnObject.put("success", "false");
						returnObject.put("error", "Balance is not enough!");
					}
				}
			}
		}catch (Exception e){
			returnObject.put("success", "false");
			returnObject.put("error", "An error occured!");
		}
		return returnObject;
	}
}