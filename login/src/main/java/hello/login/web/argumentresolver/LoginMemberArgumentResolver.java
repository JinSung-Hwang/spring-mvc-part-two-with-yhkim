package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
  // note: HandlerMethodArgumentResolver은 컨트롤러 메서드에서 특정 조건에 맞는 파라미터가 있을 때 원하는 값을 바인딩해주는 인터페이스이다.
  // note: 컨트롤에서 공통으로 사용하는것을 애노테이션을 등록해두었다가 ArgumentResolver를 통해 공통으로 사용할 수 있다.

  @Override
  public boolean supportsParameter(MethodParameter parameter) { // 언제 이 LoginMemberArgumentResolver를 사용할지 체크하는 메서드
    // 이것은 내부적으로 cache가 있어서 값이 같으면 한번 검사한 것은 다시 검사하지 않는다.
    log.info("supportsParameter 실행");

    boolean hasParameterAnnotation = parameter.hasParameterAnnotation(Login.class); // @Login이 붙어있는지 확인
    boolean hasMemberType = Member.class.equals(parameter.getParameterType());// 파라미터 타입이 Member.class인지 확인

    return hasParameterAnnotation && hasMemberType;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) { // supportsParameter가 true를 반환하면 이 메서드가 실행된다.
    log.info("resolveArgument 실행");

    HttpServletRequest nativeRequest = (HttpServletRequest) webRequest.getNativeRequest();
    HttpSession session = nativeRequest.getSession(false);
    if (session == null) {
      return null;
    }

    return nativeRequest.getSession(false).getAttribute(SessionConst.LOGIN_MEMBER);
  }
}
