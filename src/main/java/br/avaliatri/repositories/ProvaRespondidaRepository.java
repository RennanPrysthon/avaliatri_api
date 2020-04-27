package br.avaliatri.repositories;

import br.avaliatri.models.ProvaRespondida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvaRespondidaRepository extends JpaRepository<ProvaRespondida, Integer> {
}
