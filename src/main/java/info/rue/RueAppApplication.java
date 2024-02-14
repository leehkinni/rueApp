package info.rue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RueAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RueAppApplication.class, args);
	}

}
