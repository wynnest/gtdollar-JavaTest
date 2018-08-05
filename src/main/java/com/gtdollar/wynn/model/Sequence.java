package com.gtdollar.wynn.model;

/*
 * @author  Wynn Teo
 * @version 1.0
 * @since   2018-08-05 
 */
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Sequence")
public class Sequence {
	@Id
	private String id;
	private int seq;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

}
