package com.db.awmd.challenge.repository;

import java.math.BigDecimal;
import java.util.List;

import com.db.awmd.challenge.dto.AccountDTO;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.OverDraftAccountException;

public interface AccountsRepository {

  public void createAccount(AccountDTO account) throws DuplicateAccountIdException;

  public AccountDTO getAccount(String accountId);

  public void clearAccounts();
  
  public boolean debit(AccountDTO fromAccount,BigDecimal amount) throws OverDraftAccountException;
  
  public boolean credit(AccountDTO toAccount,BigDecimal amount);
}
