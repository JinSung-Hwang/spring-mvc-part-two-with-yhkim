package hello.typeconverter.controller;

import hello.typeconverter.type.IpPort;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
  @GetMapping("/hello-v1")
  public String helloV1(HttpServletRequest request) {
    String data = request.getParameter("data"); // note: getParameter()의 return type은 String이다.
    Integer intValue = Integer.valueOf(data); // note: 그래서 Integer 타입을 사용하려면 Integer.valueOf()를 사용하여 String을 Integer로 변환한다.
    System.out.println("intValue = " + intValue);
    return "ok";
  }

  @GetMapping("/hello-v2")
  public String helloV2(@RequestParam Integer data) { // note: 하지만 스프링에서는 중간에 String을 Integer로 변환하는 과정을 처리해준다. 이것이 TypeConverter의 역할이다.
    System.out.println("data = " + data);
    return "ok";
  }

  @GetMapping("/ip-port")
  public String ipPort(@RequestParam IpPort ipPort) {
    System.out.println("ipPort IP = " + ipPort.getIp());
    System.out.println("ipPort PORT = " + ipPort.getPort());
    return "ok";
  }

}
