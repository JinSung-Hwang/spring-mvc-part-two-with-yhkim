package hello.login.web.interceptor;

import hello.login.web.SessionConst;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String requestURI = request.getRequestURI();

    log.info("인증 체크 인터셉터 실행 {}", requestURI);

    HttpSession session = request.getSession();
    if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) { // 로그인이 안된 사용자
      log.info("미인증 사용자 요청");
      // 로그인화면으로 redirect
      response.sendRedirect("/login?redirectURL=" + requestURI);
      return false; // 다음 핸들러를 호출하지 않을때는 false를 리턴한다.
    }
    return true;
  }

}
