package br.avaliatri.services;

import br.avaliatri.dtos.util.QuestaoRespondidaDTO;
import br.avaliatri.models.Questao;
import br.avaliatri.models.QuestaoRespondida;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestaoRespondidaService {
    public static QuestaoRespondida convertDtoToEntity(QuestaoRespondidaDTO dto){
        QuestaoRespondida entity = new QuestaoRespondida();
        Questao q = new Questao();
        q.setId(dto.getQuestao());
        entity.setQuestao(q);
        entity.setIs_correta(dto.getIs_correta());

        return entity;
    }

    public static QuestaoRespondidaDTO convertEntityToDto(QuestaoRespondida entity){
        QuestaoRespondidaDTO dto = new QuestaoRespondidaDTO();

        return dto;
    }

    public static List<QuestaoRespondidaDTO> convertEntityListToDtoList(List<QuestaoRespondida> entities) {
        List<QuestaoRespondidaDTO> dtos = new ArrayList<>();
        for(QuestaoRespondida p: entities) {
            dtos.add(convertEntityToDto(p));
        }

        return dtos;
    }

    public static List<QuestaoRespondida> convertDtoToEntity(List<QuestaoRespondidaDTO> dtos){
        List<QuestaoRespondida> entities = new ArrayList<>();
        for(QuestaoRespondidaDTO dto: dtos) {
            entities.add(convertDtoToEntity(dto));
        }

        return entities;
    }


}
