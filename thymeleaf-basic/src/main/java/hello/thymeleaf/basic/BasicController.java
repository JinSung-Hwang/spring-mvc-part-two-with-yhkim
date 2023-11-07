package hello.thymeleaf.basic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.stereotype.Component;
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

    @GetMapping("variable")
    public String variable(Model model) {

      User userA = new User("userA", 10);
      User userB = new User("userB", 20);

      List<User> list = new ArrayList<>();
      list.add(userA);
      list.add(userB);

      Map<String, User> map = new HashMap<>();
      map.put("userA", userA);
      map.put("userB", userB);

      model.addAttribute("user", userA);
      model.addAttribute("users", list);
      model.addAttribute("userMap", map);

      return "basic/variable";
    }

    @GetMapping("basic-objects")
    public String basicObjects(HttpSession session) {
      session.setAttribute("sessionData", "Hello Session");
      return "basic/basic-objects";
    }

    @Component("helloBean") // note: 이 어노테이션을 사용하면 컴포넌트스캔을 해서 HelloBean을 빈으로 등록한다.
    public class HelloBean {
      public String hello(String data) {
        return "Hello " + data;
      }
    }

    @GetMapping("date")
    public String data(Model model) { // note: 타임리프에서 제공하는 날를 다루는 방법을 배운다. (localDateTime, temporals.day)
      model.addAttribute("localDateTime", LocalDateTime.now());
      return "basic/date";
    }

    @GetMapping("link")
    public String link(Model model) {
      model.addAttribute("param1", "data1");
      model.addAttribute("param2", "data2");
      return "basic/link";
    }

    @GetMapping("/literal")
    public String literal(Model model) {
      model.addAttribute("data", "Spring!");
      return "basic/literal";
    }

    @GetMapping("/operation")
    public String operation(Model model) {
      model.addAttribute("nullData", null);
      model.addAttribute("data", "Spring!");
      return "basic/operation";
    }

    @GetMapping("/attribute")
    public String attribute() {
      return "basic/attribute";
    }

    @GetMapping("/each")
    public String each(Model model) {
      addUsers(model);
      return "basic/each";
    }

    @GetMapping("/condition")
    public String condition(Model model) {
      addUsers(model);
      return "basic/condition";
    }

    @GetMapping("/comments")
    public String comments(Model model) {
      model.addAttribute("data", "Spring!");
      return "basic/comments";
    }

    @GetMapping("/block")
    public String block(Model model) {
      addUsers(model);
      return "basic/block";
    }

    @GetMapping("/javascript")
    public String javascript(Model model) {
      model.addAttribute("user", new User("UserA", 10));
      addUsers(model);
      return "basic/javascript";
    }

    private void addUsers(Model model) {
      List<User> list = new ArrayList<>();
      list.add(new User("UserA", 10));
      list.add(new User("UserB", 20));
      list.add(new User("UserC", 30));

      model.addAttribute("users", list);
    }

    @Data
    static class User {
      private String username;
      private int age;

      public User(String username, int age) {
        this.username = username;
        this.age = age;
      }
    }
}
