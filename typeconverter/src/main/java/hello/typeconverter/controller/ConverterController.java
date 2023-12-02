package hello.typeconverter.controller;

import hello.typeconverter.type.IpPort;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ConverterController {

//   note: HelloConverter.java에서는 Http Request에서 주어지는 값들을 ConversionService를 통해 값을 변환했다.
//   note: 여기에서는 View에 데이터를 전달할때 Model에 객체를 그대로 넣지만 ConversionService를 통해 Thymeleaf에서 사용할 수 있는 형태로 변환되는것을 확인하려고 한다.
//   note: 하여 ConverterController와 Converter-form.html, Converter-view.html 파일을 같이 보면서 확인하면 된다.

  @GetMapping("/converter-view")
  public String converterView(Model model) {
    model.addAttribute("number", 10000);
    model.addAttribute("ipPort", new IpPort("127.0.0.1", 8080));
    return "converter-view";
  }

  @GetMapping("/converter/edit")
  public String converterForm(Model model) {
    IpPort ipPort = new IpPort("127.0.0.1", 8080);
    Form form = new Form(ipPort);
    model.addAttribute("form", form);
    return "converter-form";
  }

  @PostMapping("/converter/edit")
  public String converterEdit(Form form, Model model) {
    IpPort ipPort = form.getIpPort();
    model.addAttribute("ipPort", ipPort);
    return "converter-view";
  }

  @Data
  static class Form {
    private IpPort ipPort;

    public Form(IpPort ipPort) {
      this.ipPort = ipPort;
    }
  }
}
