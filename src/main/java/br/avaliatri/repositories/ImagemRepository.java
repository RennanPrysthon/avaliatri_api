package br.avaliatri.repositories;

import br.avaliatri.models.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagemRepository extends JpaRepository<Imagem, Integer> {
}
