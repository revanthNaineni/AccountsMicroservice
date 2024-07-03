package com.revanth.accountsmicroservices.mapper;

import com.revanth.accountsmicroservices.DTO.AccountsDTO;
import com.revanth.accountsmicroservices.entity.Accounts;

public class AccountsMapper {

	public static AccountsDTO mapToAccountsDto(Accounts accounts, AccountsDTO accountsDto) {
		accountsDto.setAccountNumber(accounts.getAccountNumber());
		accountsDto.setAccountType(accounts.getAccountType());
		accountsDto.setBranchAddress(accounts.getBranchAddress());
		return accountsDto;
	}

	public static Accounts mapToAccounts(AccountsDTO accountsDto, Accounts accounts) {
		accounts.setAccountNumber(accountsDto.getAccountNumber());
		accounts.setAccountType(accountsDto.getAccountType());
		accounts.setBranchAddress(accountsDto.getBranchAddress());
		return accounts;
	}

}
