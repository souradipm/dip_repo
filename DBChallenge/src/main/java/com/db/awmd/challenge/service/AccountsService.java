package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.dto.AccountDTO;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.OverDraftAccountException;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

	@Getter
	private final AccountsRepository accountsRepository;

	private Account fromLock, toLock;

	@Autowired
	public AccountsService(AccountsRepository accountsRepository) {
		this.accountsRepository = accountsRepository;
	}

	public void createAccount(Account account) throws DuplicateAccountIdException {

		AccountDTO accountdto = new AccountDTO();

		accountdto.setAccountId(account.getAccountId());

		accountdto.setAccountBalance(account.getBalance());

		this.accountsRepository.createAccount(accountdto);
	}

	public Account getAccount(String accountId) {

		AccountDTO accountdto = this.accountsRepository.getAccount(accountId);

		Account account = new Account(accountdto.getAccountId(), accountdto.getAccountBalance());

		return account;
	}

	public boolean transferMoney(Account fromAccount, Account toAccount, BigDecimal amount)
			throws OverDraftAccountException {

		AccountDTO fromAccountDTO = this.accountsRepository.getAccount(fromAccount.getAccountId());

		AccountDTO toAccountDTO = this.accountsRepository.getAccount(toAccount.getAccountId());

		if (fromAccount.getAccountId().compareTo(toAccount.getAccountId()) > 0) {

			fromLock = fromAccount;
			toLock = toAccount;

		} else {
			fromLock = toAccount;
			toLock = fromAccount;
		}

		synchronized (fromLock) {
			synchronized (toLock) {
				if (this.accountsRepository.debit(fromAccountDTO, amount)
						&& this.accountsRepository.credit(toAccountDTO, amount))
					return true;
			}

		}

		return false;
	}

}
