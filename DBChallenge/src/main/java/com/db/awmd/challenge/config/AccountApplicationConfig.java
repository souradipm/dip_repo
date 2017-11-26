package com.db.awmd.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.db.awmd.challenge.service.EmailNotificationService;
import com.db.awmd.challenge.service.NotificationService;

@Configuration
public class AccountApplicationConfig {
	
	@Bean
	public NotificationService notificationService(){
		
		return new EmailNotificationService();
	}
	
	
	
	

}
