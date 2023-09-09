package hello.login;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class WebConfig {

  @Bean
  public FilterRegistrationBean logFilter() {
    FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
    filterRegistrationBean.setFilter(new LogFilter());
    filterRegistrationBean.setOrder(1);
    filterRegistrationBean.addUrlPatterns("/*");

    return filterRegistrationBean;
  }

  @Bean
  public FilterRegistrationBean loginCheckFilter() {
    // 필터가 호출된다고 성능 저하가 일어나지 않는다. 성능은 네트워크나 DB와 같은 IO 작업에서 많이 일어난다.

    FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
    filterRegistrationBean.setFilter(new LoginCheckFilter());
    filterRegistrationBean.setOrder(2);
    filterRegistrationBean.addUrlPatterns("/*");

    return filterRegistrationBean;
  }



}
