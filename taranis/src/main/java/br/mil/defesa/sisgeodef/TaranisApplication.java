package br.mil.defesa.sisgeodef;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@ComponentScan("br.mil.defesa.sisgeodef")
public class TaranisApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaranisApplication.class, args);
	}
	
}
