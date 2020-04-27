package br.avaliatri.controllers;

import br.avaliatri.dtos.QuestaoDTO;
import br.avaliatri.excecoes.Excecao;
import br.avaliatri.models.Alternativa;
import br.avaliatri.models.Questao;
import br.avaliatri.services.AlternativaService;
import br.avaliatri.services.ProvaService;
import br.avaliatri.services.QuestaoService;
import br.avaliatri.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "questoes")
public class QuestaoController {
    private QuestaoService service;
    private ProvaService provaService;
    private AlternativaService alternativaService;

    public QuestaoController(QuestaoService service, ProvaService provaService, AlternativaService alternativaService) {
        this.service = service;
        this.provaService = provaService;
        this.alternativaService = alternativaService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) throws Excecao {
        Questao questao = service.findById(id);

        if(questao.getProva().getIs_published()) {
            throw new Excecao("Prova que contem a questao com id " + id + " esta publicada, por isso nao pode ser excluida");
        }

        this.service.delete(questao);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestaoDTO> editQuestao(
            @PathVariable("id") Integer id,
            @RequestBody QuestaoDTO dtoNew
    ) throws Excecao {
        Questao questao = service.findById(id);

        if(questao.getProva().getIs_published()) {
            throw new Excecao("Prova que contem a questao com id " + id + " esta publicada, por isso nao pode ser alterada");
        }

        for (Alternativa a: questao.getAlternativas()) {
            if(a.getOpcao().equalsIgnoreCase("A")){
                a.setTexto(dtoNew.getAlternativaA());
            }
            if(a.getOpcao().equalsIgnoreCase("B")){
                a.setTexto(dtoNew.getAlternativaB());
            }
            if(a.getOpcao().equalsIgnoreCase("C")){
                a.setTexto(dtoNew.getAlternativaC());
            }
            if(a.getOpcao().equalsIgnoreCase("D")){
                a.setTexto(dtoNew.getAlternativaD());
            }
            if(a.getOpcao().equalsIgnoreCase("E")){
                a.setTexto(dtoNew.getAlternativaE());
            }
        }
        this.alternativaService.updateAll(questao.getAlternativas());
        questao.setEnunciado(dtoNew.getEnunciado());
        questao.setAlternativa_correta(dtoNew.getAlternativa_correta());
        questao.setUpdated_at(Utils.getInstancia().getDataAtual());

        questao = service.update(questao);

        QuestaoDTO questaoDTO = QuestaoService.convertEntityToDto(questao);
        return ResponseEntity.ok().body(questaoDTO);
    }

}
