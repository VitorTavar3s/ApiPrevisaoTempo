package com.dev.apiprevisaotempo.controller;


import com.dev.apiprevisaotempo.DTO.CidadePrev;
import com.dev.apiprevisaotempo.DTO.Previsao;
import com.dev.apiprevisaotempo.entity.Cidade;
import com.dev.apiprevisaotempo.repository.PrevisaoRepository;
import com.dev.apiprevisaotempo.service.PrevisaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrevisaoController {

    @Autowired
    private PrevisaoService previsaoService;

    @Autowired
    private PrevisaoRepository previsaoRepository;

    @GetMapping("/previsao")
    public ResponseEntity<CidadePrev> buscarCidades(@RequestParam String codLocalidade) {
        CidadePrev cidade = previsaoService.listarPrevisao(codLocalidade);

        return ResponseEntity.ok().body(cidade);
    }
}
