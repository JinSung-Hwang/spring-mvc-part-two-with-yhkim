package hello.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component // spring templates/error/xxx.html 을 사용하기 위해서 주석처리함
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

// note: 이렇게 WebServerFactoryCustomizer<ConfigurableWebServerFactory>를 이용해서 에러에따른 에러 라우팅을 등록하지 않고 스프링 부트에서 이미 설정된 세팅으로 에러페이지를 제공할 수 있다.
// note: 스프링에서 이미 설정된 세팅으로 에러페이지를 제공하면 html만 만들어서 특정 경로에 생성해두면 된다. (스프링 부트 BasicErrorController.java 파일을 보면 세팅이 되어있다.)

// note: html을 특정 경로에 생성해두는것은 2가지 우선순위를 갖는다.
// note:   1. 특정 경로의 error.html 파일이 우선순위를 갖는다.
// note:   2. 포괄적인 에러 페이지보다는 자세한 페이지가 우선순위를 갖는다. ex) 4xx.html 보다는 404.html이 더 우선순위를 갖는다.

// note: 1. 특정 경로 error.html 파일의 우선순위
// note:   1. 뷰 템플릿
// note:      1. resources/templates/error/500.html
// note:   2. 정적 리소스 (static, public)
// note:      1. resources/static/error/500.html
// note:      1. resources/public/error/500.html
// note:   3. 적용 대상이 없을때 뷰 이름
// note:      1. resources/templates/error.html


