package br.avaliatri.repositories;
import br.avaliatri.models.Prova;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvaRepository extends JpaRepository<Prova, Integer> {
}
