package io.spring2go.authcodeserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableAuthorizationServer //授权服务器配置
public class AuthCodeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthCodeServerApplication.class, args);
	}
}
