package hello.login;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LogInterceptor())
      .order(1)
      .addPathPatterns("/**") // **는 하위 경로까지 모두 적용한다는 뜻이다. *는 현재 경로만 적용한다는 뜻이다.
      .excludePathPatterns("/css/**", "/*.ico", "/error"); // addPathPatterns로 모든 경로는 허용하지만 css, ico, error는 제외한다.
      // interceptor는 filter와는 다르게 배제할 pathPattern도 지정할 수도 있고 ?, *, **, {spring} 등과 같은 패턴을 사용할 수 있다.
  }

//  @Bean
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
