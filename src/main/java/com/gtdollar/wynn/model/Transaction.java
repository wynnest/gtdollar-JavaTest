package com.gtdollar.wynn.model;

/*
 * @author  Wynn Teo
 * @version 1.0
 * @since   2018-08-05 
 */
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "Transaction")
public class Transaction {

	@Id
	private long id;
	private String from;
	private String to;
	private String type;
	private double amount;
	@JsonFormat(timezone = "GMT+08:00", pattern = "yyyy-MM-dd'T'HH:mmXXX")
	private Date dateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
