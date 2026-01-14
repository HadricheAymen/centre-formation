package spring.jpa.centre_formation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CentreFormationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CentreFormationApplication.class, args);
	}

}
