package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {
  // filter를 사용하려면 filter인터페이스를 구현해야한다.
  // filter는 싱글톤으로 동작한다.
  // filter는 filterchain을 통해서 다음 filter를 호출 시킬 수 있다.

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    log.info("log filter init");
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    log.info("log filter doFilter");

    // ServletRequest는 HttpServletRequest의 인터페이스이다. 기능이 많이 없어서 다운 캐스팅해서 사용해야한다.
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String requestURI = httpRequest.getRequestURI();

    String uuid = UUID.randomUUID().toString();

    try {
      log.info("REQUEST [{}][{}]", uuid, requestURI);
      chain.doFilter(request, response); // 다음 필터를 호출하거나 서블릿을 호출하게하는 함수
    } catch (Exception e) {
      throw e;
    } finally {
      log.info("RESPONSE [{}][{}]", uuid, requestURI);
    }
  }

  @Override
  public void destroy() {
    log.info("log filter destory");
  }
}
