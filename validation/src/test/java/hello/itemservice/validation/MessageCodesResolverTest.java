package hello.itemservice.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

public class MessageCodesResolverTest {

  // MessageCodesResolver는 인터페이스
  // DefaultMessageCodesResolver는 구현체
  // DefaultMessageCodesResolver는 code를 넣으면 여러 code를 반환해준다.

  // MessageCodesResolver는 세분화된 오류메시지를 찾고 없으면 일반화된 오류메시지를 찾도록 도와주는 Resolver이다.
  MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

  @Test
  void messageCodesResolverObject() {
    String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
    for (String messageCode: messageCodes) {
      System.out.println("messageCode = " + messageCode);
    }

    Assertions.assertThat(messageCodes).containsExactly("required.item", "required");
  }

  @Test
  void messageCodesResolverField() {
    String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
    for (String messageCode : messageCodes) {
      System.out.println("messageCode = " + messageCode);
    }
    Assertions.assertThat(messageCodes).containsExactly(
            "required.item.itemName",
            "required.itemName",
            "required.java.lang.String",
            "required"
    );
  }

}
