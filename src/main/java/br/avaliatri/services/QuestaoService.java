package br.avaliatri.services;

import br.avaliatri.dtos.QuestaoDTO;
import br.avaliatri.excecoes.Excecao;
import br.avaliatri.models.*;
import br.avaliatri.repositories.AlternativaRepository;
import br.avaliatri.repositories.ProvaRepository;
import br.avaliatri.repositories.QuestaoRepository;
import br.avaliatri.repositories.QuestaoRespondidaRepository;
import lombok.Synchronized;
import org.hibernate.annotations.Synchronize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class QuestaoService {

    private QuestaoRepository repository;
    private ProvaRepository provaRepository;
    private QuestaoRespondidaRepository questaoRespondidaRepository;
    private AlternativaRepository alternativaRepository;

    public QuestaoService(QuestaoRepository repository, ProvaRepository provaRepository, QuestaoRespondidaRepository questaoRespondidaRepository, AlternativaRepository alternativaRepository) {
        this.repository = repository;
        this.provaRepository = provaRepository;
        this.questaoRespondidaRepository = questaoRespondidaRepository;
        this.alternativaRepository = alternativaRepository;
    }

    @Transactional
    public Questao save(Questao e) {
        for(Alternativa a: e.getAlternativas()) {
            a.setQuestao(e);
        }
        this.alternativaRepository.saveAll(e.getAlternativas());
        return this.repository.save(e);
    }

    public Page<Questao> findAll(PageRequest pageRequest) {
        return this.repository.findAll(pageRequest);
    }

    public static Questao convertDtoToEntity(QuestaoDTO dto) {
        Questao entity = new Questao();
        List<Alternativa> alternativas = new ArrayList<>();
        entity.setAlternativa_correta(dto.getAlternativa_correta());
        entity.setEnunciado(dto.getEnunciado());
        entity.setTextoApoio(dto.getTextoApoio());
        Alternativa a;

        a = new Alternativa("A", dto.getAlternativaA());
        alternativas.add(a);
        a = new Alternativa("B", dto.getAlternativaB());
        alternativas.add(a);
        a = new Alternativa("C", dto.getAlternativaC());
        alternativas.add(a);
        a = new Alternativa("D", dto.getAlternativaD());
        alternativas.add(a);
        a = new Alternativa("E", dto.getAlternativaE());
        alternativas.add(a);

        entity.setAlternativas(alternativas);
        return entity;
    }

    public static QuestaoDTO convertEntityToDto(Questao entity) {
        QuestaoDTO dto = new QuestaoDTO();
        dto.setAlternativa_correta(entity.getAlternativa_correta());
        dto.setEnunciado(entity.getEnunciado());
        dto.setId(entity.getId());
        if(entity.getTemImagem()) {
            dto.setImagem(entity.getImagem().getCaminhoArquivo());
        }
        dto.setTextoApoio(entity.getTextoApoio());
        dto.setTemImagem(entity.getTemImagem());
        dto.setAlternativas(AlternativaService.convertEntityListToDtoList(entity.getAlternativas()));
        String alternativaValor;
        for(Alternativa a: entity.getAlternativas()) {
            if(a.getOpcao().equalsIgnoreCase("A")){
                alternativaValor = a.getTexto();
                dto.setAlternativaA(alternativaValor);
            }
            if(a.getOpcao().equalsIgnoreCase("B")){
                alternativaValor = a.getTexto();
                dto.setAlternativaB(alternativaValor);
            }
            if(a.getOpcao().equalsIgnoreCase("C")){
                alternativaValor = a.getTexto();
                dto.setAlternativaC(alternativaValor);
            }
            if(a.getOpcao().equalsIgnoreCase("D")){
                alternativaValor = a.getTexto();
                dto.setAlternativaD(alternativaValor);
            }
            if(a.getOpcao().equalsIgnoreCase("E")){
                alternativaValor = a.getTexto();
                dto.setAlternativaE(alternativaValor);
            }
        }
        return dto;
    }

    public List<Questao> saveAll(List<Questao> questoes) {
        return this.repository.saveAll(questoes);
    }

    public static List<QuestaoDTO> convertEntityListToDtoList(Set<Questao> entities) {
        List<QuestaoDTO> dtos = new ArrayList<>();
        for(Questao e: entities) {
            dtos.add(convertEntityToDto(e));
        }

        return dtos;
    }

    public static List<Questao> convertDtoListToEntityList(List<QuestaoDTO> dtos){
        List<Questao> entities = new ArrayList<>();
        List<Alternativa> alternativas;
        Questao questao;
        for(QuestaoDTO dto: dtos) {
            alternativas = new ArrayList<>();
            questao = convertDtoToEntity(dto);
            alternativas.add(new Alternativa("A", dto.getAlternativaA()));
            alternativas.add(new Alternativa("B", dto.getAlternativaB()));
            alternativas.add(new Alternativa("C", dto.getAlternativaC()));
            alternativas.add(new Alternativa("D", dto.getAlternativaD()));
            alternativas.add(new Alternativa("E", dto.getAlternativaE()));

            questao.setAlternativas(alternativas);
            entities.add(questao);
        }

        return entities;
    }

    public Questao findById(Integer id) throws Excecao {
        return this.repository.findById(id)
            .orElseThrow(()-> new Excecao("Questao com id " + " nao foi encontrada"));
    }

    public Questao update(Questao questao) {
        return this.repository.save(questao);
    }

    public void delete(Questao questao) {
        Set<Prova> provas = questao.getProvas();
        for(Prova prova: provas) {
            prova.removeQuestao(questao);
            this.delete(questao);
        }
        List<QuestaoRespondida> respondidas = questao.getQuestaoesRespondidas();
        for(QuestaoRespondida questaoRespondida: respondidas) {
            this.questaoRespondidaRepository.delete(questaoRespondida);
        }
        this.repository.delete(questao);
    }
}
