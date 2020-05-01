package br.avaliatri;

import br.avaliatri.services.DatabaseInstantiate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.env.Environment;

import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class AvaliatriApplication extends SpringBootServletInitializer implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AvaliatriApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AvaliatriApplication.class);
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
