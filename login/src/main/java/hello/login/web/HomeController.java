package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String home() {
        return "home";
    }

    // required = false로 주는 이유는 로그인 안된 사용자도 들어와야 하기 때문이다.
    // memberId는 String이지만 Long으로 줘도 typeConvert되어서 넘어온다.
//    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
      if (memberId == null) {
        return "home";
      }

      //로그인
      Member loginMember = memberRepository.findById(memberId);
      if (loginMember == null) {
        return "home";
      }

      model.addAttribute("member", loginMember);
      return "loginHome";
    }

//  @GetMapping("/")
  public String homeLoginV2(HttpServletRequest request, Model model) {
    // 세션 관리자에 저장된 회원 정보 조회
    Member member = (Member) sessionManager.getSession(request);

    //로그인
    if (member == null) {
      return "home";
    }

    model.addAttribute("member", member);
    return "loginHome";
  }

//  @GetMapping("/")
  public String homeLoginV3(HttpServletRequest request, Model model) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return "home";
    }
    Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

    // 세션에 회원 데이터가 없으면 home
    if (loginMember == null) {
      return "home";
    }

    // 세션이 유지되면 로그인으로 이동
    model.addAttribute("member", loginMember);
    return "loginHome";
  }

  @GetMapping("/")
  public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {
    // @SessionAttribute는 세션을 생성하지 않기때문에 세션을 찾을때만 사용한다.

    // 아래 4줄 로직이 모두 @SessionAttribute로 모두 처리 된다.
//    HttpSession session = request.getSession(false);
//    if (session == null) {
//      return "home";
//    }
//    Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

    // 세션에 회원 데이터가 없으면 home
    if (loginMember == null) {
      return "home";
    }

    // 세션이 유지되면 로그인으로 이동
    model.addAttribute("member", loginMember);
    return "loginHome";
  }
}










