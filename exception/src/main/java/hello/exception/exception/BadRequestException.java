package hello.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 오류")
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad") // note: 메시지를 메시지 코드로 사용할 수 도 있다. 동작은 ResponseStatusExceptionResolver.applyStatusAndReason 메서드에서 reason이 notnull이면 messages.properties를 먼저 찾는다.
// note: 오류가 발생해도 RuntimeException때문에 500에러가 발생하는것이 아니고 400에러가 발생한다.
// note: 동작 원리는 Spring이 실행될때 기본으로 등록하는 ResponseStatusExceptionResolver.class에서 발생한 Exception의 애노테이션을 확인한다.
// note: 애노테이션의 code과 reason값을 확인하여 response.send() 호출또는 ModelAndView를 리턴한다.
public class BadRequestException extends RuntimeException{

}
