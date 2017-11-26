package com.db.awmd.challenge.web;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferMoneyRequest;
import com.db.awmd.challenge.domain.TransferMoneyResponse;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.OverDraftAccountException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.NotificationService;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

	private final AccountsService accountsService;

	private final NotificationService notificationService;

	@Autowired
	public AccountsController(AccountsService accountsService, NotificationService notificationService) {
		this.accountsService = accountsService;
		this.notificationService = notificationService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
		log.info("Creating account {}", account);

		try {
			this.accountsService.createAccount(account);
		} catch (DuplicateAccountIdException daie) {
			return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping(path = "/{accountId}")
	public Account getAccount(@PathVariable String accountId) {
		log.info("Retrieving account for id {}", accountId);
		return this.accountsService.getAccount(accountId);
	}

	@PostMapping(path = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> transferMoney(@RequestBody TransferMoneyRequest transrequest) {

		try {

			if (log.isDebugEnabled())
				log.debug("Trasnfering " + transrequest.getAmount().doubleValue() + " from account "
						+ transrequest.getFromAccountId() + " to " + transrequest.getToAccountId());

			Account fromAccount = new Account(transrequest.getFromAccountId());

			Account toAccount = new Account(transrequest.getToAccountId());

			BigDecimal amount = transrequest.getAmount();

			boolean flag = this.accountsService.transferMoney(fromAccount, toAccount, amount);

			TransferMoneyResponse response = new TransferMoneyResponse(flag);

			this.notificationService.notifyAboutTransfer(fromAccount,
					"Amount " + amount.intValue() + " transferred succeesfully");

			return new ResponseEntity<Object>(response, HttpStatus.OK);

		} catch (OverDraftAccountException ode) {
			return new ResponseEntity<>(ode.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

}
