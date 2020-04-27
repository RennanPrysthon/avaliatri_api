package br.avaliatri.services;

import br.avaliatri.dtos.AlternativaDTO;
import br.avaliatri.dtos.QuestaoDTO;
import br.avaliatri.excecoes.Excecao;
import br.avaliatri.models.Alternativa;
import br.avaliatri.models.Prova;
import br.avaliatri.models.Questao;
import br.avaliatri.repositories.QuestaoRepository;
import br.avaliatri.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestaoService {

    private QuestaoRepository repository;
    private UsuarioRepository usuarioRepository;

    public QuestaoService(QuestaoRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    public Questao save(Questao e) {
        return this.repository.save(e);
    }

    public static Questao convertDtoToEntity(QuestaoDTO dto) {
        Questao entity = new Questao();
        List<Alternativa> alternativas = new ArrayList<>();
        entity.setProva(new Prova());
        entity.getProva().setId(dto.getProva());
        entity.setAlternativa_correta(dto.getAlternativa_correta());
        entity.setEnunciado(dto.getEnunciado());
        Alternativa a;

        a = new Alternativa(dto.getAlternativaA().getAlternativa(), dto.getAlternativaA().getTexto());
        alternativas.add(a);
        a = new Alternativa(dto.getAlternativaB().getAlternativa(), dto.getAlternativaB().getTexto());
        alternativas.add(a);
        a = new Alternativa(dto.getAlternativaC().getAlternativa(), dto.getAlternativaC().getTexto());
        alternativas.add(a);
        a = new Alternativa(dto.getAlternativaD().getAlternativa(), dto.getAlternativaD().getTexto());
        alternativas.add(a);
        a = new Alternativa(dto.getAlternativaE().getAlternativa(), dto.getAlternativaE().getTexto());
        alternativas.add(a);

        entity.setAlternativas(alternativas);
        return entity;
    }

    public static QuestaoDTO convertEntityToDto(Questao entity) {
        QuestaoDTO dto = new QuestaoDTO();
        dto.setAlternativa_correta(entity.getAlternativa_correta());
        dto.setEnunciado(entity.getEnunciado());
        dto.setId(entity.getId());
        dto.setProva(entity.getProva().getId());
        AlternativaDTO alternativaValor;
        for(Alternativa a: entity.getAlternativas()) {
            if(a.getOpcao().equalsIgnoreCase("A")){
                alternativaValor = AlternativaService.convertEntityToDto(a);
                dto.setAlternativaA(alternativaValor);
            }
            if(a.getOpcao().equalsIgnoreCase("B")){
                alternativaValor = AlternativaService.convertEntityToDto(a);
                dto.setAlternativaB(alternativaValor);
            }
            if(a.getOpcao().equalsIgnoreCase("C")){
                alternativaValor = AlternativaService.convertEntityToDto(a);
                dto.setAlternativaC(alternativaValor);
            }
            if(a.getOpcao().equalsIgnoreCase("D")){
                alternativaValor = AlternativaService.convertEntityToDto(a);
                dto.setAlternativaD(alternativaValor);
            }
            if(a.getOpcao().equalsIgnoreCase("E")){
                alternativaValor = AlternativaService.convertEntityToDto(a);
                dto.setAlternativaE(alternativaValor);
            }
        }
        return dto;
    }

    public List<Questao> saveAll(List<Questao> questoes) {
        return this.repository.saveAll(questoes);
    }

    public static List<QuestaoDTO> convertEntityListToDtoList(List<Questao> entities) {
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
            alternativas.add(AlternativaService.convertDtoToEntity(dto.getAlternativaA()));
            alternativas.add(AlternativaService.convertDtoToEntity(dto.getAlternativaB()));
            alternativas.add(AlternativaService.convertDtoToEntity(dto.getAlternativaC()));
            alternativas.add(AlternativaService.convertDtoToEntity(dto.getAlternativaD()));
            alternativas.add(AlternativaService.convertDtoToEntity(dto.getAlternativaE()));
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
        this.repository.delete(questao);
    }
}
