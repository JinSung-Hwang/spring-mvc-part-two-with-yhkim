package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
  private final LoginService loginService;
  private final SessionManager sessionManager;

  @GetMapping("/login")
  public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
    return "login/loginForm";
  }

//  @PostMapping("/login")
  public String login(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
    if (bindingResult.hasErrors()) {
      return "login/loginForm";
    }

    Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
    if (loginMember == null) {
      bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
      return "login/loginForm";
    }

    // 로그인 성공 처리 TODO

    // 쿠키에 시간 정보를 주지 않으면 세션 쿠키(브라우저 종료시 모두 종료)
    Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
    response.addCookie(idCookie);

    return "redirect:/";
  }

//  @PostMapping("/logout")
  public String logout(HttpServletResponse response) {
    return expireCookie(response, "memberId");
  }

  private static String expireCookie(HttpServletResponse response, String cookieName) {
    Cookie cookie = new Cookie(cookieName, null);
    cookie.setMaxAge(0);
    response.addCookie(cookie);
    return "redirect:/";
  }

//  @PostMapping("/login")
  public String loginV2(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
    if (bindingResult.hasErrors()) {
      return "login/loginForm";
    }

    Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
    if (loginMember == null) {
      bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
      return "login/loginForm";
    }

    // 로그인 성공 처리 TODO

    // 세션 관리자를 통해 세션을 생성하고, 회원 데이터 보관
    sessionManager.createSession(loginMember, response);

    return "redirect:/";
  }

//  @PostMapping("/logout")
  public String logoutV2(HttpServletRequest request) { // request를 넣는 이유는 request의 쿠키를 찾아서 제거하기위해서 request를 받는다.
    sessionManager.expire(request); // 물론 쿠키는 남아있지만 서버에서 세션이 지워지기 떄문에 상관없다. 물론 명시적으로 지워도 되고 그냥 남겨두는 경우도 있다.(참고로 HttpServletRequest에서도 남겨둔다.)
    return "redirect:/";
  } // 세션을 특별한 어떤 것이라기 보다는 그냥 최대한 데이터를 서버쪽에 저장해두는 방법일 뿐이다.


//  @PostMapping("/login")
  public String loginV3(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request) {
    if (bindingResult.hasErrors()) {
      return "login/loginForm";
    }

    Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
    if (loginMember == null) {
      bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
      return "login/loginForm";
    }

    // 로그인 성공 처리 TODO
    // getSession이 true(default)일때: 세션이 있으면 있는 세션 반환하고 없으면 신규 세션을 생성한다.
    HttpSession session = request.getSession(true);
    // 세션에 로그인 회원 정보 보관
    session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

    return "redirect:/";
  }

//    @PostMapping("/logout")
  public String logoutV3(HttpServletRequest request) {
    // getSession이 false일때: 세션이있으면 있는 세션을 반환하고 없으면 null을 반환한다.
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }

    return "redirect:/";
  }

  @PostMapping("/login")
  public String loginV4(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request, @RequestParam(defaultValue = "/") String redirectURL) {
    if (bindingResult.hasErrors()) {
      return "login/loginForm";
    }

    Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
    if (loginMember == null) {
      bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
      return "login/loginForm";
    }

    // 로그인 성공 처리
    HttpSession session = request.getSession(true);
    // 세션에 로그인 회원 정보 보관
    session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

    return "redirect:" + redirectURL; // note: 사용자가 보려던 화면에 갔는데 로그인 화면이 나와서 로그인했을때 홈으로 가는것이 아니라 사용자가 가려던 화면을 가게 만듬
  }

  @PostMapping("/logout")
  public String logoutV4(HttpServletRequest request) {
    // getSession이 false일때: 세션이있으면 있는 세션을 반환하고 없으면 null을 반환한다.
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }

    return "redirect:/";
  }




}


