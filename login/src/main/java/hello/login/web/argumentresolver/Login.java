package hello.login.web.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 파라미터에 붙는 애노테이션
@Retention(RetentionPolicy.RUNTIME) // 리플렉션하려면 정보가 남아있어야한다. 그래서 런타임까지 애노테이션 정보가 남아 있을 수 있도록 @Retention을 RUNTIME으로 준다.
public @interface Login {
}
