package br.avaliatri.controllers;

import br.avaliatri.dtos.ProvaDTO;
import br.avaliatri.dtos.ProvaRespondidaDTO;
import br.avaliatri.dtos.QuestaoDTO;
import br.avaliatri.dtos.util.QuestaoRespondidaDTO;
import br.avaliatri.excecoes.Excecao;
import br.avaliatri.models.*;
import br.avaliatri.repositories.AlternativaRepository;
import br.avaliatri.repositories.ProvaRepository;
import br.avaliatri.repositories.QuestaoRepository;
import br.avaliatri.repositories.QuestaoRespondidaRepository;
import br.avaliatri.services.*;
import br.avaliatri.utils.Utils;
import lombok.Synchronized;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "provas")
@CrossOrigin
public class ProvaController {
    private ProvaService service;
    private ProvaRepository provaRepository;
    private AlternativaRepository alternativaRepository;
    private QuestaoRepository questaoRepository;
    private QuestaoRespondidaRepository questaoRespondidaRepository;
    private UsuarioService usuarioService;
    private ProvaRespondidaService provaRespondidaService;

    public ProvaController(ProvaService service, ProvaRepository provaRepository, AlternativaRepository alternativaRepository, QuestaoRepository questaoRepository, QuestaoRespondidaRepository questaoRespondidaRepository, UsuarioService usuarioService, ProvaRespondidaService provaRespondidaService) {
        this.service = service;
        this.provaRepository = provaRepository;
        this.alternativaRepository = alternativaRepository;
        this.questaoRepository = questaoRepository;
        this.questaoRespondidaRepository = questaoRespondidaRepository;
        this.usuarioService = usuarioService;
        this.provaRespondidaService = provaRespondidaService;
    }

    @GetMapping("")
    public ResponseEntity<Page<ProvaDTO>> getAll(
            @RequestParam(value="page", defaultValue ="0") Integer page,
            @RequestParam(value="linesPerPage", defaultValue ="4")Integer linesPerPage,
            @RequestParam(value="orderBy", defaultValue ="id")String orderBy,
            @RequestParam(value="direction", defaultValue = "DESC")String direction
    ) {
        Page<ProvaDTO> provas = this.service.findAll(page, linesPerPage, orderBy, direction);

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
        q.setTemImagem(false);
        p = this.service.addQuestao(p, q);

        return ResponseEntity.status(HttpStatus.CREATED).body(ProvaService.convertEntityToDto(p));
    }

    @Synchronized
    @PostMapping("/{id}/responder")
    public ResponseEntity<ProvaRespondidaDTO> responderQuestao(
            @PathVariable("id") Integer id,
            @Valid @RequestBody ProvaRespondidaDTO dto,
            BindingResult result
    ) throws Exception {
        verifyErrors(result);
        Prova p;
        Usuario u;

        this.provaRespondidaService.verificarSeExisteResultado(id, dto.getUsuario());

        List<QuestaoRespondidaDTO> questaoRespondidaDTOS;
        List<Questao> questoes;
        List<QuestaoRespondida> resultado = new ArrayList<>();

        Map<Integer, Boolean> respostas = new HashMap<Integer, Boolean>();

        Map<Integer, String> respostasUsuario = new HashMap<Integer, String>();

        p = service.findById(id);
        u = usuarioService.findById(dto.getUsuario());

        questoes = p.getQuestoes();
        int quantidadeQuestoes = questoes.size();
        int quantidadeAcertos = 0;

        questaoRespondidaDTOS = dto.getQuestoes_respondidas();

        if(questoes.size() != questaoRespondidaDTOS.size()) {
            throw new Excecao("Responda todas as questoes");
        }

        for(Questao questao_salva: questoes) {
            for(QuestaoRespondidaDTO resposta: questaoRespondidaDTOS) {
                if(resposta.getQuestao().equals(questao_salva.getId())) {
                    if(questao_salva.getAlternativa_correta().equals(resposta.getAlternativa_usuario())) {
                        quantidadeAcertos++;
                        respostas.put(questao_salva.getId(), true);
                        respostasUsuario.put(questao_salva.getId(), resposta.getAlternativa_usuario());
                        resultado.add(new QuestaoRespondida(null, questao_salva, resposta.getAlternativa_usuario(),  true));
                    } else {
                        respostas.put(questao_salva.getId(), false);
                        respostasUsuario.put(questao_salva.getId(), resposta.getAlternativa_usuario());
                        resultado.add(new QuestaoRespondida(null, questao_salva, resposta.getAlternativa_usuario(), false));
                    }
                }
            }
        }

        ProvaRespondida provaRespondida = new ProvaRespondida();

        provaRespondida.setQtd_questoes(quantidadeQuestoes);
        provaRespondida.setNota(quantidadeAcertos * 100/quantidadeQuestoes);
        provaRespondida.setRespondida_em(Utils.getInstancia().getDataAtual());
        provaRespondida.setProva(p);
        provaRespondida.setUsuario(u);

        provaRespondida.setQuestoes(this.questaoRespondidaRepository.saveAll(resultado));
        provaRespondida = this.provaRespondidaService.save(provaRespondida);
        ProvaRespondidaDTO respondidaDTO = ProvaRespondidaService.convertEntityToDto(provaRespondida);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(respondidaDTO);
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
