package hello.typeconverter.ConverterTest;

import static org.assertj.core.api.Assertions.assertThat;

import hello.typeconverter.converter.IntegerToStringConverter;
import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIntegerConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.type.IpPort;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

public class ConversionServiceTest {
  @Test
  void conversionService() {
    // 등록
    DefaultConversionService conversionService = new DefaultConversionService(); // note: 스프링 부트는 기본으로 ConversionService를 제공한다.
    conversionService.addConverter(new StringToIntegerConverter()); // note: 이전에 정의해둔 컨버터를 DefaultConversionService에 등록한다.
    conversionService.addConverter(new IntegerToStringConverter());
    conversionService.addConverter(new StringToIpPortConverter());
    conversionService.addConverter(new IpPortToStringConverter());

    // 사용

    assertThat(conversionService.convert("10", Integer.class)).isEqualTo(10); // note: 사용할때는 변환할 value와 변환한 타입만 지정하면 된다.
    assertThat(conversionService.convert(10, String.class)).isEqualTo("10");
    assertThat(conversionService.convert("127.0.0.1:8080", IpPort.class)).isEqualTo(new IpPort("127.0.0.1", 8080));
    assertThat(conversionService.convert(new IpPort("127.0.0.1", 8080), String.class)).isEqualTo("127.0.0.1:8080");
  }

}
