package br.avaliatri.services;

import br.avaliatri.dtos.ProvaRespondidaDTO;
import br.avaliatri.dtos.ResultadoDTO;
import br.avaliatri.dtos.util.QuestaoRespondidaDTO;
import br.avaliatri.excecoes.Excecao;
import br.avaliatri.models.*;
import br.avaliatri.repositories.ProvaRespondidaRepository;
import br.avaliatri.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProvaRespondidaService {

    private ProvaRespondidaRepository repository;
    private UsuarioService usuarioService;
    private ProvaService provaService;

    public ProvaRespondidaService(ProvaRespondidaRepository repository, UsuarioService usuarioService, ProvaService provaService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
        this.provaService = provaService;
    }

    public ProvaRespondida save(ProvaRespondida provaRespondida) {
        return this.repository.save(provaRespondida);
    }

    public ResultadoDTO getAllResultados(Usuario entitiy) {
        List<ProvaRespondida> provaRespondidas = this.repository.findAllByUsuario(entitiy);
        List<ProvaRespondidaDTO> dtos = ProvaRespondidaService.convertEntityListToDtoList(provaRespondidas);

        ResultadoDTO resultadoDTO = new ResultadoDTO();
        resultadoDTO.setQuantidade_provas_respondidas(provaRespondidas.size());
        resultadoDTO.setProvas_respondidas(dtos);
        resultadoDTO.setUser_name(entitiy.getName());
        resultadoDTO.setUser_email(entitiy.getEmail());
        return resultadoDTO;
    }

    public ProvaRespondidaDTO getResultadoById(Integer id_resultado) throws Excecao {
        ProvaRespondida provaRespondida = this.repository.findById(id_resultado)
                .orElseThrow(() -> new Excecao("Resultado nao encontrado"));
        return convertEntityToDto(provaRespondida);
    }

    public static List<ProvaRespondidaDTO> convertEntityListToDtoList(List<ProvaRespondida> entities) {
        List<ProvaRespondidaDTO> dtos = new ArrayList<>();
        ProvaRespondidaDTO dto;
        for(ProvaRespondida entity: entities) {
            dto = ProvaRespondidaService.convertEntityToDto(entity);
            dto.setQuestoes_respondidas(null);
            dto.setTitulo(entity.getProva().getTitle());
            dtos.add(dto);
        }
        return dtos;
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
                    qn.setEnunciado(q.getQuestao().getEnunciado());
                    qn.setAlternativa_usuario(q.getAlternativa_usuario());
                    qn.setIs_correta(q.getIs_correta());
                    qn.setQuestao(q.getQuestao().getId());
                    qn.setAlternativas(AlternativaService.convertEntityListToDtoList(q.getQuestao().getAlternativas()));
                    return qn;
                }).collect(Collectors.toList()));
        return dto;
    }

    public void verificarSeExisteResultado(Integer prova_id, Integer user_id) throws Excecao {
        Usuario usuario = this.usuarioService.findById(user_id);
        Prova prova = this.provaService.findById(prova_id);
        Optional<ProvaRespondida> provaRespondida = this.repository.findByUsuarioAndProva(usuario, prova);

        if (provaRespondida.isPresent()){
            throw new Excecao("Prova ja foi respondida");
        }
    }
}
