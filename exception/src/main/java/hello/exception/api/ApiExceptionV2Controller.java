package hello.exception.api;

import hello.exception.api.ApiExceptionController.MemberDto;
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
public class ApiExceptionV2Controller {

  // note: 실행 흐름
  // note: 1. 예외가 발생하면 ExceptionResolver가 작동한다.
  // note: 2. ExceptionResolver는 가장 우선순위가 높은 ExceptionHandlerExceptionResolver를 실행한다.
  // note: 3. ExceptionHandlerExceptionResolver는 같은 컨트롤러 파일에 IllegalExceptionHandler를 처리할 수 있는 @ExceptionHandler가 있는지 확인한다.
  // note: 4. 찾아서 illegalExHandler()가 실행된다.
  // note: 5. illegalExHandler가 @RestController가 이기때문에 @ResponseBody가 적용되고 그러면 HttpConverter가 실행된다.
  // note: 6. 그러면 응답이 JSON으로 반환된다.
  // note: 7. JSON으로 반환될떄 @ResponseStatus가 있기때문에 200이 아니라 400으로 응답된다.

  // note: @ResponseStatus을 통해서 HttpStatus가 200이 아니라 400으로 응답을 보내지게 된다. 원래는 return 객체이기때문에 Spring에서는 200 OK를 반환한다. 또한 Status가 400이라고 servlet으로 가지 않는다.
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class) // note: 같은 Contoller 안에서 exception이 발생하면 이 메서드를 실행한다.
  public ErrorResult illegalExHandler(IllegalArgumentException e) {
    log.error("[exceptionHandler] ex", e);
    return new ErrorResult("BAD", e.getMessage());
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResult> userExceptionHandler(UserException e) { // note: 파라미터로 UserException을 받으면 @ExceptionHandler에 UserException를 받은거와 동일하다.
    log.error("[exceptionHandler] ex", e);
    ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
    return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler
  public ErrorResult exHandler(Exception e) { // note: Exception을 파라미터로 받으면 적용안되고 놓친 예외는 여기에서 모두 처리된다.
    // note: 이렇게 부모 클래스를 사용하면 자식 클래스의 예외도 처리할 수 있다.
    log.error("[exceptionHandler] ex", e);
    return new ErrorResult("EX", "내부 오류");
  }

  @GetMapping("/api2/members/{id}")
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
