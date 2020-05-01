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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public DatabaseInstantiate(BCryptPasswordEncoder pe, UsuarioRepository usuarioRepository, ProvaRepository provaRepository, QuestaoRepository questaoRepository, AlternativaRepository alternativaRepository) {
        this.pe = pe;
        this.usuarioRepository = usuarioRepository;
        this.provaRepository = provaRepository;
        this.questaoRepository = questaoRepository;
        this.alternativaRepository = alternativaRepository;
    }

    public void init() {
        logger.log(Level.INFO, "Instanciando base generica");
        Usuario usuario = new Usuario();
        usuario.setName("Rennan Prysthon");
        usuario.setEmail("admin");
        usuario.setPassword(pe.encode("admin"));
        usuario.setCreated_at(
                Utils.getInstancia().getDataAtual()
        );
        usuario.addPerfil(Perfil.PROFESSOR);
        usuario = this.usuarioRepository.save(usuario);
        logger.log(Level.INFO, "Usuario " + usuario.getName() + " adicionado");

        LongStream.range(1, 5)
                .mapToObj(i -> {
                    Usuario u = new Usuario();
                    u.setName("Aluno " + i);
                    u.setEmail("aluno"+i+"@email.com");
                    u.setPassword(pe.encode("1234"));
                    u.addPerfil(Perfil.ALUNO);
                    u.setCreated_at(Utils.getInstancia().getDataAtual());
                    u.setIs_active(true);
                    return u;
                })
                .map(v -> usuarioRepository.save(v))
                .forEach(u -> logger.log(Level.INFO, "Usuario: [ "+u.getName()+" ] criado"));


        Usuario finalUsuario = usuario;
        LongStream.range(1, 5)
                .mapToObj(i -> {
                    Prova p = new Prova();
                    p.setTitle("Prova " + i);
                    p.setUsuario(finalUsuario);
                    p.setCreated_at(Utils.getInstancia().getDataAtual());
                    p.setDescription("Descricao da prova " + i);
                    p.setIs_activated(true);
                    p.setIs_published(true);
                    p = provaRepository.save(p);
                    p.setQuestoes(adicionarQuestoes(i,p));
                    return p;
                })
                .forEach(p -> logger.log(Level.INFO, "Prova " + p.getTitle() + " adicionada"));
    }


    private List<Questao> adicionarQuestoes(Long c, Prova p) {
        List<Questao> questoes = new ArrayList<>();
        Questao questao;
        for (int i = 1; i < 4; i++) {
            questao = new Questao();
            questao.setEnunciado("Questao " + i + " da prova " + p.getTitle());
            questao.setAlternativas(Arrays.asList(
                    new Alternativa("A", "Alternativa da questao " + questao.getEnunciado()),
                    new Alternativa("B", "Alternativa da questao " + questao.getEnunciado()),
                    new Alternativa("C", "Alternativa da questao " + questao.getEnunciado()),
                    new Alternativa("D", "Alternativa da questao " + questao.getEnunciado()),
                    new Alternativa("E", "Alternativa da questao " + questao.getEnunciado())
            ));
            questao.setCreated_at(Utils.getInstancia().getDataAtual());
            questao.setProva(p);
            questao.setAlternativa_correta("A");
            questao = questaoRepository.save(questao);
            questao.setAlternativas(adicinarAlternativas(i, questao));
            questao.setTemImagem(false);
            questoes.add(questao);
        }
        questaoRepository.saveAll(questoes);
        return questoes;
    }

    private List<Alternativa> adicinarAlternativas(int c, Questao q) {
        String opcoes = "ABCDE";
        List<Alternativa> alternativas = new ArrayList<>();
        Alternativa a;
        for(int i = 0; i < 5; i++) {
            a = new Alternativa();
            a.setTexto("Alternativa " + (i+1) + " da questao " + c);
            a.setOpcao(String.valueOf(opcoes.charAt(i)));
            a.setQuestao(q);
            a.setTemImagem(false);
            alternativas.add(a);
        }
        alternativaRepository.saveAll(alternativas);
        return alternativas;
    }
}
