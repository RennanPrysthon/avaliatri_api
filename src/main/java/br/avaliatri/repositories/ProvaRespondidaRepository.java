package br.avaliatri.repositories;

import br.avaliatri.models.Prova;
import br.avaliatri.models.ProvaRespondida;
import br.avaliatri.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvaRespondidaRepository extends JpaRepository<ProvaRespondida, Integer> {
    Optional<ProvaRespondida> findByUsuarioAndProva(Usuario usuario, Prova prova);
}
