package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

	@Bean
	Hibernate5JakartaModule hibernate5JakartaModule() {
//		return new Hibernate5JakartaModule(); //LAZY 로딩 무시

		//LAZY 로딩 한번에 다 끌고오기
		Hibernate5JakartaModule hibernate5JakartaModule = new Hibernate5JakartaModule();
//		hibernate5JakartaModule.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING, true);
		return hibernate5JakartaModule;
	}

}
