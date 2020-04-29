package br.avaliatri.services;

import br.avaliatri.dtos.UsuarioDTO;
import br.avaliatri.excecoes.Excecao;
import br.avaliatri.models.Usuario;
import br.avaliatri.enums.Perfil;
import br.avaliatri.repositories.UsuarioRepository;
import br.avaliatri.utils.Utils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private BCryptPasswordEncoder pe;
    private UsuarioRepository repository;

    public UsuarioService(BCryptPasswordEncoder pe, UsuarioRepository repository) {
        this.pe = pe;
        this.repository = repository;
    }

    public Usuario save(Usuario e) {
        e.setCreated_at(Utils.getInstancia().getDataAtual());
        return this.repository.save(e);
    }

    public static Usuario convertDtoToEntity(UsuarioDTO dto){
        Usuario entity = new Usuario();
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.addPerfil(Perfil.toEnum(dto.getTipo_usuario()));
        return entity;
    }

    public static UsuarioDTO convertEntityToDto(Usuario entity){
        UsuarioDTO dto = new UsuarioDTO();
        dto.setCreated_at(Utils.getInstancia().getDataFormatada(entity.getCreated_at()));
        dto.setDeleted_at(Utils.getInstancia().getDataFormatada(entity.getDeleted_at()));
        dto.setUpdated_at(Utils.getInstancia().getDataFormatada(entity.getUpdated_at()));
        dto.setIs_active(entity.getIs_active());
        dto.setEmail(entity.getEmail());
        dto.setName(entity.getName());
        dto.setId(entity.getId());
        dto.setQtd_provas_criadas(entity.getProvas_criadas().size() == 0?null:entity.getProvas_criadas().size());
        dto.setPerfil(entity.getPerfis().stream().map(p -> Perfil.toEnum(p).getRole()).collect(Collectors.toList()));
        return dto;
    }

    public static List<UsuarioDTO> convertEntityListToDtoList(List<Usuario> entities) {
        List<UsuarioDTO> dtos = new ArrayList<>();
        UsuarioDTO dto;
        for(Usuario u: entities) {
            dto = convertEntityToDto(u);
            dtos.add(dto);
        }
        return dtos;
    }

    public static List<Usuario> convertDtoToEntity(List<UsuarioDTO> dtos){
        List<Usuario> entities = new ArrayList<>();
        Usuario u;
        for(UsuarioDTO dto: dtos) {
            u = convertDtoToEntity(dto);
            entities.add(u);
        }
        return entities;
    }

    public List<Usuario> findAll() {
        return this.repository.findAll();
    }

    public Usuario findById(Integer id) throws Excecao {
        return this.repository.findById(id)
            .orElseThrow(() -> new Excecao("Usuario com id " + id + " nao foi encontrado"));
    }
}
