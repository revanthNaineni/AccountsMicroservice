package com.revanth.accountsmicroservices.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "Accounts", description = "Schema to hold Account information")
public class AccountsDTO {

	@Schema(description = "Name of the customer")
	@NotEmpty(message = "Account Number can not be null or empty")
	@Pattern(regexp = "(^$|[0-9]{10})", message = "Account Number must be 10 digits")
    private Long accountNumber;

	@Schema(description = "Type of the account")
	@NotEmpty(message = "Account Type can not be null or empty")
    private String accountType;

	@Schema(description = "Branch Address")
	@NotEmpty(message = "Branch address can not be null or empty")
    private String branchAddress;
}
