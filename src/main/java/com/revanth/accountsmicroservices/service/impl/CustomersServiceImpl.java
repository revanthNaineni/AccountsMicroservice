package com.revanth.accountsmicroservices.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.revanth.accountsmicroservices.DTO.AccountsDTO;
import com.revanth.accountsmicroservices.DTO.CardsDto;
import com.revanth.accountsmicroservices.DTO.CustomerDetailsDTO;
import com.revanth.accountsmicroservices.DTO.LoansDto;
import com.revanth.accountsmicroservices.entity.Accounts;
import com.revanth.accountsmicroservices.entity.Customer;
import com.revanth.accountsmicroservices.exceptions.ResourceNotFoundException;
import com.revanth.accountsmicroservices.mapper.AccountsMapper;
import com.revanth.accountsmicroservices.mapper.CustomerMapper;
import com.revanth.accountsmicroservices.repositories.AccountsRepository;
import com.revanth.accountsmicroservices.repositories.CustomerRepository;
import com.revanth.accountsmicroservices.service.ICustomersService;
import com.revanth.accountsmicroservices.service.client.CardsFeignClient;
import com.revanth.accountsmicroservices.service.client.LoansFeignClient;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {
	
	private AccountsRepository accountsRepository;
	
	private CustomerRepository customerRepository;
	
	private CardsFeignClient cardsFeignClient;
	
	private LoansFeignClient loansFeignClient;

	@Override
    public CustomerDetailsDTO fetchCustomerDetails(String mobileNumber, String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDTO customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDTO());
        customerDetailsDto.setAccountsDTO(AccountsMapper.mapToAccountsDto(accounts, new AccountsDTO()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId, mobileNumber);
        if(null != loansDtoResponseEntity) {
            customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
        }

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
        if(null != cardsDtoResponseEntity) {
            customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        }


        return customerDetailsDto;

    }

}
