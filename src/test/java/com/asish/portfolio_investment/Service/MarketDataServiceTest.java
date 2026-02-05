package com.asish.portfolio_investment.Service;

import com.asish.portfolio_investment.dto.MarketPriceResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarketDataServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MarketDataService marketDataService;

    @Test
    void getLivePrice_success() {

        String symbol = "TATASTEEL";
        String expectedUrl = "http://localhost:5000/market/live?symbol=" + symbol;

        MarketPriceResponseDTO mockResponse = new MarketPriceResponseDTO();
        mockResponse.setSymbol(symbol);
        mockResponse.setPrice(123.45);

        when(restTemplate.getForObject(expectedUrl, MarketPriceResponseDTO.class))
                .thenReturn(mockResponse);


        MarketPriceResponseDTO response = marketDataService.getLivePrice(symbol);


        assertNotNull(response);
        assertEquals(symbol, response.getSymbol());
        assertEquals(123.45, response.getPrice());

        verify(restTemplate, times(1))
                .getForObject(expectedUrl, MarketPriceResponseDTO.class);
    }

    @Test
    void getLivePrice_serviceFailure() {

        String symbol = "TATASTEEL";
        String expectedUrl = "http://localhost:5000/market/live?symbol=" + symbol;

        when(restTemplate.getForObject(expectedUrl, MarketPriceResponseDTO.class))
                .thenThrow(new RestClientException("Service down"));


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> marketDataService.getLivePrice(symbol)
        );

        assertEquals("Live market data service unavailable", exception.getMessage());
    }
}
