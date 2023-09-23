package hello.exception;

import hello.exception.filter.LogFilter;
import hello.exception.interceptor.LogInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer { // note: webMvcConfigurer는 interceptor를 등록할때 사용한다.

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LogInterceptor())
      .order(1)
      .addPathPatterns("/**")
      .excludePathPatterns("/css/**", "*.icon", "/error", "/error/page/**"); // 오류 페이지 경로
      // note: interceptor는 dispatcherType을 가지고 request호출과 error호출을 구분하지 않는다.
      // note: 대신에 excludePathPatterns를 가지고 error호출은 interceptor를 타지 않도록 만들 수 있다.
  }

//  @Bean
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
