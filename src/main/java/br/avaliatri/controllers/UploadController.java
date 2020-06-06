package br.avaliatri.controllers;

import br.avaliatri.dtos.AlternativaDTO;
import br.avaliatri.dtos.QuestaoDTO;
import br.avaliatri.excecoes.Excecao;
import br.avaliatri.models.Alternativa;
import br.avaliatri.models.Imagem;
import br.avaliatri.models.Questao;
import br.avaliatri.repositories.AlternativaRepository;
import br.avaliatri.repositories.ImagemRepository;
import br.avaliatri.repositories.QuestaoRepository;
import br.avaliatri.services.AlternativaService;
import br.avaliatri.services.QuestaoService;
import br.avaliatri.upload.CaminhoImagem;
import br.avaliatri.upload.ImagemService;
import br.avaliatri.upload.Img;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/upload")
@CrossOrigin
public class UploadController {

    private static final Logger log = Logger.getLogger(UploadController.class.getName());

    private ImagemService service;
    private QuestaoRepository questaoRepository;
    private ImagemRepository imagemRepository;
    private AlternativaRepository alternativaRepository;

    public UploadController(ImagemService service, QuestaoRepository questaoRepository, ImagemRepository imagemRepository, AlternativaRepository alternativaRepository) {
        this.service = service;
        this.questaoRepository = questaoRepository;
        this.imagemRepository = imagemRepository;
        this.alternativaRepository = alternativaRepository;
    }

    @PostMapping("/questao/{id}")
    public ResponseEntity<QuestaoDTO> uploadFotoQuestao(
            @RequestParam("file") MultipartFile file,
            @PathVariable("id") Integer questao_id
    ) throws Excecao {
        if (file.isEmpty()) {
            log.log(Level.SEVERE, "Arquivo nulo!");
            throw new Excecao("Arquivo enviado na requisão está nulo");
        }

        Questao questao = this.questaoRepository.findById(questao_id)
                .orElseThrow(() -> new Excecao("Questão não encontrada"));

        if(questao.getTemImagem()) {
           this.service.deletarImagem(questao.getImagem());
        }

        Img img = this.service.gerarImagem(file, questao_id);

        String caminhoImagem = this.service.store(img);
        CaminhoImagem res = new CaminhoImagem(caminhoImagem);

        Imagem imagem = new Imagem(res.getCaminho_arquivo(), img.getNomeSemExtensao(), img.getExtensao());
        imagem.getQuestoes().add(questao);
        this.imagemRepository.save(imagem);

        log.log(Level.INFO, "Imagem salva na questao de id " + imagem.getId());

        questao.setImagem(imagem);
        questao.setTemImagem(true);
        this.questaoRepository.save(questao);

        QuestaoDTO dto = QuestaoService.convertEntityToDto(questao);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/alternativa/{id}")
    public ResponseEntity<AlternativaDTO> uploadFotoAlternativa(
            @RequestParam("file") MultipartFile file,
            @PathVariable("id") Integer alternativa_id
    ) throws Excecao {
        if (file.isEmpty()) {
            log.log(Level.SEVERE, "Arquivo nulo!");
            throw new Excecao("Arquivo enviado na requisão está nulo");
        }

        Alternativa alternativa = this.alternativaRepository.findById(alternativa_id)
                .orElseThrow(() -> new Excecao("Questão não encontrada"));

        if(alternativa.getTemImagem()) {
            this.service.deletarImagem(alternativa.getImagem());
        }

        Img img = this.service.gerarImagem(file, alternativa.getQuestao().getId(), alternativa_id);

        String caminhoImagem = this.service.store(img);
        CaminhoImagem res = new CaminhoImagem(caminhoImagem);

        Imagem imagem = new Imagem(res.getCaminho_arquivo(), img.getNomeSemExtensao(), img.getExtensao());
        imagem.getAlternativa().add(alternativa);
        this.imagemRepository.save(imagem);

        log.log(Level.INFO, "Imagem salva na alternativa de id " + imagem.getId());

        alternativa.setImagem(imagem);
        alternativa.setTemImagem(true);
        this.alternativaRepository.save(alternativa);
        AlternativaDTO dto = AlternativaService.convertEntityToDto(alternativa);
        return ResponseEntity.ok().body(dto);
    }
}
