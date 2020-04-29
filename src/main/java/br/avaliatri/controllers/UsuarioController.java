package br.avaliatri.controllers;

import br.avaliatri.dtos.ProvaDTO;
import br.avaliatri.dtos.ProvaRespondidaDTO;
import br.avaliatri.dtos.ResultadoDTO;
import br.avaliatri.dtos.UsuarioDTO;
import br.avaliatri.excecoes.Excecao;
import br.avaliatri.models.Prova;
import br.avaliatri.models.Usuario;
import br.avaliatri.services.ProvaRespondidaService;
import br.avaliatri.services.ProvaService;
import br.avaliatri.services.UsuarioService;
import br.avaliatri.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "usuarios")
public class UsuarioController {
    private UsuarioService service;
    private ProvaService provaService;
    private ProvaRespondidaService provaRespondidaService;

    public UsuarioController(UsuarioService service, ProvaService provaService, ProvaRespondidaService provaRespondidaService) {
        this.service = service;
        this.provaService = provaService;
        this.provaRespondidaService = provaRespondidaService;
    }

    @GetMapping("")
    public ResponseEntity<List<UsuarioDTO>> getAllUsers() {
        List<Usuario> entities = this.service.findAll();
        List<UsuarioDTO> usuarios = UsuarioService.convertEntityListToDtoList(entities);

        return ResponseEntity.ok().body(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUserById(@PathVariable("id") Integer id) throws Excecao {
        Usuario entitiy = this.service.findById(id);
        UsuarioDTO usuario = UsuarioService.convertEntityToDto(entitiy);

        return ResponseEntity.ok().body(usuario);
    }

    @GetMapping("/{id}/provas")
    public ResponseEntity<Page<ProvaDTO>> getAllProvasUser(
            @PathVariable("id") Integer id,
            @RequestParam(value="page", defaultValue ="0") Integer page,
            @RequestParam(value="linesPerPage", defaultValue ="4")Integer linesPerPage,
            @RequestParam(value="orderBy", defaultValue ="id")String orderBy,
            @RequestParam(value="direction", defaultValue = "DESC")String direction
    )throws Excecao {
        Usuario entitiy = this.service.findById(id);

        Page<ProvaDTO> provas = this.provaService.findAllForUser(entitiy, page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok().body(provas);
    }

    @GetMapping("/{id}/resultados")
    public ResponseEntity<ResultadoDTO> getAllResultadosByUser(@PathVariable("id") Integer id)
    throws Excecao {
        Usuario entitiy = this.service.findById(id);
        ResultadoDTO resultadoDTO = provaRespondidaService.getAllResultados(entitiy);
        return ResponseEntity.ok().body(resultadoDTO);
    }

    @GetMapping("/{id}/resultados/{id_resultado}")
    public ResponseEntity<ProvaRespondidaDTO> getResultadoById(
            @PathVariable("id") Integer id,
            @PathVariable("id_resultado") Integer id_resultado
    ) throws Excecao {
        Usuario entitiy = this.service.findById(id);
        ProvaRespondidaDTO resultadoDTO = provaRespondidaService.getResultadoById(id_resultado);

        if(entitiy.getId() == resultadoDTO.getUsuario()) {
            return ResponseEntity.ok().body(resultadoDTO);
        }

        return ResponseEntity.ok().body(resultadoDTO);
    }


    @PostMapping("")
    public ResponseEntity<UsuarioDTO> saveUser(@Valid @RequestBody UsuarioDTO dto) {
        Usuario usuario = UsuarioService.convertDtoToEntity(dto);

        usuario = this.service.save(usuario);

        dto = UsuarioService.convertEntityToDto(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUser(@PathVariable("id") Integer id, @Valid @RequestBody UsuarioDTO dto) throws Excecao {
        Usuario usuario = this.service.findById(id);

        usuario.setPassword(dto.getPassword());
        usuario.setUpdated_at(Utils.getInstancia().getDataAtual());
        usuario.setName(dto.getName());

        usuario = this.service.save(usuario);
        dto = UsuarioService.convertEntityToDto(usuario);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UsuarioDTO> deleteUser(@PathVariable("id") Integer id, @Valid @RequestBody UsuarioDTO dto) throws Excecao {
        Usuario usuario = this.service.findById(id);

        usuario.setIs_active(false);
        usuario.setDeleted_at(Utils.getInstancia().getDataAtual());

        this.service.save(usuario);
        return ResponseEntity.noContent().build();
    }
}
