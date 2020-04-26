package br.avaliatri.repositories;

import br.avaliatri.models.Questao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestaoRepository extends JpaRepository<Questao, Integer> {
}
