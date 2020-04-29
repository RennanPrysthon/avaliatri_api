package br.avaliatri.services;

import br.avaliatri.dtos.AlternativaDTO;
import br.avaliatri.dtos.ProvaDTO;
import br.avaliatri.models.Alternativa;
import br.avaliatri.repositories.AlternativaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlternativaService {

    private AlternativaRepository repository;

    public AlternativaService(AlternativaRepository repository) {
        this.repository = repository;
    }

    public Alternativa save(Alternativa e) {
        return this.repository.save(e);
    }

    public List<Alternativa> updateAll(List<Alternativa> alternativas) {
        return this.repository.saveAll(alternativas);
    }

    public static Alternativa convertDtoToEntity(AlternativaDTO dto){
        Alternativa entity = new Alternativa();
        entity.setOpcao(dto.getAlternativa());
        entity.setTexto(dto.getTexto());
        return entity;
    }

    public static AlternativaDTO convertEntityToDto(Alternativa entity){
        AlternativaDTO dto = new AlternativaDTO();
        dto.setTexto(entity.getTexto());
        dto.setAlternativa(entity.getOpcao());
        dto.setId(entity.getId());
        dto.setQuestao(entity.getQuestao().getId());
        if(entity.getTemImagem()) {
            dto.setImagem(entity.getImagem().getCaminhoArquivo());
        }
        dto.setTemImagem(entity.getTemImagem());
        return dto;
    }

    public static List<AlternativaDTO> convertEntityListToDtoList(List<Alternativa> entities) {
        List<AlternativaDTO> dtos = new ArrayList<>();
        for(Alternativa entity: entities) {
            dtos.add(convertEntityToDto(entity));
        }
        return dtos;
    }

    public static List<Alternativa> convertDtoListToEntityList(List<AlternativaDTO> dtos){
        List<Alternativa> entities = new ArrayList<>();
        for(AlternativaDTO dto: dtos) {
            entities.add(convertDtoToEntity(dto));
        }
        return entities;
    }

    public List<Alternativa> saveAll(List<Alternativa> entities) {
        for(Alternativa e: entities) {
            e = this.save(e);
        }
        return entities;
    }
}
