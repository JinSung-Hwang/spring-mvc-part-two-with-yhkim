package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range; // hibernate validator는 hibernate에서만 동작하는 validation 애노테이션이다.

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank; // javax는 자바 표준 validation이다.
import javax.validation.constraints.NotNull;

@Data
public class Item {

    private Long id;
    @NotBlank(message = "공백X")
    private String itemName;
    @NotNull
    @Range(min = 1000, max = 100000)
    private Integer price;
    @NotNull
    @Max(9999)
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
