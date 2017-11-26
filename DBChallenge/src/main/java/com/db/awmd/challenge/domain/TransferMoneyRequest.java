package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferMoneyRequest {
	
	@NotEmpty
	@NotNull
	private String fromAccountId;
	
	@NotEmpty
	@NotNull
	private String toAccountId;
	
	@NotEmpty
	@NotNull
	@Min(value=0,message="Transfer amount should be positve")
	private BigDecimal amount;
	
	

}
