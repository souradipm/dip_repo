package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.dto.AccountDTO;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.OverDraftAccountException;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

	private final Map<String, AccountDTO> accounts = new ConcurrentHashMap<>();


	@Override
	public void createAccount(AccountDTO account) throws DuplicateAccountIdException {
		AccountDTO previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
		if (previousAccount != null) {
			throw new DuplicateAccountIdException("Account id " + account.getAccountId() + " already exists!");
		}
	}

	@Override
	public AccountDTO getAccount(String accountId) {
		return accounts.get(accountId);
	}

	@Override
	public void clearAccounts() {
		accounts.clear();
	}

	@Override
	public boolean debit(AccountDTO fromAccount, BigDecimal amount) throws OverDraftAccountException {
		if (fromAccount.getAccountId() != null && fromAccount.hasSufficientaccountBalance()) {
			fromAccount = accounts.get(fromAccount.getAccountId());

			if (fromAccount != null) {
				BigDecimal bal = fromAccount.getAccountBalance().subtract(amount);

				if (bal.doubleValue() > 0) {
					fromAccount.setAccountBalance(bal);
					return true;
				} else
					return false;

			}

			else
				throw new NullPointerException("No Account exists with the account id");

		} else
			throw new OverDraftAccountException("Account does not have sufficient Balance");

	}

	@Override
	public boolean credit(AccountDTO toAccount, BigDecimal amount) throws NullPointerException {

		if (amount.doubleValue() > 0 && toAccount.getAccountId() != null) {

			toAccount = accounts.get(toAccount.getAccountId());

			toAccount.credit(amount);

			return true;

		} else
			throw new NullPointerException("Insufficient Data");

	}

	
}
