package com.dev.apiprevisaotempo.service.impl;


import com.dev.apiprevisaotempo.entity.City;
import com.dev.apiprevisaotempo.repository.CityRepository;
import com.dev.apiprevisaotempo.service.CityService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CityDataServiceImpl implements CityService {

    private final CityRepository cityRepository;

    public CityDataServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }


    /*public void fetchAndSaveCities(ForecastRequest forecastRequest) {
        String citiesUrl = "http://servicos.cptec.inpe.br/XML/listaCidades?city=" + forecastRequest ;
        RestTemplate restTemplate = new RestTemplate();
        String xmlResponse = restTemplate.getForObject(citiesUrl, String.class);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlResponse)));

            doc.getDocumentElement().normalize();

            NodeList cityNodes = doc.getElementsByTagName("cidade");


                Node cityNode = cityNodes.item(0);

                    Element cityElement = (Element) cityNode;
                    String cityName = cityElement.getElementsByTagName("nome").item(0).getTextContent();
                    String uf = cityElement.getElementsByTagName("uf").item(0).getTextContent();
                    String cityId = cityElement.getElementsByTagName("id").item(0).getTextContent();

                    City city = new City();
                    city.setId(Long.parseLong(cityId));
                    city.setNome(cityName);
                    city.setUf(uf);
                    city.setAtualizacao(LocalDate.now());
                    cityRepository.save(city);


        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void buscarCidades(String nomeCidade){
        String cidadeCodificada;
        cidadeCodificada = URLEncoder.encode(nomeCidade, StandardCharsets.UTF_8);
        String url = "http://servicos.cptec.inpe.br/XML/listaCidades?city=" + cidadeCodificada;
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2XmlHttpMessageConverter converter = new MappingJackson2XmlHttpMessageConverter();
        converter.setObjectMapper(new XmlMapper());
        restTemplate.getMessageConverters().add(converter);
        City[] cities = restTemplate.getForObject(url, City[].class);

        assert cities != null;
        for (City city : cities) {
            if (cityRepository.findByNome(city.getNome()) == null) {
                cityRepository.save(city);
            }
        }
    }

    @Override
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
}
