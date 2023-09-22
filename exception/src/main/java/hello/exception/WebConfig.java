package hello.exception;

import hello.exception.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

@Configuration
public class WebConfig {
  @Bean
  public FilterRegistrationBean logFilter() {
    FilterRegistrationBean<Filter> filterRegistrationBean =new FilterRegistrationBean<>();
    filterRegistrationBean.setFilter(new LogFilter());
    filterRegistrationBean.setOrder(1);
    filterRegistrationBean.addUrlPatterns("/*");
    filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR); // note: 필터는 DispatcherType.REQUEST, DispatcherType.ERROR 일때만 호출되도록 만듬
    // DispatcherType.REQUEST 는 API가 처음 호출될때를 말한다.
    // DispatcherType.ERROR 는 서블릿에서 에러가 발생했을때를 말한다.
    // filter의 setDispatcherTypes의 기본값은 DispatcherType.REQUEST 이기때문에 API가 처음 호출될떄만 필터가 호출된다.
    return filterRegistrationBean;
  }
}
