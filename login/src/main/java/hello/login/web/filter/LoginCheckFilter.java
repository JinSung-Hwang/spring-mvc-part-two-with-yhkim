package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Component
public class LoginCheckFilter implements Filter {
  // init 메소드와 destory 메소드는 오버라이드해서 구현하지 않아도 되도록 filter interface에서 default 키워드를 사용했다.

  private static final String[] whitelist = {"/", "/members/add", "/login", "/logout", "/css/*"};

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String requestURI = httpRequest.getRequestURI();

    HttpServletResponse httpResponse = (HttpServletResponse) response;

    try {
      log.info("인증 체크 필터 시작 {}", requestURI);
      if (isLoginCheckPatch(requestURI)) {
        log.info("인증 체크 로직 실행 {}", requestURI);
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
          log.info("미인증 사용자 요청 {}", requestURI);
          // 로그인 화면으로 redirect
          httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
          return; // 다음 필터, 서블릿, 컨트롤러를 실행시키지 않음
        }
      }

      chain.doFilter(request, response);
      // note: request, response에는 ServletRequest또는 ServletResponse의 구현체로만 넘겨주면 잘 동작된다.
      // note: 물론 이렇게는 잘 사용하지는 않지만 참고차 알아두자.
    } catch (Exception e) {
      throw e;
    } finally {
      log.info("인증 체크 필터 종료 {}", requestURI);
    }
  }

  /**
   * 화이트 리스트의 경우 인증 체크X
   */
  private boolean isLoginCheckPatch(String requestURI) {
    return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
  }


}
