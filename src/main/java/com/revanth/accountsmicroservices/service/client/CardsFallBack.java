package com.revanth.accountsmicroservices.service.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.revanth.accountsmicroservices.DTO.CardsDto;

@Component
public class CardsFallBack implements CardsFeignClient {

	@Override
	public ResponseEntity<CardsDto> fetchCardDetails(String correlationId, String mobileNumber) {
		return null;
	}

}
