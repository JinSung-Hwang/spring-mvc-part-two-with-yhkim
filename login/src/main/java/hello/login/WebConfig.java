package hello.login;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  // 인터셉터의 장점은 filter보다 path에 관한 세밀한 설정이 가능하다는 것이다.
  // 또한 filter는 하나 의 함수에서 시작과 끝까지의 관심사를 다 처리해야하지만 interceptor는 preHandle, postHandle, afterCompletion으로 관심사 분리를 할 수 있다.
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LogInterceptor())
      .order(1)
      .addPathPatterns("/**") // **는 하위 경로까지 모두 적용한다는 뜻이다. *는 현재 경로만 적용한다는 뜻이다.
      .excludePathPatterns("/css/**", "/*.ico", "/error"); // addPathPatterns로 모든 경로는 허용하지만 css, ico, error는 제외한다.
      // interceptor는 filter와는 다르게 배제할 pathPattern도 지정할 수도 있고 ?, *, **, {spring} 등과 같은 패턴을 사용할 수 있다.

    registry.addInterceptor(new LoginCheckInterceptor())
      .order(2)
      .addPathPatterns("/**")
      .excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**", "/*.ico", "/error"); // 여기 경로일떄는 LoginCheckInterceptor를 호출 조차 하지 않는다.
  }

//  @Bean
  public FilterRegistrationBean logFilter() {
    FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
    filterRegistrationBean.setFilter(new LogFilter());
    filterRegistrationBean.setOrder(1);
    filterRegistrationBean.addUrlPatterns("/*");

    return filterRegistrationBean;
  }

//  @Bean
  public FilterRegistrationBean loginCheckFilter() {
    // 필터가 호출된다고 성능 저하가 일어나지 않는다. 성능은 네트워크나 DB와 같은 IO 작업에서 많이 일어난다.

    FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
    filterRegistrationBean.setFilter(new LoginCheckFilter());
    filterRegistrationBean.setOrder(2);
    filterRegistrationBean.addUrlPatterns("/*");

    return filterRegistrationBean;
  }
}
