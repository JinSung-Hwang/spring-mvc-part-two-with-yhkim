package hello.login.web.session;

import hello.login.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;


class SessionManagerTest {
  SessionManager sessionManager = new SessionManager();

  @Test
  public void sessionTest() {
    MockHttpServletResponse response = new MockHttpServletResponse();

    Member member = new Member();
    sessionManager.createSession(member, response);

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setCookies(response.getCookies());
    // 세션 조회
    Object session = sessionManager.getSession(request);

    assertThat(session).isEqualTo(member);

    // 세션 만료
    sessionManager.expire(request);
    Object expired = sessionManager.getSession(request);
    assertThat(expired).isNull();
   }
}