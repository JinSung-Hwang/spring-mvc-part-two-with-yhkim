package hello.typeconverter.formatter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.type.IpPort;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;

public class FormattingConversionServiceTest {
  @Test
  void formattingConversionService() {
    // note: DefaultFormattingConversionService는 내부적으로 conversionService를 상속받아서 사용하고 있다.
    // note: 그래서 formatter와 converter를 모두 등록할 수 있다.
    // note: formatter와 converter를 모두 등록하는 서비스는 다르지만 사용할때는 같은 메소드인 convert()를 사용한다.
    DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
    // 컨버터 등록
    conversionService.addConverter(new StringToIpPortConverter());
    conversionService.addConverter(new IpPortToStringConverter());
    // 포맷터 등록
    conversionService.addFormatter(new MyNumberFormatter());

    // 컨버터 사용
    IpPort convert = conversionService.convert("127.0.0.1:8080", IpPort.class);
    assertThat(convert).isEqualTo(new IpPort("127.0.0.1", 8080));
    // 포맷터 사용
    assertThat(conversionService.convert(1000L, String.class)).isEqualTo("1,000");
    assertThat(conversionService.convert("1,000", Long.class)).isEqualTo(1000L);
  }
}
