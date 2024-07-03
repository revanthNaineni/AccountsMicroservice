package com.revanth.accountsmicroservices.service;

import com.revanth.accountsmicroservices.DTO.CustomerDetailsDTO;

public interface ICustomersService {
	
	CustomerDetailsDTO fetchCustomerDetails(String mobileNumber, String correlationId);

}
