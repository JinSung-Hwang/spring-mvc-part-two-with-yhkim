package hello.exception.api;

import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionController {

  @GetMapping("/api/members/{id}")
  public MemberDto getMember(@PathVariable("id") String id) {
    if (id.equals("ex")) {
      throw new RuntimeException("잘못된 사용자");
    }
    if (id.equals("badh")) {
      throw new IllegalArgumentException("잘못된 입력값");
    }
    if (id.equals("user-ex")) {
      throw new UserException("사용자 오류");
    }


    return new MemberDto(id, "hello" + id);
  }

  @GetMapping("/api/response-status-ex1")
  public String responseStatusEx1() {
    throw new BadRequestException();
  }

  @GetMapping("/api/response-status-ex2")
  public String responseStatusEx2() {
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
    // note: spring에서 제공하는 에러 전환 메서드이다.
    // note: 3번째 파라미터에 발생한 예외를 넣고 1, 2번쨰 파라미터에 어떤 에러 코드와 메세지를 작성할것인지 넣으면 된다.
  }

  @GetMapping("/api/default-handler-ex")
  public String defaultException(@PathVariable Integer data) { // note: API에 data에 String값을 넣으면 400에러가 발생한다.
    // note: 원래는 TypeMismatchException가 발생하면서 서블릿 컨테이너까지 올라가면서 500에러가 발생한다.
    // note: 하지만 스프링 부트에 기본적으로 등록된 DefaultHandlerExceptionResolver.class가 500에러가 아니라 400에러가 발생하도록 예외전환을 한다.

    // note: 즉, 정리하자면 DefaultHandlerExceptionResolver.class는 스프링 부트가 제공하는 기본 예외 처리기이다.

    return "ok";
  }

  @Data
  @AllArgsConstructor
  static class MemberDto {
    private String memberId;
    private String name;
  }
}
