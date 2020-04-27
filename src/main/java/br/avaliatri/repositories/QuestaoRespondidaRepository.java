package br.avaliatri.repositories;

import br.avaliatri.models.QuestaoRespondida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestaoRespondidaRepository extends JpaRepository<QuestaoRespondida, Integer> {
}
