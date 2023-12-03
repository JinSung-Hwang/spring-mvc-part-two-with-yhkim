package hello.typeconverter.formatter;


import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

@Slf4j
public class MyNumberFormatter implements Formatter<Number> {
  // note: formatter는 문자를 객체로 또는 객체를 문자로 변경하는 역할을 한다. formatter는 application내부에서 사용되는 일을 한다. ex) date type을 문자로 변경한다.
  // note: 이전에 배웠던 converter는 객체에서 객체로 변경하는 역할도 한다. converter가 더 범용적으로 사용된다.

  @Override
  public Number parse(String text, Locale locale) throws ParseException {
    System.out.println("text = " + text);
    // "1,000" -> 1000
    NumberFormat format = NumberFormat.getNumberInstance(locale);
    return format.parse(text);
  }

  @Override
  public String print(Number object, Locale locale) {
    System.out.println("object = " + object);
    NumberFormat instance = NumberFormat.getInstance(locale); // note: "1,000" 처럼 숫자를 문자 형태로 변경할때는 NumberFormat.getInstance()를 사용한다. 이 객체가 나라별로 포맷을 변형해준다.
    return instance.format(object);
  }
}
