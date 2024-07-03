package com.revanth.accountsmicroservices.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "CustomerDetails", description = "Schema to hold Customer, Account, Cards and Loans information")
public class CustomerDetailsDTO {
	
	@Schema(description = "Name of the customer", example = "Test")
	@NotEmpty(message = "Name cannot be null or empty")
	@Size(min = 3, max = 50, message = "The length of customer name should be between 3 to 50")
	private String name;

	@Schema(description = "Email address of the customer", example = "Test@Gmail.com")
	@NotEmpty(message = "Email cannot be null or empty")
	@Email(message = "Email address should be a valid value")
	private String email;

	@Schema(description = "Mobile Number of the customer", example = "8888888888")
	@Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
	private String mobileNumber;

	@Schema(description = "Account details of the customer")
	private AccountsDTO accountsDTO;
	
	@Schema(description = "Loans details of the customer")
	private LoansDto loansDto;
	
	@Schema(description = "Cards details of the customer")
	private CardsDto cardsDto;

}
