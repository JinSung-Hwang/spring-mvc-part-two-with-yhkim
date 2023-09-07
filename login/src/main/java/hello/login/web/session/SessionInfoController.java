package hello.login.web.session;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SessionInfoController {

  @GetMapping("/session-info")
  public String sessionInfo(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return "세션이 없습니다.";
    }

    // 세션 데이터 출력
    session.getAttributeNames().asIterator()
        .forEachRemaining(name -> log.info("session name={}, value={}", name, session.getAttribute(name)));

    log.info("sessionId={}", session.getId()); // 세션 아이디
    log.info("maxInactiveInterval={}", session.getMaxInactiveInterval()); // 세션 유효 시간 (밀리 초)
    log.info("creationTime={}", new Date(session.getCreationTime())); // 세션 생성 시간
    log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime())); // 세션에 마지막에 접근한 시간
    log.info("isNew = {}", session.isNew()); // 새로 생성된 세션인지 확인 // 세션을 가져다 쓴것이면 false가 반환된다. 여기서는 false가 반환되었다.

    return "세션 출력";
  }

}
