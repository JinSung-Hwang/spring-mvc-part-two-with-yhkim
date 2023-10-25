package hello.exception.exceptionHandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exceptionHandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice // note: 대상으로 지정한 여러 컨트롤러에 @ExceptionHandler, @InitBinder 기능을 부여해주는 애노테이션이다. 대상을 지정하지 않으면 모든 컨트롤러에 글로벌 적용된다.
// note: 대상 지정 예시 3가지:: @RestControllerAdvice("org.example.controllers"), @ControllerAdvice(annotation = RestController.class), @ControllerAdvice(assignableTypes = {AbstractController.class})
public class ExceptionControllerAdvice {
// note: @ControllerAdvice는 ApiExceptionV2Controller에서 @ExceptionHandler 메서드가 각 컨트롤에 중복해서 있는것과 실행과 예외처리 메서드가 같이 있는것을 분리할 수 있게 해준다.

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

}
