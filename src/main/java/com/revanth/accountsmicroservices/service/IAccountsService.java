package com.revanth.accountsmicroservices.service;

import com.revanth.accountsmicroservices.DTO.CustomerDTO;

public interface IAccountsService {

	void createAccount(CustomerDTO customerDTO);

	CustomerDTO fetchAccountDetails(String mobileNumber);
	
	boolean updateAccount(CustomerDTO customerDTO);
	
	boolean deleteAccount(String mobileNumber);
	
	boolean updateCommunicationStatus(Long accountNumber);
	
	

}
