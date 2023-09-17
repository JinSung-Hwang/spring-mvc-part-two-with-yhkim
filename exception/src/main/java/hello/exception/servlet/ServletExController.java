package hello.exception.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ServletExController {
  // 자바의 기본 에러 처리
  //   자바의 메인 메서드를 직접 싱행하는 경우 main이라는 이름의 쓰레드가 실행된다.
  //   실행 도중에 예외를 잡지 못하고 처음 실행한 main() 메서드를 넘어서 예외가 던져지면, 예외 정보를 남기고 해당 쓰레드는 종료된다.

  // 웹 애플리케이션 예외 처리
  //   컨트롤러에서 예외가 발생하면, 컨트롤러 밖으로 예외를 던지고, 컨트롤러 밖에는 인터셉터 -> 서블릿 -> 필터 -> 컨테이너까지 예외를 던진다.
  //   결국에는 톰캣과 같은 was가 예외로 전달되는 것이다.

  // 스프링이 아닌 순수 서블릿의 예외 처리 2가지
  //   1. throw 를 통해서 tomcat까지 예외를 던진다. 이러면 tomcat은 500에러를 리턴한다.
  //   2. response.sendError(httpStatus, errorMessage) 를 통해서 예외를 던진다. response.sendError는 인터셉터, 서블릿, 필터 등에서는 예외로 처리하지는 않지만 tomcat까지 가서는 설정한 httpStatus에 따라 다른 에러를 리턴한다.

  @GetMapping("/error-ex")
  public void errorEx() {
    log.info("error-ex");
    throw new RuntimeException("예외 발생");
  }

  @GetMapping("/error-404")
  public void error404(HttpServletResponse response) throws IOException {
    log.info("error-404");
    response.sendError(404, "404 오류");
  }

  @GetMapping("/error-500")
  public void error500(HttpServletResponse response) throws IOException {
    log.info("error-500");
    response.sendError(500);
  }

}
