package br.avaliatri.repositories;

import br.avaliatri.models.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.perfil = 0")
    Page<Usuario> findAllAlunos(PageRequest pageRequest);
    @Query("SELECT u FROM Usuario u WHERE u.perfil = 1")
    Page<Usuario> findAllProfessores(PageRequest pageRequest);
}
