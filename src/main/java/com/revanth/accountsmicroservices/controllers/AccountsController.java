package com.revanth.accountsmicroservices.controllers;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revanth.accountsmicroservices.DTO.AccountsContactInfoDTO;
import com.revanth.accountsmicroservices.DTO.CustomerDTO;
import com.revanth.accountsmicroservices.DTO.ErrorResponseDTO;
import com.revanth.accountsmicroservices.DTO.ResponseDTO;
import com.revanth.accountsmicroservices.constants.AccountConstants;
import com.revanth.accountsmicroservices.service.IAccountsService;

import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

@Tag(name = "CRUD REST API for accounts in ZBank", description = "CRUD REST API to CREATE, READ, UPDATE, DELETE accounts in ZBank")
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class AccountsController {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final IAccountsService iAccountsService;

    public AccountsController(IAccountsService iAccountsService) {
        this.iAccountsService = iAccountsService;
    }

    @Value("${info.build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private AccountsContactInfoDTO accountsContactInfoDTO;

    @ApiResponse(responseCode = "201", description = "Http Status Created")
    @Operation(summary = "Create account REST API", description = "REST API to create new customer & Account inside ZBank")
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createAccount(@Valid @RequestBody CustomerDTO customerDTO) {
        iAccountsService.createAccount(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(AccountConstants.STATUS_201, AccountConstants.MESSAGE_201));

    }

    @ApiResponse(responseCode = "200", description = "Http Status OK")
    @Operation(summary = "Read account REST API", description = "REST API to read Account details inside ZBank")
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDTO> fetchAccountDetails(@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        CustomerDTO customerDTO = iAccountsService.fetchAccountDetails(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDTO);

    }

    @ApiResponses({@ApiResponse(responseCode = "200", description = "Http Status OK"), @ApiResponse(responseCode = "500", description = "Http Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))})
    @Operation(summary = "Update account REST API", description = "REST API to update  customer & Account inside ZBank")
    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateAccountDetails(@Valid @RequestBody CustomerDTO customerDTO) {
        boolean isUpdated = iAccountsService.updateAccount(customerDTO);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(AccountConstants.STATUS_200, AccountConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(AccountConstants.STATUS_500, AccountConstants.MESSAGE_500));
        }

    }

    @ApiResponses({@ApiResponse(responseCode = "200", description = "Http Status OK"), @ApiResponse(responseCode = "500", description = "Http Status Internal Server Error")})
    @Operation(summary = "Delete account REST API", description = "REST API to delete  customer & Account inside ZBank")
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteAccountDetails(@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(AccountConstants.STATUS_200, AccountConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDTO(AccountConstants.STATUS_417, AccountConstants.MESSAGE_417_DELETE));
        }

    }

	
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Http Status OK"),
			@ApiResponse(responseCode = "500", description = "Http Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))) })

	@Operation(summary = "Get Build information", description = "Get Build information that is deployed into accounts microservice")
	@Retry(name = "getBuildInfo", fallbackMethod = "getBuildInfoFallBack")
	@GetMapping("/build-info")
	public ResponseEntity<String> getBuildInfo() throws TimeoutException {
		logger.info("getBuildInfo is invoked");
		return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
	}
	
	public ResponseEntity<String> getBuildInfoFallBack(Throwable throwable) {
		logger.info("getBuildInfoFallBack is invoked");
		return ResponseEntity.status(HttpStatus.OK).body("0.9");
	}
	 


    @ApiResponses({@ApiResponse(responseCode = "200", description = "Http Status OK"), @ApiResponse(responseCode = "500", description = "Http Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))})
    @Operation(summary = "Get Java version", description = "Get Java version from environmental variables")
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("JAVA_HOME"));
    }


    @ApiResponses({@ApiResponse(responseCode = "200", description = "Http Status OK"), @ApiResponse(responseCode = "500", description = "Http Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))})
    @Operation(summary = "Get Contact info", description = "Get Contact info")
    @GetMapping("/contact-info")
    public ResponseEntity<AccountsContactInfoDTO> getContactInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(accountsContactInfoDTO);
    }


}
