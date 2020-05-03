package br.avaliatri.controllers;

import br.avaliatri.dtos.ProvaRespondidaDTO;
import br.avaliatri.excecoes.Excecao;
import br.avaliatri.models.ProvaRespondida;
import br.avaliatri.services.ProvaRespondidaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "resultados")
public class ResultadoController {

    private ProvaRespondidaService provaRespondidaService;

    public ResultadoController(ProvaRespondidaService provaRespondidaService) {
        this.provaRespondidaService = provaRespondidaService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProvaRespondidaDTO> getResultadoById(
            @PathVariable("id") Integer id
    ) throws Excecao {
        ProvaRespondidaDTO resultadoDTO = provaRespondidaService.getResultadoDtoById(id);
        return ResponseEntity.ok().body(resultadoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProvaRespondidaDTO> atualizarResultado(
            @PathVariable("id") Integer id,
            @RequestBody ProvaRespondidaDTO dto
    ) throws Excecao {
        ProvaRespondida provaRespondida = provaRespondidaService.getResultadoById(id);
        provaRespondida.setObservacao(dto.getObservacao());
        provaRespondida = this.provaRespondidaService.save(provaRespondida);
        ProvaRespondidaDTO dtoRes = ProvaRespondidaService.convertEntityToDto(provaRespondida);
        return ResponseEntity.ok().body(dtoRes);
    }
}
