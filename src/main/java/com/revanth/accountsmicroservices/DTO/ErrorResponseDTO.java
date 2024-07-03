package com.revanth.accountsmicroservices.DTO;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
@Schema(name = "Error Response", description = "Schema to hold error response information")
public class ErrorResponseDTO {
	
	@Schema(description = "API path invoked by client")
	private String apiPath;
	
	@Schema(description = "Error code in the response", example = "404")
	private HttpStatus errorCode;
	
	@Schema(description = "Error message in the response", example = "Request processed sucessfully")
	private String errorMessage;
	
	@Schema(description = "Time representing when error happened")
	private LocalDateTime errorTime;

}
