package br.avaliatri.controllers;

import br.avaliatri.dtos.QuestaoDTO;
import br.avaliatri.excecoes.Excecao;
import br.avaliatri.models.Alternativa;
import br.avaliatri.models.Questao;
import br.avaliatri.services.AlternativaService;
import br.avaliatri.services.ProvaService;
import br.avaliatri.services.QuestaoService;
import br.avaliatri.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "questoes")
@CrossOrigin
public class QuestaoController {
    private QuestaoService service;
    private ProvaService provaService;
    private AlternativaService alternativaService;

    public QuestaoController(QuestaoService service, ProvaService provaService, AlternativaService alternativaService) {
        this.service = service;
        this.provaService = provaService;
        this.alternativaService = alternativaService;
    }

    @GetMapping()
    public ResponseEntity<Page<Questao>> findAll(
        @RequestParam(value="page", defaultValue ="0") Integer page,
        @RequestParam(value="linesPerPage", defaultValue ="4")Integer linesPerPage,
        @RequestParam(value="orderBy", defaultValue ="id")String orderBy,
        @RequestParam(value="direction", defaultValue = "DESC")String direction
    ) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<Questao> questaos = this.service.findAll(pageRequest);
        return ResponseEntity.ok().body(questaos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestaoDTO> findById(@PathVariable("id") Integer id) throws Excecao {
        Questao questao = this.service.findById(id);
        return ResponseEntity.ok().body(QuestaoService.convertEntityToDto(questao));
    }

    @PostMapping
    public ResponseEntity<QuestaoDTO> save(@RequestBody QuestaoDTO questaoDTO) {

        Questao questao = QuestaoService.convertDtoToEntity(questaoDTO);
        questao = this.service.save(questao);

        return ResponseEntity.ok().body(QuestaoService.convertEntityToDto(questao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) throws Excecao {
        Questao questao = service.findById(id);

        this.service.delete(questao);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Questao> editQuestao(
            @PathVariable("id") Integer id,
            @RequestBody QuestaoDTO dtoNew
    ) throws Excecao {
        Questao questao = service.findById(id);

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
        questao.setTextoApoio(dtoNew.getTextoApoio());
        questao = service.update(questao);
        return ResponseEntity.ok().body(questao);
    }

}
