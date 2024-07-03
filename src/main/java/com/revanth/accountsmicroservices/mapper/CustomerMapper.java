package com.revanth.accountsmicroservices.mapper;

import com.revanth.accountsmicroservices.DTO.CustomerDTO;
import com.revanth.accountsmicroservices.DTO.CustomerDetailsDTO;
import com.revanth.accountsmicroservices.entity.Customer;

public class CustomerMapper {
	
	public static CustomerDTO mapToCustomerDto(Customer customer, CustomerDTO customerDto) {
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setMobileNumber(customer.getMobileNumber());
        return customerDto;
    }
	
	public static CustomerDetailsDTO mapToCustomerDetailsDto(Customer customer, CustomerDetailsDTO customerDetailsDTO) {
		customerDetailsDTO.setName(customer.getName());
		customerDetailsDTO.setEmail(customer.getEmail());
		customerDetailsDTO.setMobileNumber(customer.getMobileNumber());
        return customerDetailsDTO;
    }

    public static Customer mapToCustomer(CustomerDTO customerDto, Customer customer) {
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setMobileNumber(customerDto.getMobileNumber());
        return customer;
    }

}
