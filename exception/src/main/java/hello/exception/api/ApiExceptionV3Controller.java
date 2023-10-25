package hello.exception.api;

import hello.exception.exception.UserException;
import hello.exception.exceptionHandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ApiExceptionV3Controller {

  // note: 실행 흐름
  // note: 1. 예외가 발생하면 ExceptionResolver가 작동한다.
  // note: 2. ExceptionResolver는 가장 우선순위가 높은 ExceptionHandlerExceptionResolver를 실행한다.
  // note: 3. ExceptionHandlerExceptionResolver는 exceptionHandler.advice 패키지 안에있는 ExceptionControllerAdvice에 IllegalExceptionHandler를 처리할 수 있는 @ExceptionHandler가 있는지 확인한다.
  // note: 4. illegalExHandler가 @RestControllerAdvice때문에 @ResponseBody가 적용되고 그러면 HttpConverter가 실행된다.
  // note: 5. 그러면 응답이 JSON으로 반환된다.
  // note: 6. JSON으로 반환될떄 @ResponseStatus가 있기때문에 200이 아니라 400으로 응답된다.


  @GetMapping("/api3/members/{id}")
  public MemberDto getMember(@PathVariable("id") String id) {
    if (id.equals("ex")) {
      throw new RuntimeException("잘못된 사용자");
    }
    if (id.equals("bad")) {
      throw new IllegalArgumentException("잘못된 입력값");
    }
    if (id.equals("user-ex")) {
      throw new UserException("사용자 오류");
    }

    return new MemberDto(id, "hello" + id);
  }

  @Data
  @AllArgsConstructor
  static class MemberDto {
    private String memberId;
    private String name;
  }
}
