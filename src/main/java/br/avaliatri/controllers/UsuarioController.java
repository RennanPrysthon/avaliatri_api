package br.avaliatri.controllers;

import br.avaliatri.dtos.ProvaDTO;
import br.avaliatri.dtos.ProvaRespondidaDTO;
import br.avaliatri.dtos.ResultadoDTO;
import br.avaliatri.dtos.UsuarioDTO;
import br.avaliatri.dtos.util.QuestaoRespondidaDTO;
import br.avaliatri.enums.Perfil;
import br.avaliatri.excecoes.Excecao;
import br.avaliatri.models.Usuario;
import br.avaliatri.services.ProvaRespondidaService;
import br.avaliatri.services.ProvaService;
import br.avaliatri.services.UsuarioService;
import br.avaliatri.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "usuarios")
@CrossOrigin
public class UsuarioController {
    private UsuarioService service;
    private ProvaService provaService;
    private ProvaRespondidaService provaRespondidaService;
    private BCryptPasswordEncoder pe;

    public UsuarioController(UsuarioService service, ProvaService provaService, ProvaRespondidaService provaRespondidaService, BCryptPasswordEncoder pe) {
        this.service = service;
        this.provaService = provaService;
        this.provaRespondidaService = provaRespondidaService;
        this.pe = pe;
    }

    @GetMapping("/alunos")
    public ResponseEntity<Page<UsuarioDTO>> getAllAlunos(
        @RequestParam(value="page", defaultValue ="0") Integer page,
        @RequestParam(value="linesPerPage", defaultValue ="5")Integer linesPerPage,
        @RequestParam(value="orderBy", defaultValue ="id")String orderBy,
        @RequestParam(value="direction", defaultValue = "DESC")String direction
    ) {
        Page<UsuarioDTO> usuarios = this.service.findAllAlunos(page, linesPerPage, orderBy, direction);

        return ResponseEntity.ok().body(usuarios);
    }

    @GetMapping("/professores")
    public ResponseEntity<Page<UsuarioDTO>> getAllProfessores(
        @RequestParam(value="page", defaultValue ="0") Integer page,
        @RequestParam(value="linesPerPage", defaultValue ="5")Integer linesPerPage,
        @RequestParam(value="orderBy", defaultValue ="id")String orderBy,
        @RequestParam(value="direction", defaultValue = "DESC")String direction
    ) {
        Page<UsuarioDTO> usuarios = this.service.findAllProfessores(page, linesPerPage, orderBy, direction);

        return ResponseEntity.ok().body(usuarios);
    }

    @GetMapping("/professores/{id}/resultados")
    public ResponseEntity<Page<ProvaRespondidaDTO>> getAllResultadosByProfessores(
            @PathVariable("id") Integer id,
            @RequestParam(value="page", defaultValue ="0") Integer page,
            @RequestParam(value="linesPerPage", defaultValue ="5")Integer linesPerPage,
            @RequestParam(value="orderBy", defaultValue ="id")String orderBy,
            @RequestParam(value="direction", defaultValue = "DESC")String direction
    ) throws Excecao {
        Page<ProvaRespondidaDTO> respondidaDTOS = this.service.findAllResultadosByProfessores(id, page, linesPerPage, orderBy, direction);

        return ResponseEntity.ok().body(respondidaDTOS);
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
        ProvaRespondidaDTO resultadoDTO = provaRespondidaService.getResultadoById(id_resultado);
        List<QuestaoRespondidaDTO> dtos = resultadoDTO.getQuestoes_respondidas();

        return ResponseEntity.ok().body(resultadoDTO);
    }

    @PostMapping("")
    public ResponseEntity<UsuarioDTO> saveAluno(@Valid @RequestBody UsuarioDTO dto) {
        Usuario usuario = UsuarioService.convertDtoToEntity(dto);
        usuario.setPerfil(Perfil.ALUNO);
        usuario = this.service.save(usuario);

        dto = UsuarioService.convertEntityToDto(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/professor")
    public ResponseEntity<UsuarioDTO> saveProfessor(@Valid @RequestBody UsuarioDTO dto) {
        Usuario usuario = UsuarioService.convertDtoToEntity(dto);
        usuario.setPerfil(Perfil.PROFESSOR);
        usuario = this.service.save(usuario);

        dto = UsuarioService.convertEntityToDto(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUser(@PathVariable("id") Integer id, @RequestBody UsuarioDTO dto) throws Excecao {
        Usuario usuario = this.service.findById(id);

        usuario.setUpdated_at(Utils.getInstancia().getDataAtual());
        usuario.setName(dto.getName());

        usuario = this.service.update(usuario);
        dto = UsuarioService.convertEntityToDto(usuario);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<UsuarioDTO> updatePassword(@PathVariable("id") Integer id, @RequestBody UsuarioDTO dto) throws Excecao {
        Usuario usuario = this.service.findById(id);

        usuario.setPassword(this.pe.encode(dto.getPassword()));
        usuario.setUpdated_at(Utils.getInstancia().getDataAtual());

        usuario = this.service.update(usuario);
        dto = UsuarioService.convertEntityToDto(usuario);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Integer id) throws Excecao {
        Usuario usuario = this.service.findById(id);

        usuario.setIs_active(false);
        usuario.setDeleted_at(Utils.getInstancia().getDataAtual());

        this.service.delete(usuario);
        return ResponseEntity.noContent().build();
    }
}
