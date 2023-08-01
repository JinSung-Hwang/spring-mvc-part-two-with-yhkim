package hello.itemservice.validation;

import hello.itemservice.domain.item.Item;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class BeanValidationTest {

  @Test
  void beanValidation() {
    // validation을 통해서 validation annotation을 직접 실행 시키는 방법
    // 평상시에는 스프링을 통해서 validation을(@Validated, @Valid를 통해) 실행시키기 때문에 아래 로직처럼 validation을 직접 실행 시킬 필요는 없다.
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    Item item = new Item();
    item.setItemName(" "); // 공백
    item.setPrice(0);
    item.setQuantity(10000);

    Set<ConstraintViolation<Item>> violations = validator.validate(item);
    for (ConstraintViolation<Item> violation : violations) {
      System.out.println("violation = " + violation);
      System.out.println("violation = " + violation.getMessage());
    }

  }
}
