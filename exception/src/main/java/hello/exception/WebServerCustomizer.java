package hello.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
  @Override
  public void customize(ConfigurableWebServerFactory factory) {
    // ServletExController(서비스 로직, 도메인 로직 등)에서 예외가 발생해서 Exception 또는 request.sendError를 통해 예외가 WAS까지 전달된다.
    // 예외를 전달받은 WAS는 예외 페이지 정보, 즉 아래에 각 예외에 따른 설정되어 있는 에러 페이지로 이동한다.
    // 에러 페이지를 이동 할때는 다시 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러(ex /error-page/500)` 모두 호출된다.

    ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
    ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");

    ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500"); // 런타임의 자식 예외 모두 함께 예외 처리가 된다.
    factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
  }
}
