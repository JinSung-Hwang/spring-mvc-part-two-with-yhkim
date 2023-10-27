package hello.thymeleaf.basic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/basic")
public class BasicController {
    @GetMapping("text-basic")
    public String textBasic(Model model) {
      model.addAttribute("data", "Hello <b>Spring!</b>");
      // note: HTML엔티티 란?
      // note: HTML예약어 대신 기존 사용하던 문자열 그대로 사용하기 위해서 만든 문자셋을 HTML엔티티라고 한다.
      // note: 위에 Hello <br> Spring! </br> 와 같은것은 HTML엔티티로 이스케이프(변환)되어서 전달되고 브라우저에서는 HTML엔티티를 다시 문자로 변환해서 보여준다.
      // note: 이스케이프 하는 이유?
      // note: 사용자가 입력한 테그 값때문에 화면이 깨질수 있고 악의적으로 XSS 공격을 할 수 있기 때문이다.
      // note: 그래서 기본적으로 이스케이프처리 되는 th:text나 [[${data}]]를 사용한다.
      return "basic/text-basic";
    }

    @GetMapping("text-unescaped")
    public String textUnescaped(Model model) {
      model.addAttribute("data", "Hello <b>Spring!</b>");
      return "basic/text-unescaped";
    }
}
