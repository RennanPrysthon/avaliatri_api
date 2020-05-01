package br.avaliatri.repositories;

import br.avaliatri.models.Prova;
import br.avaliatri.models.ProvaRespondida;
import br.avaliatri.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProvaRespondidaRepository extends JpaRepository<ProvaRespondida, Integer> {
    Optional<ProvaRespondida> findByUsuarioAndProva(Usuario usuario, Prova prova);

    List<ProvaRespondida> findAllByUsuario(Usuario entitiy);

    Collection<? extends ProvaRespondida> findALlByProva(Prova p);
}
