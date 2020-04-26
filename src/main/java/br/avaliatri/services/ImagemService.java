package br.avaliatri.services;

import br.avaliatri.dtos.AlternativaDTO;
import br.avaliatri.dtos.ImagemDTO;
import br.avaliatri.dtos.ProvaDTO;
import br.avaliatri.models.Alternativa;
import br.avaliatri.models.Imagem;
import br.avaliatri.repositories.AlternativaRepository;
import br.avaliatri.repositories.ImagemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImagemService {

    private ImagemRepository repository;

    public ImagemService(ImagemRepository repository) {
        this.repository = repository;
    }

    public Imagem save(Imagem e) {
        return this.repository.save(e);
    }

    public static Imagem convertDtoToEntity(ImagemDTO dto){
        Imagem entity = new Imagem();

        return entity;
    }

    public static ImagemDTO convertEntityToDto(Imagem entity){
        ImagemDTO dto = new ImagemDTO();

        return dto;
    }

    public static List<ImagemDTO> convertEntityListToDtoList(List<Imagem> entities) {
        List<ImagemDTO> dtos = new ArrayList<>();
        return dtos;
    }

    public static List<Imagem> convertDtoToEntity(List<ImagemDTO> dtos){
        List<Imagem> entities = new ArrayList<>();
        return entities;
    }
}
