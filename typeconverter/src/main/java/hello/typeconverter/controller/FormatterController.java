package hello.typeconverter.controller;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FormatterController {

  @GetMapping("/formatter/edit")
  public String formatterForm(Model model) {
    Form form = new Form();
    form.setNumber(10000);
    form.setLocalDateTime(LocalDateTime.now());
    model.addAttribute("form", form);
    return "formatter-form";
  }

  @PostMapping("/formatter/edit")
  public String formatterEdit(@ModelAttribute Form form) { // note: @ModelAttribute를 붙이면 model에 form 객체가 자동으로 바인딩이 된다. 하여 model.addAttribute("form", form);을 생략할 수 있다.
    return "formatter-view";
  }

  @Data
  static class Form {
    @NumberFormat(pattern = "###,###") // note: 이 어노테이션 포맨터는 데이터를 받을때도 적용되고, 화면에 출력할떄도 적용된다. 화면에서는 ${{}}를 사용하면 된다.
    private Integer number;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // note: MM을 대문자로 쓰는것은 국제 표준이다.
    private LocalDateTime localDateTime;
  }

}
