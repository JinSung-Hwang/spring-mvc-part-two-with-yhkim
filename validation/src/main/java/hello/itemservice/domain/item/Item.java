package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range; // hibernate validator는 hibernate에서만 동작하는 validation 애노테이션이다.
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank; // javax는 자바 표준 validation이다.
import javax.validation.constraints.NotNull;

@Data
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "총합이 10000원을 넘게 입력해주세요")
// scriptAssert도 'ScriptAssert.item,ScriptAssert' 에러를 등록해서 erros.properties에 에러 메세지를 등록하고 사용할 수 있다.
// scriptAssert로 약간의 복잡한 validation을 처리할 수 있지만 더 복잡한 validation은 처리하기 힘들다. 그래서 실무에서는 복잡한 validation은 자바로 처리하는것으로 통일한다.
public class Item {

    @NotNull(groups = UpdateCheck.class)
    private Long id;
    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class}, message = "공백X")
    private String itemName;
    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1000, max = 100000, groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;
    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Max(value = 9999, groups = {SaveCheck.class})
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
