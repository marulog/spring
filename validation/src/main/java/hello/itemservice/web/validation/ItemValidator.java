package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
        //clazz가 Item객체이거나 하위 타입일 경우 true 반환
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item)target;

        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){ // 글자가 없으면
            errors.rejectValue("itemName", "required");

        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }

        if(item.getQuantity() == null || item.getQuantity() >= 9999){
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        };
    }
}
