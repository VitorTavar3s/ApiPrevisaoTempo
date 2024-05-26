package com.dev.apiprevisaotempo.service.impl;

import com.dev.apiprevisaotempo.dto.request.ForecastRequest;
import com.dev.apiprevisaotempo.dto.response.CityResponse;
import com.dev.apiprevisaotempo.dto.response.ForecastResponse;
import com.dev.apiprevisaotempo.entity.City;
import com.dev.apiprevisaotempo.entity.Forecast;
import com.dev.apiprevisaotempo.mapper.CityMapper;
import com.dev.apiprevisaotempo.repository.CityRepository;
import com.dev.apiprevisaotempo.repository.ForecastRepository;
import com.dev.apiprevisaotempo.service.CityService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
public class ForecastServiceImplTest {

    @InjectMocks
    private ForecastServiceImpl forecastService;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private ForecastRepository forecastRepository;

    @Mock
    private CityService cityService;

    @Mock
    private CityMapper cityMapper;

    @MockBean
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetForecastByCityName() {
        // Mock input
        ForecastRequest request = new ForecastRequest();
        request.setCityName("Test City");

        // Mock city entity
        City city = new City();
        city.setId(1L);
        city.setNome("Test City");

        // Expectativa da chamada à API simulada
        String responseBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<city>" +
                "<id>1</id>" +
                "<name>Test City</name>" +
                "<forecasts>" +
                "<forecast>" +
                "<date>" + LocalDate.now().toString() + "</date>" +
                "<max>30</max>" +
                "<min>20</min>" +
                "</forecast>" +
                "</forecasts>" +
                "</city>";

        mockServer.expect(requestTo("http://servicos.cptec.inpe.br/XML/cidade/1/previsao.xml"))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_XML));

        // Chamada ao método do serviço
        CityResponse response = forecastService.getForecastByCityName(request);

        // Verificações
        mockServer.verify(); // Verifica se todas as solicitações esperadas foram feitas
        assertEquals("Test City", response.getNome());
        assertEquals(1, response.getPrevisoes().size()); // Corrigido para verificar o tamanho da lista
        assertEquals(LocalDate.now().toString(), response.getPrevisoes().get(0).getDia()); // Acessa o dia da previsão
        assertEquals(30, response.getPrevisoes().get(0).getMaxima()); // Acessa a temperatura máxima da previsão
        assertEquals(20, response.getPrevisoes().get(0).getMinima());
    }
}