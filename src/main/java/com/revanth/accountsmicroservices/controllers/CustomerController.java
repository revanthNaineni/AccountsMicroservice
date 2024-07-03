package com.revanth.accountsmicroservices.controllers;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revanth.accountsmicroservices.DTO.CustomerDetailsDTO;
import com.revanth.accountsmicroservices.service.ICustomersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;

@Tag(name = "REST API for Customers in ZBank", description = "REST API to fetchcustomer details in ZBank")
@RestController
@RequestMapping(path = "/api", produces = { MediaType.APPLICATION_JSON_VALUE })
@Validated
public class CustomerController {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	private ICustomersService iCustomersService;

	public CustomerController(ICustomersService iCustomersService) {
		this.iCustomersService = iCustomersService;
	}

	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Http Status OK"),
			@ApiResponse(responseCode = "500", description = "Http Status Internal Server Error") })
	@Operation(summary = "Fetch Customer Details REST API", description = "REST API to fetch Customer Details based on a mobile number")
	 @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<CustomerDetailsDTO> fetchCustomerDetails(@RequestHeader("eazybank-correlation-id")
                                                                       String correlationId,
                                                                    @RequestParam @Pattern(regexp="(^$|[0-9]{10})",
                                                                            message = "Mobile number must be 10 digits")
                                                                   String mobileNumber) {
        logger.info("fetchCustomerDetails method start");
        CustomerDetailsDTO customerDetailsDto = iCustomersService.fetchCustomerDetails(mobileNumber, correlationId);
        logger.info("fetchCustomerDetails method end");
        return ResponseEntity.status(HttpStatus.SC_OK).body(customerDetailsDto);

    }
}
