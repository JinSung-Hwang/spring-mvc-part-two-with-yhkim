package hello.typeconverter.formatter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.text.ParseException;
import java.util.Locale;
import org.junit.jupiter.api.Test;


class MyNumberFormatterTest {

  MyNumberFormatter formatter = new MyNumberFormatter();

  @Test
  void parse() throws ParseException {
    // Given // When
    Number parse = formatter.parse("1,000", Locale.KOREA);
    // Then
    assertThat(parse).isEqualTo(1000L);
  }

  @Test
  void print() {
    // Given // When
    String print = formatter.print(1000, Locale.KOREA);
    // Then
    assertThat(print).isEqualTo("1,000");
  }


}