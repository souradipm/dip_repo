package com.db.awmd.challenge.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {
	
	private String accountId;
	
	private BigDecimal accountBalance;
	
	/** method for debiting account **/
	public boolean debit(BigDecimal amount) {
		if (amount.doubleValue() > 0) {
			this.accountBalance = this.accountBalance.subtract(amount);
			return true;
		} else
			return false;
	}

	/** method for crediting account **/
	public boolean credit(BigDecimal amount) {
		if (amount.doubleValue() > 0) {
			this.accountBalance = this.accountBalance.add(amount);
			return true;
		} else
			return false;
	}

	/** method for checking sufficient accountBalance in account **/
	public boolean hasSufficientaccountBalance() {
		if (this.accountBalance.doubleValue() > 0)
			return true;
		else
			return false;
	}

	
	
	

}
