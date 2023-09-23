package hello.exception.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

  public static final String LOG_ID = "logId";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    String requestURI = request.getRequestURI();
    String uuid = UUID.randomUUID().toString();

    request.setAttribute(LOG_ID, uuid);

    // 핸들러 정보는 어떤 핸들러 매핑을 사용하는가에 따라 달라진다.
    // @RequestMapping, @Controller를 사용하면 HandlerMethod가 넘어온다.
    // `/resources/static`와 같은 정적 리소스를 요청하면 ResourceHttpRequestHandler가 넘어온다.
    if (handler instanceof HandlerMethod) {
      // @RequestMapping: HandlerMethod // 컨트롤러 메서드에 @RequestMapping이 붙어있으면 HandlerMethod가 온다.
      // 정적 리소스: ResourceHttpRequestHandler
      HandlerMethod hm = (HandlerMethod) handler;// 호출한 컨트롤러 메서드의 모든 정보가 포함되어있다.
    }

    log.info("로그 인터셉터 REQUEST preHandle [{}][{}][{}][{}]", uuid, request.getDispatcherType(), requestURI, handler);

    return true; // 리턴 false면 다음 핸들러 어댑터와 컨트롤러가 실행되지 않는다. true면 실행된다.
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    // controller에서 exception이 발생했으면 postHandle은 실행되지 않는다.


    log.info("로그 인터셉터 RESPONSE postHandle [{}]", modelAndView);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    // Exception은 컨트롤러에서 예외가 발생하지 않았다면 ex에는 null이 넘어온다.
    // 종료 로그를 afterCompletion을 사용하는 이유는 예외가 발생했을 때도 호출되기 때문이다.

    String requestURI = request.getRequestURI();
    Object logId = (String) request.getAttribute(LOG_ID); // request는 한 세션 동안 같은 request 객체를 사용하기 때문에 logId를 저장해놓으면 다른 메서드에서도 사용할 수 있다.
    // 즉 preHandle에서 저장한 logId를 afterCompletion을 사용할 수 있다.

    log.info("로그 인터셉터 RESPONSE afterCompletion [{}][{}][{}][{}]", logId, request.getDispatcherType(), requestURI, handler);
    if (ex != null) {
      log.error("afterCompletion error!!", ex);
    }

    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
