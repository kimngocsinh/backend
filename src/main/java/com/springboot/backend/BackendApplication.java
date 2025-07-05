package com.springboot.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

@SpringBootApplication
public class BackendApplication implements InitializingBean {

    public BackendApplication(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BackendApplication.class);
		Environment env = app.run(args).getEnvironment();
		logApplicationStartup(env);
	}

	private static final Logger log = LoggerFactory.getLogger(BackendApplication.class);

	private Environment env;

	public void MainApplication(Environment env) {
		this.env = env;
	}


	private static void logApplicationStartup(Environment env) {
		String protocol = "http";
		String serverPort = env.getProperty("server.port");
		String contextPath = env.getProperty("server.servlet.context-path");
		if (!StringUtils.hasText(contextPath)) {
			contextPath = "/";
		}
		String hostAddress = "localhost";
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.warn("The host name could not be determined, using `localhost` as fallback");
		}
		String[] profile = env.getActiveProfiles();
		if (profile.length == 0) {
			profile = env.getDefaultProfiles();
		}

		String textBlock = """
                
                ----------------------------------------------------------
                Application '{}' is running! Access URLs:
                Local: \t\t{}://localhost:{}{}
                External: \t{}://{}:{}{}
                Profile(s): {}
                ----------------------------------------------------------
                
                """;

		log.info(textBlock, env.getProperty("spring.application.name"),
				protocol, serverPort, contextPath,
				protocol, hostAddress, serverPort, contextPath, profile);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
		if (activeProfiles.isEmpty()) {
			log.info("No profile is active. Using default configuration.");
			return;
		}
	}
}
