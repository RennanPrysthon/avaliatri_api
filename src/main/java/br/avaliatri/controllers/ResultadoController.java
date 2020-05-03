package br.avaliatri.controllers;

import br.avaliatri.dtos.ProvaRespondidaDTO;
import br.avaliatri.dtos.util.QuestaoRespondidaDTO;
import br.avaliatri.excecoes.Excecao;
import br.avaliatri.services.ProvaRespondidaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        ProvaRespondidaDTO resultadoDTO = provaRespondidaService.getResultadoById(id);

        return ResponseEntity.ok().body(resultadoDTO);
    }
}
