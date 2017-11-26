package com.db.awmd.test.challenge.config;

import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.repository.AccountsRepositoryInMemory;

@RunWith(SpringRunner.class)
@Configuration
public class TestAccountConfig {
	
	@Bean
	public AccountsRepository repository(){
		return new AccountsRepositoryInMemory();
	}

}
