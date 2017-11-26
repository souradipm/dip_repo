package com.db.awmd.challenge.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TransferMoneyResponse {

	private String success;

	@JsonCreator
	public TransferMoneyResponse(@JsonProperty("success") boolean success) {
		if(success)
			this.success="SUCCESS";
		else
			this.success="FAILED";
	}

}
