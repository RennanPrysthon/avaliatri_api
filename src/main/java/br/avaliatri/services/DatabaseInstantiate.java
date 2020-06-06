package br.avaliatri.services;

import br.avaliatri.models.Alternativa;
import br.avaliatri.models.Prova;
import br.avaliatri.models.Questao;
import br.avaliatri.models.Usuario;
import br.avaliatri.enums.Perfil;
import br.avaliatri.repositories.AlternativaRepository;
import br.avaliatri.repositories.ProvaRepository;
import br.avaliatri.repositories.QuestaoRepository;
import br.avaliatri.repositories.UsuarioRepository;
import br.avaliatri.utils.Utils;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.LongStream;

@Service
public class DatabaseInstantiate {
    private static final Logger logger = Logger.getLogger(DatabaseInstantiate.class.getName());

    private BCryptPasswordEncoder pe;
    private UsuarioRepository usuarioRepository;
    private ProvaRepository provaRepository;
    private QuestaoRepository questaoRepository;
    private AlternativaRepository alternativaRepository;
    private static final String POSICOES = "ABCDE";
    public DatabaseInstantiate(BCryptPasswordEncoder pe, UsuarioRepository usuarioRepository, ProvaRepository provaRepository, QuestaoRepository questaoRepository, AlternativaRepository alternativaRepository) {
        this.pe = pe;
        this.usuarioRepository = usuarioRepository;
        this.provaRepository = provaRepository;
        this.questaoRepository = questaoRepository;
        this.alternativaRepository = alternativaRepository;
    }

    public void init() {
        this.alternativaRepository.deleteAll();
        this.provaRepository.deleteAll();
        this.questaoRepository.deleteAll();
        this.usuarioRepository.deleteAll();
        logger.log(Level.INFO, "Instanciando base generica");

        Usuario usuario = new Usuario();
        usuario.setPerfil(Perfil.PROFESSOR);
        usuario.setName("Rennan Prysthon");
        usuario.setPassword(pe.encode("1234"));
        usuario.setEmail("rennandelcastillo@gmail.com");
        usuario = this.usuarioRepository.save(usuario);

        Questao questao = new Questao();
        questao.setEnunciado("Enunciado da questao 1");
        questao.setTextoApoio("Acao da questao 1");
        questao.setAlternativas(getAlternativas());
        questao.setAlternativa_correta("A");
        questao = this.questaoRepository.saveAndFlush(questao);

        Prova prova = new Prova();
        prova.setTitle("Prova teste");
        prova.setDescription("Descriçao da prova");
        prova.setUsuario(usuario);
        prova.addQuestao(questao);
        this.provaRepository.save(prova);
    }

    public List<Alternativa> getAlternativas() {
        List<Alternativa> alternativas = new ArrayList<>();
        Alternativa alternativa;
        for(int i = 0; i < 5; i++) {
            alternativa = new Alternativa();
            alternativa.setTemImagem(false);
            alternativa.setTexto("Texto " + i);
            alternativa.setOpcao(String.valueOf(POSICOES.charAt(i)));
            alternativas.add(alternativa);
        }

        return alternativas;
    }
}
