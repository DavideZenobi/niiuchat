package io.dz.niiuchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;

@SpringBootApplication(exclude = { JooqAutoConfiguration.class })
public class NiiuChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(NiiuChatApplication.class, args);
	}

}
