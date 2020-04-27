package br.avaliatri.services;

import br.avaliatri.dtos.ProvaRespondidaDTO;
import br.avaliatri.dtos.util.QuestaoRespondidaDTO;
import br.avaliatri.models.ProvaRespondida;
import br.avaliatri.models.QuestaoRespondida;
import br.avaliatri.repositories.ProvaRespondidaRepository;
import br.avaliatri.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProvaRespondidaService {

    private ProvaRespondidaRepository repository;

    public ProvaRespondidaService(ProvaRespondidaRepository repository) {
        this.repository = repository;
    }

    public static ProvaRespondidaDTO convertEntityToDto(ProvaRespondida provaRespondida) {
        ProvaRespondidaDTO dto = new ProvaRespondidaDTO();
        dto.setId(provaRespondida.getId());
        dto.setNota(provaRespondida.getNota());
        dto.setObservacao(provaRespondida.getObservacao());
        dto.setUsuario(provaRespondida.getUsuario().getId());
        dto.setRespondida_em(Utils.getInstancia().getDataFormatada(provaRespondida.getRespondida_em()));
        dto.setQuantidade_questoes(provaRespondida.getQtd_questoes());
        dto.setQuestoes_respondidas(provaRespondida
                .getQuestoes()
                .stream()
                .map(q-> {
                    QuestaoRespondidaDTO qn = new QuestaoRespondidaDTO();
                    qn.setAlternativa_usuario(q.getAlternativa_usuario());
                    qn.setIs_correta(q.getIs_correta());
                    qn.setQuestao(q.getQuestao().getId());
                    return qn;
                }).collect(Collectors.toList()));
        dto.setProva(ProvaService.convertEntityToDto(provaRespondida.getProva()));
        return dto;
    }

    public ProvaRespondida save(ProvaRespondida provaRespondida) {
        return this.repository.save(provaRespondida);
    }
}
