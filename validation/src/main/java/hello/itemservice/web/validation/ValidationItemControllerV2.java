package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) { // BindingResult는 Item 다음에 와야한다. 순서가 중요하다.!!! Item의 에러를 담기 때문에 그렇다고 한다.
        // bindingResult는 타임리프의 #fileds, th:errors, th:errorclass 와 같이 동작하여 error표시를 편리하게 해준다.
        // 사용법 참고 validation/src/main/resources/templates/validation/v2/addForm.html

        // BindingResult 정보
        // 1. @ModelAttribute가 걸린 Item 같은 Object에 데이터를 바인딩하다가 에러가 나면 어떻게 될까?
        //   BindingResult가 없다면 400에러가 발생하고 바로 에러 페이지를 호출한다.
        //   하지만 BindingResult가 있으면 BindingResult에 FieldError를 담아서 컨트롤러를 정상 호출한다.
        // 2. BindingResult와 Errors는 무엇인가?
        //   BindingResult는 인터페이스이고 Errors라는 인터페이스를 상속받고 있다.
        //   BindingResult를 파라미터로 사용하면 BeanPropertyBindingResult라는 구현체가 값으로 들어온다.
        //   Errors를 사용해도 되지만 BindingResult의 여러 메소드를 사용할 수 없다.

        // 검증 오류 결과를 보관하기 위한 Map
        Map<String, String> errors = new HashMap<>();

        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수 입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 10000000) {
            bindingResult.addError(new FieldError("item", "price", "가격은 1000 ~ 10000000 까지 허용합니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9999까지 허용합니다."));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량 합은 10000원 이상이어야합니다. 현재 값 = " + resultPrice));
                // Field가 따로 없고 global로 error가 발생하는 형태라서 ObjectError를 넣었다.
            }
        }

        // 검증에 실패하면 다시 등록 폼(add form)으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult); // bindingResult는 아래처럼 Model에 따로 담지 않아도 View에 전달된다.
//            model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }



//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // 기존 addItemV1는 에러가 발생하면 사용자가 입력한 값이 모두 사라지는 이슈가 있었다.
        // 하여 V2에서는 해당 이슈를 수정하는 방법을 배우자.

        // 검증 오류 결과를 보관하기 위한 Map
        Map<String, String> errors = new HashMap<>();

        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            // FieldError의 두번쨰 생성자를 통해서 생성하고 rejectedValue에 값을 넣으면 에러가 났을때 값을 입력시켜준다. 즉 오류가 나도 값을 그대로 유지 시킬 수 있는 것이다.
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null,  "상품 이름은 필수 입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 10000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1000 ~ 10000000 까지 허용합니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 최대 9999까지 허용합니다."));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", null, null,  "가격 * 수량 합은 10000원 이상이어야합니다. 현재 값 = " + resultPrice));
                // Field가 따로 없고 global로 error가 발생하는 형태라서 ObjectError를 넣었다.
            }
        }

        // 검증에 실패하면 다시 등록 폼(add form)으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult); // bindingResult는 Model에 따로 담지 않아도 view에 전달된다.
//            model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            // codes에 string을 넣는데 첫번쨰 error code를 못찾으면 두번쨰 error code를 넣는다.
            // 두번쨰 error code를 못찾으면 FieldError 파라미터의 default message를 출력한다.
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName", "default.error.message"}, null,  null));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 10000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000},  null));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice},  null));
                // Field가 따로 없고 global로 error가 발생하는 형태라서 ObjectError를 넣었다.
            }
        }

        // 검증에 실패하면 다시 등록 폼(add form)으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult); // bindingResult는 Model에 따로 담지 않아도 view에 전달된다.
//            model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        log.info("bindingResult.getObjectName {}", bindingResult.getObjectName());
        log.info("bindingResult.getTarget {}", bindingResult.getTarget());

//        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
        // 아래의 StringUtils.hastText와 bindingResult.rejectValue 구문을 합친 구문으로 만들어주는것이 ValidationUtils.rejectIfEmptyOrWhitespace 이다.

        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            // bindingResult.rejectValue는 FieldError의 objectName, field, new String[]{}의 중복된 문자열 등을 자동으로 처리해준다.
            // 자동으로 처리할 수 있는 이유는 Item뒤에 BindingResult를 파라미터로 넣기떄문에 BidingResult에서는 앞에있는 Object의 이름을 알 수 있다.
            // object를 알 수 있는 메서드는 아래와 같다.
            // 1. bindingResult.getObjectName();
            // 2. bindingResult.getTarget();
            bindingResult.rejectValue("itemName", "required");
            // bindingResult.rejectValue 안쪽에서는 MessageCodesResolver가 사용된다.
            // 동작 방식 확인은 MessageCodesResolverTest.java 파일을 참조하자.
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 10000000) {
            bindingResult.rejectValue("price", "range", new Object[]{1000, 10000000}, null);
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        // 특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.rejectValue("totalPriceMin", "max", new Object[]{10000, resultPrice}, null);
            }
        }

        // 검증에 실패하면 다시 등록 폼(add form)으로
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult); // bindingResult는 Model에 따로 담지 않아도 view에 전달된다.
//            model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }



    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

