package hello.itemservice.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class MessageSourceTest {

  @Autowired
  MessageSource ms; // message를 쓰고 싶으면 이렇게 autowired로 주입받아서 아래 테스트 코드처럼 필요에 따라서 message를 사용하면 된다.

  @Test
  void helloMessage() {
    String result = ms.getMessage("hello", null, null);
    assertThat(result).isEqualTo("안녕");
  }

  @Test
  void notFoundMessageCode() {
    assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
            .isInstanceOf(NoSuchMessageException.class);
  }

  @Test
  void notFoundMessageCodeDefaultMessage() {
    String result = ms.getMessage("no_code", null, "기본 메세지", null);
    assertThat(result).isEqualTo(result);
  }

  @Test
  void argumentMessage() {
    String message = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
    assertThat(message).isEqualTo("안녕 Spring");

    // 참고 파라미터는 다음과 같이 사용할 수 있다.!!
    // hello.name=안녕 {0}
    // <p th:text="#{hello.name(${item.itemName})}"></p>   // thymeleaf에서는 #{}를 통해 mesage를 가져오고 #{}안쪽에 ()를 넣어서 파라미터 값을 받을 수 있다.


  }

  @Test
  void defaultLang() {
    assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
    assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
  }

  @Test
  void enLang() {
    assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
  }

}