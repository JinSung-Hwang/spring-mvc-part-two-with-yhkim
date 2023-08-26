package hello.itemservice.web.validation;

import hello.itemservice.web.validation.form.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

//  @ModelAttribute: 필드 단위로 정교하게 바인딩이 된다. 특정 필드가 타입이 안맞아 바인딩이 안되어도 나머지 필드가 정상바인딩이 된다. (참고: QueryString, PostForm을 다룰때 사용함)
//  @ModelAttribute: 객체 단위로 바인딩이 된다. 바인딩이 되지 않으면 controller를 호출하지 않고 예외를 발생한다. 당연히 validator도 적용되지 않는다. (참고: HTTP Body, JSON을 다룰떄 사용함)

  @PostMapping("/add")
  public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {
    log.info("API 컨트롤러 호출");

    if (bindingResult.hasErrors()) {
      log.info("검증 오류 발생 error={}", bindingResult);
      return bindingResult.getAllErrors();
    }

    log.info("성공 로직 실행");
    return form;
  }
}
