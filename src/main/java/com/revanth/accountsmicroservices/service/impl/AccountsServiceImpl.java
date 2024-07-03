package com.revanth.accountsmicroservices.service.impl;

import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import com.revanth.accountsmicroservices.DTO.AccountsDTO;
import com.revanth.accountsmicroservices.DTO.AccountsMsgDto;
import com.revanth.accountsmicroservices.DTO.CustomerDTO;
import com.revanth.accountsmicroservices.constants.AccountConstants;
import com.revanth.accountsmicroservices.controllers.CustomerController;
import com.revanth.accountsmicroservices.entity.Accounts;
import com.revanth.accountsmicroservices.entity.Customer;
import com.revanth.accountsmicroservices.exceptions.CustomerAlreadyExistsException;
import com.revanth.accountsmicroservices.exceptions.ResourceNotFoundException;
import com.revanth.accountsmicroservices.mapper.AccountsMapper;
import com.revanth.accountsmicroservices.mapper.CustomerMapper;
import com.revanth.accountsmicroservices.repositories.AccountsRepository;
import com.revanth.accountsmicroservices.repositories.CustomerRepository;
import com.revanth.accountsmicroservices.service.IAccountsService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	private AccountsRepository accountsRepository;
	private CustomerRepository customerRepository;
	private final StreamBridge streamBridge;

	@Override
	public void createAccount(CustomerDTO customerDTO) {

		Customer customer = CustomerMapper.mapToCustomer(customerDTO, new Customer());
		Optional<Customer> mobileNumber = customerRepository.findByMobileNumber(customer.getMobileNumber());
		if (mobileNumber.isPresent()) {
			throw new CustomerAlreadyExistsException(
					"Customer already registered with given mobileNumber " + customer.getMobileNumber());
		}
		Customer savedCustomer = customerRepository.save(customer);
		Accounts savedAccount = accountsRepository.save(createNewAccount(customer));
		sendCommunication(savedAccount, savedCustomer);
    }

    private void sendCommunication(Accounts account, Customer customer) {
        var accountsMsgDto = new AccountsMsgDto(account.getAccountNumber(), customer.getName(),
                customer.getEmail(), customer.getMobileNumber());
        logger.info("Sending Communication request for the details: {}", accountsMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", accountsMsgDto);
        logger.info("Is the Communication request successfully triggered ? : {}", result);
    }

	public Accounts createNewAccount(Customer customer) {

		Accounts accounts = new Accounts();
		accounts.setCustomerId(customer.getCustomerId());
		long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
		accounts.setAccountNumber(randomAccNumber);
		accounts.setAccountType(AccountConstants.SAVINGS);
		accounts.setBranchAddress(AccountConstants.ADDRESS);
		return accounts;

	}

	@Override
	public CustomerDTO fetchAccountDetails(String mobileNumber) {
		Customer customer = customerRepository.findByMobileNumber(mobileNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
				() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

		CustomerDTO customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDTO());
		customerDto.setAccountsDTO(AccountsMapper.mapToAccountsDto(accounts, new AccountsDTO()));
		return customerDto;
	}

	@Override
	public boolean updateAccount(CustomerDTO customerDTO) {
		boolean isUpdated = false;
		AccountsDTO accountsDTO = customerDTO.getAccountsDTO();
		if (accountsDTO != null) {
			Accounts accounts = accountsRepository.findById(accountsDTO.getAccountNumber())
					.orElseThrow(() -> new ResourceNotFoundException("Account", "Account Number",
							accountsDTO.getAccountNumber().toString()));
			AccountsMapper.mapToAccounts(accountsDTO, accounts);
			accounts = accountsRepository.save(accounts);
			Customer customer = customerRepository.findById(accounts.getCustomerId())
					.orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerID",
							accountsDTO.getAccountNumber().toString()));
			CustomerMapper.mapToCustomer(customerDTO, customer);
			customerRepository.save(customer);
			isUpdated = true;
		}
		return isUpdated;
	}

	@Override
	public boolean deleteAccount(String mobileNumber) {
		Customer customer = customerRepository.findByMobileNumber(mobileNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
		accountsRepository.deleteByCustomerId(customer.getCustomerId());
		customerRepository.deleteById(customer.getCustomerId());
		return true;}

	@Override
    public boolean updateCommunicationStatus(Long accountNumber) {
        boolean isUpdated = false;
        if(accountNumber !=null ){
            Accounts accounts = accountsRepository.findById(accountNumber).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountNumber.toString())
            );
            accounts.setCommunicationSw(true);
            accountsRepository.save(accounts);
            isUpdated = true;
        }
        return  isUpdated;
    }
}
