package br.avaliatri.services;

import br.avaliatri.dtos.ProvaDTO;
import br.avaliatri.enums.StatusProva;
import br.avaliatri.excecoes.Excecao;
import br.avaliatri.models.*;
import br.avaliatri.repositories.*;
import br.avaliatri.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProvaService {

    private ProvaRepository repository;
    private UsuarioRepository usuarioRepository;
    private QuestaoRepository questaoRepository;
    private AlternativaRepository alternativaRepository;
    private ProvaRespondidaRepository respondidaRepository;

    public ProvaService(ProvaRepository repository, UsuarioRepository usuarioRepository, QuestaoRepository questaoRepository, AlternativaRepository alternativaRepository, ProvaRespondidaRepository respondidaRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.questaoRepository = questaoRepository;
        this.alternativaRepository = alternativaRepository;
        this.respondidaRepository = respondidaRepository;
    }

    @Transactional
    public Prova save(Prova p) throws Exception {
        Usuario u = this.usuarioRepository.findById(p.getUsuario().getId())
                .orElseThrow(()->new Excecao(Collections.singletonList("Usuario nao encontrado")));

        List<Alternativa> alternativas;
        Set<Questao> questaos = p.getQuestoes();
        p.setQuestoes(new HashSet<>());
        p = this.repository.saveAndFlush(p);
        for(Questao q: questaos){
            q.getProvas().add(p);

            alternativas = q.getAlternativas();
            q.setAlternativas(new ArrayList<>());
            q.setCreated_at(Utils.getInstancia().getDataAtual());
            q = this.questaoRepository.saveAndFlush(q);

            for(Alternativa a: alternativas) {
                a.setQuestao(q);
                a = this.alternativaRepository.save(a);
                q.getAlternativas().add(a);
            }
            p.getQuestoes().add(q);

            p = this.repository.save(p);
        }

        p.setUsuario(u);
        p.setCreated_at(Utils.getInstancia().getDataAtual());
        if(u.getProvas_criadas() != null) {
            u.getProvas_criadas().add(p);
        } else {
            u.setProvas_criadas(Collections.singletonList(p));
        }
        this.usuarioRepository.save(u);
        return this.repository.save(p);
    }

    public Page<ProvaDTO> findAll(Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        return this.repository.findAll(pageRequest).map(prova -> {
            ProvaDTO provaDTO = new ProvaDTO();
            provaDTO.setId(prova.getId());
            provaDTO.setTitle(prova.getTitle());
            provaDTO.setCreated_at(Utils.getInstancia().getDataFormatada(prova.getCreated_at()));
            provaDTO.setIs_activated(prova.getIs_activated());
            provaDTO.setIs_published(prova.getIs_published());
            provaDTO.setCriado_por(prova.getUsuario().getName());
            provaDTO.setQtd_questoes(prova.getQuestoes().size());
            return provaDTO;
        });
    }

    public Prova findById(Integer id) throws Excecao {
        return this.repository.findById(id)
                .orElseThrow(()-> new Excecao("Prova com id " + id + " nao foi encontrada", HttpStatus.NOT_FOUND));
    }

    public static Prova convertDtoToEntity(ProvaDTO dto){
        Prova prova = new Prova();
        Usuario usuario = new Usuario();

        usuario.setId(dto.getUsuario());
        prova.setTitle(dto.getTitle());
        prova.setDescription(dto.getDescription());
        prova.setUsuario(usuario);
        return prova;
    }

    public static ProvaDTO convertEntityToDto(Prova entity){
        ProvaDTO dto = new ProvaDTO();
        dto.setId(entity.getId());
        dto.setQuestoes(QuestaoService.convertEntityListToDtoList(entity.getQuestoes()));
        dto.setQtd_questoes(dto.getQuestoes().size());
        dto.setCreated_at(Utils.getInstancia().getDataFormatada(entity.getCreated_at()));
        dto.setDeleted_at(Utils.getInstancia().getDataFormatada(entity.getDeleted_at()));
        dto.setUpdated_at(Utils.getInstancia().getDataFormatada(entity.getUpdated_at()));
        dto.setUsuario(entity.getUsuario().getId());
        dto.setCriado_por(entity.getUsuario().getName());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setIs_activated(entity.getIs_activated());
        dto.setIs_published(entity.getIs_published());
        return dto;
    }

    public static List<ProvaDTO> convertEntityListToDtoList(List<Prova> entities) {
        List<ProvaDTO> provaDTOS = new ArrayList<>();
        ProvaDTO provaDTO;
        for(Prova p: entities) {
            provaDTO = convertEntityToDto(p);
            provaDTO.setQtd_questoes(provaDTO.getQuestoes().size());
            provaDTO.setQuestoes(null);
            provaDTOS.add(provaDTO);
        }

        return provaDTOS;
    }

    public static List<Prova> convertDtoToEntity(List<ProvaDTO> dtos){
        List<Prova> entities = new ArrayList<>();
        for(ProvaDTO provaDTO: dtos) {
            entities.add(convertDtoToEntity(provaDTO));
        }

        return entities;
    }

    public Prova update(Prova p) {
        return this.repository.save(p);
    }

    public void delete(Prova prova) {
        this.repository.delete(prova);
    }

    @Transactional
    public Prova addQuestao(Prova p, Questao q) {
        List<Alternativa> alternativas;
        alternativas = q.getAlternativas();
        for(Alternativa a: alternativas) {
            a.setQuestao(q);
        }
        q.setCreated_at(Utils.getInstancia().getDataAtual());
        q.setAlternativas(alternativas);
        q = this.questaoRepository.save(q);
        p.addQuestao(q);
        p.setUpdated_at(Utils.getInstancia().getDataAtual());
        p = this.repository.save(p);
        this.alternativaRepository.saveAll(q.getAlternativas());
        return p;
    }

    public Page<ProvaDTO> findAllForUser(Usuario entitiy, Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        return this.repository.findAllUser(pageRequest).map(p -> {
            Optional<ProvaRespondida> provaRespondida = this.respondidaRepository.findByUsuarioAndProva(entitiy, p);
            ProvaDTO provaDTO = new ProvaDTO();
            provaDTO.setId(p.getId());
            provaDTO.setTitle(p.getTitle());
            provaDTO.setCreated_at(Utils.getInstancia().getDataFormatada(p.getCreated_at()));
            provaDTO.setDescription(p.getDescription());
            provaDTO.setQtd_questoes(p.getQuestoes().size());
            if(provaRespondida.isPresent()) {
                provaDTO.setPontuacao(provaRespondida.get().getNota());
                provaDTO.setResultado_id(provaRespondida.get().getId());
                provaDTO.setStatusProva(StatusProva.FEITA);
                provaDTO.setResultado_id(provaRespondida.get().getId());
            } else {
                provaDTO.setStatusProva(StatusProva.NAO_FEITA);
                provaDTO.setIs_activated(p.getIs_activated());
            }

            return provaDTO;
        });
    }
}
