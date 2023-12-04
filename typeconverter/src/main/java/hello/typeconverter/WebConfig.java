package hello.typeconverter;

import hello.typeconverter.converter.IntegerToStringConverter;
import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIntegerConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.formatter.MyNumberFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addFormatters(FormatterRegistry registry) {
    // note: 밑에 두줄을 주석처리하는 이유는 밑에 MyNumberFormatter를 만들어서 등록했기 때문이다. 같은 숫자를 문자로 변경하는 Converter와 Formatter가 있으면 Converter가 우선순위가 높다.
//    registry.addConverter(new StringToIntegerConverter()); // note: 내가 재정의한 converter를 만들었던것을 등록한다. 이렇게 등록하면 스프링이 알아서 사용한다.
//    registry.addConverter(new IntegerToStringConverter()); // note: 내가 재정의한 converter가 기존에 만들어져있던 같은 타임의 converter보다 우선순위가 높다.
    registry.addConverter(new StringToIpPortConverter()); // note: 즉, 내가 만든 IntegerToStringConverter가 스프링이 만든 IntegerToStringConverter보다 우선순위가 높다.
    registry.addConverter(new IpPortToStringConverter());

    registry.addFormatter(new MyNumberFormatter());
  }

}
