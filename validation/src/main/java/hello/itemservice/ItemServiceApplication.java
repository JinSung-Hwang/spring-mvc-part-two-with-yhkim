package hello.itemservice;

import hello.itemservice.web.validation.ItemValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
public class ItemServiceApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

//	@Override
//	public Validator getValidator() { // validator global 적용이다.
   	  // controller에는 @Validated 라는 어노테이션을 붙어있어야한다.
	    // 하나의 validator를 global로 사용하는일은 잘 없을거 같다.
//		return new ItemValidator();
//	}
}
