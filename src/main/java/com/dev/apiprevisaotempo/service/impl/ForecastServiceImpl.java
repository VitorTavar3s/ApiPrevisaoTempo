package com.dev.apiprevisaotempo.service.impl;

import com.dev.apiprevisaotempo.dto.request.ForecastRequest;
import com.dev.apiprevisaotempo.dto.response.CityResponse;
import com.dev.apiprevisaotempo.entity.City;
import com.dev.apiprevisaotempo.entity.Forecast;
import com.dev.apiprevisaotempo.mapper.CityMapper;
import com.dev.apiprevisaotempo.repository.CityRepository;
import com.dev.apiprevisaotempo.repository.ForecastRepository;
import com.dev.apiprevisaotempo.service.CityService;
import com.dev.apiprevisaotempo.service.ForecastService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Service
public class ForecastServiceImpl implements ForecastService {

    private final CityRepository cityRepository;

    private final ForecastRepository forecastRepository;

    private final CityService cityService;

    private final CityMapper cityMapper;


    @Override
    public Forecast getForecastByCityId(Long idCity) {
        return null;
    }

    @Override
    public CityResponse getForecastByCityName(ForecastRequest forecastRequest) {
        String name = forecastRequest.getCityName();
        cityService.buscarCidades(name);

        City city = cityRepository.findByNome(forecastRequest.getCityName());
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://servicos.cptec.inpe.br/XML/cidade/" + city.getId() + "/previsao.xml";
        XmlMapper xmlMapper = new XmlMapper();
        MappingJackson2XmlHttpMessageConverter converter = new MappingJackson2XmlHttpMessageConverter(xmlMapper);
        restTemplate.getMessageConverters().add(converter);
        City city1 = restTemplate.getForObject(url, City.class);
        assert city1 != null;
        city1.setId(city.getId());

        for (Forecast forecast : city1.getPrevisoes()) {
            String dataString = forecast.getDia();
            LocalDate data = LocalDate.parse(dataString);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataFormatada = data.format(formatter);
            forecast.setDia(dataFormatada);
        }

        forecastRepository.saveAll(city1.getPrevisoes());
        return cityMapper.toCityResponse(city1);
    }



}
