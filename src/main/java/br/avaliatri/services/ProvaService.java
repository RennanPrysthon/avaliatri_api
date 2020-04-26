package br.avaliatri.services;

import br.avaliatri.dtos.ProvaDTO;
import br.avaliatri.excecoes.Excecao;
import br.avaliatri.models.Alternativa;
import br.avaliatri.models.Prova;
import br.avaliatri.models.Questao;
import br.avaliatri.models.Usuario;
import br.avaliatri.repositories.AlternativaRepository;
import br.avaliatri.repositories.ProvaRepository;
import br.avaliatri.repositories.QuestaoRepository;
import br.avaliatri.repositories.UsuarioRepository;
import br.avaliatri.utils.Utils;
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

    public ProvaService(ProvaRepository repository, UsuarioRepository usuarioRepository, QuestaoRepository questaoRepository, AlternativaRepository alternativaRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.questaoRepository = questaoRepository;
        this.alternativaRepository = alternativaRepository;
    }

    @Transactional
    public Prova save(Prova p) throws Exception {
        Usuario u = this.usuarioRepository.findById(p.getUsuario().getId())
                .orElseThrow(()->new Excecao(Collections.singletonList("Usuario nao encontrado")));

        List<Alternativa> alternativas;
        List<Questao> questaos = p.getQuestoes();
        p.setQuestoes(new ArrayList<>());
        p = this.repository.saveAndFlush(p);
        for(Questao q: questaos){
            q.setProva(p);

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

    public List<ProvaDTO> findAll() {
        List<Prova> provas = this.repository.findAll();
        return convertEntityListToDtoList(provas);
    }

    public Prova findById(Integer id) throws Exception {
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
        q.setProva(p);

        alternativas = q.getAlternativas();

        for(Alternativa a: alternativas) {
            a.setQuestao(q);
        }
        q.setCreated_at(Utils.getInstancia().getDataAtual());
        q.setAlternativas(alternativas);
        q = this.questaoRepository.save(q);
        p.getQuestoes().add(q);
        p.setUpdated_at(Utils.getInstancia().getDataAtual());
        p = this.repository.save(p);
        this.alternativaRepository.saveAll(q.getAlternativas());
        return p;
    }

    public List<Prova> findAllForUser(Usuario entitiy) {
        return this.repository.findAll();
    }
}
