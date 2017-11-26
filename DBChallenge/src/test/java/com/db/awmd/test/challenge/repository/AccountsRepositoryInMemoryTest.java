/**
 * 
 */
package com.db.awmd.test.challenge.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.db.awmd.challenge.dto.AccountDTO;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.OverDraftAccountException;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.test.challenge.config.TestAccountConfig;

/**
 * @author HP
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes=TestAccountConfig.class,loader=AnnotationConfigContextLoader.class)
public class AccountsRepositoryInMemoryTest {

	/**
	 * @throws java.lang.Exception
	 */

	@Autowired
	private AccountsRepository repository;

	AccountDTO accountDto;
	
	AccountDTO fromAccount;
	
	AccountDTO toAccount;
	
	List<AccountDTO> accountList;

	@Before
	@PostConstruct
	public void setUp() throws Exception {

		accountDto = new AccountDTO();

		accountDto.setAccountBalance(new BigDecimal(809.9));

		accountDto.setAccountId("A34567");
		
		fromAccount= accountDto;
		
		toAccount = new AccountDTO();
		
		toAccount.setAccountId("B34567");
		
		toAccount.setAccountBalance(new BigDecimal(856.78));
		
		accountList=new ArrayList<>();
		accountList.add(fromAccount);
		accountList.add(toAccount);
		
	}

	/**
	 * Test method for
	 * {@link com.db.awmd.challenge.repository.AccountsRepositoryInMemory#createAccount(com.db.awmd.challenge.dto.AccountDTO)}.
	 */
	@Test
	public void testCreateAccount() {
		
		repository.createAccount(accountDto);
		
		
	}

	/**
	 * Test method for
	 * {@link com.db.awmd.challenge.repository.AccountsRepositoryInMemory#getAccount(java.lang.String)}.
	 */
	@Test
	public void testGetAccount() {
		repository.getAccount("A34567");
	}

	/**
	 * Test method for
	 * {@link com.db.awmd.challenge.repository.AccountsRepositoryInMemory#clearAccounts()}.
	 */
	@Test
	public void testClearAccounts() {
		
		repository.clearAccounts();
		
	}

	/**
	 * Test method for
	 * {@link com.db.awmd.challenge.repository.AccountsRepositoryInMemory#transferMoney(com.db.awmd.challenge.dto.AccountDTO, com.db.awmd.challenge.dto.AccountDTO, java.math.BigDecimal)}.
	 */
	@Test
	public void testTransferMoneyBetweenTwoAccounts() {
		
		TransferBetweenAccounts tr1=new TransferBetweenAccounts(fromAccount,toAccount,new BigDecimal(90.98));
		
		TransferBetweenAccounts tr2=new TransferBetweenAccounts(toAccount,fromAccount,new BigDecimal(90.99));
		
		
		Thread th1=new Thread(tr1);
		
		Thread th2=new Thread(tr2);
		
		th1.start();
		
		th2.start();
		
	}
	
	private class TransferBetweenAccounts implements Runnable{

		
		private BigDecimal amount;
		
		private AccountDTO account1;
		
		private AccountDTO account2;
		
		TransferBetweenAccounts(AccountDTO account1,AccountDTO account2,BigDecimal amount){
			this.amount=amount;
			
			this.account1=account1;
			this.account2=account2;
		}
		
		
		@Override
		public void run() {
			
			// TODO Auto-generated method stub
			
			try{
			
				repository.debit(account1, amount);
				repository.credit(account2, amount);
			
			}catch(OverDraftAccountException e){
				e.printStackTrace();
			}
			
			
		}
		
	}

}
