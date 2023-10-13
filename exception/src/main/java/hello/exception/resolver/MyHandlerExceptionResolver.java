package hello.exception.resolver;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
  // note: What: API안에서 발생하는 다양한 에러들을 전환시킬 수 있는 resolver이다.
  // note: Why: 에러 전환을 통해서 클라이언트에 통일되고 일괄된 에러를 전달할 수 있다.
  // note: How: HandlerExceptionResolver를 상속받아서 resolveException를 구현해주면 된다.
  @Override
  public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    // note: view(HTML)을 리턴하는 API의 경우 error 페이지를 리턴하면 되지만 json을 리턴하는 api의 경우 내부적으로 다양한 경우의 error가 밸생할 수 있다. (io, runtime, IllegalAargumentException 등등)
    // note: 이럴때 발생한 에러를 바로 리턴하는 것이 아니라 HandlerExceptionResolver를 통해서 에러 전환을 시킬 수 있다. (400, 401, 403, 404, .... )

    try {
      if (ex instanceof IllegalArgumentException) {
        log.info("IllegalArgumentException resolver to 400");
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        return new ModelAndView();
      }
    } catch (IOException e) {
      log.error("resolver ex", e);
    }

    return null;
  }
}
