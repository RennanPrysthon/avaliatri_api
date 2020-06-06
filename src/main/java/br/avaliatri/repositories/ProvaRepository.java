package br.avaliatri.repositories;
import br.avaliatri.models.Prova;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProvaRepository extends JpaRepository<Prova, Integer> {

    @Query(value = "SELECT * FROM prova p WHERE p.is_published = true", nativeQuery = true)
    Page<Prova> findAllUser(PageRequest pageRequest);
}
