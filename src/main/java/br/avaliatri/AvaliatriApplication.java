package br.avaliatri;

import br.avaliatri.models.Prova;
import br.avaliatri.models.Usuario;
import br.avaliatri.models.enums.Perfil;
import br.avaliatri.repositories.AlternativaRepository;
import br.avaliatri.repositories.ProvaRepository;
import br.avaliatri.repositories.QuestaoRepository;
import br.avaliatri.repositories.UsuarioRepository;
import br.avaliatri.services.DatabaseInstantiate;
import br.avaliatri.utils.Utils;
import org.apache.tomcat.jni.Proc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.KeyStore;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class AvaliatriApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AvaliatriApplication.class, args);
	}

	private static final Logger logger = Logger.getLogger(AvaliatriApplication.class.getName());
	@Autowired
	private Environment env;
	@Autowired
	private DatabaseInstantiate db;
	@Override
	public void run(String... args) throws Exception {
		logger.log(Level.INFO, "Iniciando projeto " + env.getProperty("app.name") + " no profile: " + env.getProperty("spring.profiles.active"));
		if (env.getProperty("spring.profiles.active").contains("test")) {
			db.init();
		}
	}
}
