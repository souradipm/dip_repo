package com.db.awmd.test.challenge.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.OverDraftAccountException;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.repository.AccountsRepositoryInMemory;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.test.challenge.config.TestAccountConfig;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=TestAccountConfig.class,loader=AnnotationConfigContextLoader.class)
public class AccountsServiceTest {
	
	
	@Autowired
	private AccountsRepository repository;
	
	private AccountsService service;
	
	private Account fromAccount; 
	
	private Account toAccount; 
	
	@Before
	public void setUp() throws Exception {
		
		service=new AccountsService(repository);
		
		fromAccount=new Account("A34567");
		
		fromAccount.setBalance(new BigDecimal(987.90));
		
		toAccount=new Account("B34568");
		
		toAccount.setBalance(new BigDecimal(999.0));
		
		
	}

	@Test
	public void testCreateAccount() {
		service.createAccount(fromAccount);
		service.createAccount(toAccount);
	}

	@Test
	public void testGetAccount() {
		service.getAccount("B34568");
	}

	@Test
	public void testTransferMoney() throws OverDraftAccountException {
		try{
		service.transferMoney(fromAccount, toAccount, new BigDecimal(10.89));
		}catch(OverDraftAccountException e){
			throw e;
		}
	}

}
