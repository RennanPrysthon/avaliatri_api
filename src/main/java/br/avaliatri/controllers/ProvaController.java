package br.avaliatri.controllers;

import br.avaliatri.dtos.ProvaDTO;
import br.avaliatri.dtos.QuestaoDTO;
import br.avaliatri.excecoes.Excecao;
import br.avaliatri.excecoes.ResponseError;
import br.avaliatri.models.Alternativa;
import br.avaliatri.models.Prova;
import br.avaliatri.models.Questao;
import br.avaliatri.repositories.AlternativaRepository;
import br.avaliatri.repositories.ProvaRepository;
import br.avaliatri.repositories.QuestaoRepository;
import br.avaliatri.services.AlternativaService;
import br.avaliatri.services.ProvaService;
import br.avaliatri.services.QuestaoService;
import br.avaliatri.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.naming.ldap.PagedResultsControl;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "provas")
public class ProvaController {
    private ProvaService service;
    private ProvaRepository provaRepository;
    private AlternativaRepository alternativaRepository;
    private QuestaoRepository questaoRepository;


    public ProvaController(ProvaService service, ProvaRepository provaRepository, AlternativaRepository alternativaRepository, QuestaoRepository questaoRepository) {
        this.service = service;
        this.provaRepository = provaRepository;
        this.alternativaRepository = alternativaRepository;
        this.questaoRepository = questaoRepository;
    }

    @GetMapping("")
    public ResponseEntity<List<ProvaDTO>> getAll() {
        List<ProvaDTO> provas = this.service.findAll();
        return ResponseEntity.ok().body(provas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProvaDTO> getById(@PathVariable("id") Integer id) throws Exception {
        Prova prova = this.service.findById(id);
        ProvaDTO provaDTO = ProvaService.convertEntityToDto(prova);
        provaDTO.setQuestoes(QuestaoService.convertEntityListToDtoList(prova.getQuestoes()));
        provaDTO.setQtd_questoes(prova.getQuestoes().size());
        return ResponseEntity.ok().body(provaDTO);
    }

    @PostMapping("")
    public ResponseEntity<ProvaDTO> save(@Valid @RequestBody ProvaDTO dto, BindingResult result) throws Exception {
        verifyErrors(result);
        List<Questao> questoes;
        Prova p = ProvaService.convertDtoToEntity(dto);

        questoes = QuestaoService.convertDtoListToEntityList(dto.getQuestoes());

        p.setQuestoes(questoes);
        p = this.service.save(p);

        dto = ProvaService.convertEntityToDto(p);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/{id}/questao")
    public ResponseEntity<ProvaDTO> addQuestaoToProva(
            @PathVariable("id") Integer id,
            @Valid @RequestBody QuestaoDTO dto,
            BindingResult result
    ) throws Exception {
        verifyErrors(result);
        Prova p = this.service.findById(id);
        if(p.getIs_published()){
            throw new Excecao("Prova  com id " + id + " esta publicada, por isso nao se pode adicionar mais questoes a ela");
        }

        Questao q = QuestaoService.convertDtoToEntity(dto);
        p = this.service.addQuestao(p, q);

        return ResponseEntity.status(HttpStatus.CREATED).body(ProvaService.convertEntityToDto(p));
    }

    @PutMapping("/{id}/publicar")
    public ResponseEntity<ProvaDTO> publish(@PathVariable("id") Integer id) throws Exception {
        Prova p = this.service.findById(id);

        if(p.getIs_published()) throw new Excecao("Prova ja foi publicada");

        if(!(p.getQuestoes().size() > 0)) throw new Excecao("Prova nao pode ser publicada sem questoes. Adicione algumas antes de prosseguir");

        p.setIs_published(true);
        p.setUpdated_at(Utils.getInstancia().getDataAtual());

        p = this.service.update(p);

        ProvaDTO provaDTO = ProvaService.convertEntityToDto(p);

        return ResponseEntity.ok().body(provaDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProvaDTO> edit(
            @PathVariable("id") Integer id,
            @RequestBody ProvaDTO dto
    ) throws Exception {
        Prova p = this.service.findById(id);

        if(p.getIs_published()) throw new Excecao("Prova ja foi publicada");

        p.setTitle(dto.getTitle());
        p.setDescription(dto.getDescription());
        p.setUpdated_at(Utils.getInstancia().getDataAtual());

        p = this.service.update(p);

        ProvaDTO provaDTO = ProvaService.convertEntityToDto(p);

        return ResponseEntity.ok().body(provaDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProvaDTO> delete(@PathVariable("id") Integer id) throws Exception {
        Prova prova = this.service.findById(id);

        if(!prova.getIs_activated()) throw new Excecao("Prova com id " + id + " ja foi excluida");

        prova.setDeleted_at(Utils.getInstancia().getDataAtual());
        prova.setIs_activated(false);
        prova.setTitle(prova.getTitle() + " [DESATIVADA]");

        prova = this.service.update(prova);

        /*
             TODO: Criar ImagemService para tratamento de arquivos
             ImagemService.excluirImagensAssociadas(prova);
         */

        if(!prova.getIs_published()) {
            this.service.delete(prova);
            return ResponseEntity.noContent().build();
        }

        ProvaDTO provaDTO = ProvaService.convertEntityToDto(prova);
        return ResponseEntity.accepted().body(provaDTO);
    }

    private void verifyErrors(BindingResult result) throws Excecao {
        List<String> erros;
        if(result.hasErrors()) {
            erros = new ArrayList<>();
            for(ObjectError error: result.getAllErrors()) {
                erros.add(error.getDefaultMessage());
            }
            throw new Excecao(erros);
        }
    }

}
