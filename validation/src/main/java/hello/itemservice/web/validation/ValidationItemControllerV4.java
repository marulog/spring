package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationItemControllerV4 { // 생성자 주입

    private final ItemRepository itemRepository;


    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v4/addForm";
    }

//    @PostMapping("/add")
//    public String addItem(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
//
//        //특정 필드 에러가 아닌 글로벌 에러
//        if(item.getPrice() != null && item.getQuantity() != null ){
//            int resultPrice = item.getPrice() * item.getQuantity();
//            if(resultPrice < 10000) {
//                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
//            }
//        }
//
//        //검증에 실패하면 다시 입력 폼으로
//        if(bindingResult.hasErrors()) {
//            log.info("errors = {}", bindingResult);
//            return "validation/v4/addForm";
//        }
//
//        //성공 로직
//        Item savedItem = itemRepository.save(item);
//        redirectAttributes.addAttribute("itemId", savedItem.getId());
//        redirectAttributes.addAttribute("status", true);
//        return "redirect:/validation/v4/items/{itemId}";
//    }


    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // ItemSavedForm form = new ItemSaveForm();
        // 요청 파라미터를 보고 같은 이름의 필드를 찾아 setter 호출
        // itemName=노트북&&price=20000 -> form.setItemName("노트북")
        // 그 이후 해당 객체를 모델에 자동 등록해줌
        // 원래 model.addAttribute("itemSaveForm, form); 이렇게 담김
        // 근데 우린 item 모델 그렇게 넣을거야~
        // 요청 파라미터 자체는 itemSaveForm에 저장되는데 "item"이라는 이름으로 모델에 저장할거야

        //특정 필드 에러가 아닌 글로벌 에러
        if(form.getPrice() != null && form.getQuantity() != null ){
            int resultPrice = form.getPrice() * form.getQuantity();
            if(resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "validation/v4/addForm";
        }

        // 성공로직
        //우리는 itemSaveForm이라는 form 받아서 html로 넘길거라서 다시 만듬
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/editForm";
    }

//    @PostMapping("/{itemId}/edit")
//    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {
//
//        //특정 필드 에러가 아닌 글로벌 에러
//        if(item.getPrice() != null && item.getQuantity() != null ){
//            int resultPrice = item.getPrice() * item.getQuantity();
//            if(resultPrice < 10000) {
//                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
//            }
//        }
//
//        if(bindingResult.hasErrors()){
//            log.info("arrors={}", bindingResult);
//            return "validation/v4/editForm";
//        }
//
//        itemRepository.update(itemId, item);
//        return "redirect:/validation/v4/items/{itemId}";
//    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) {

        //특정 필드 에러가 아닌 글로벌 에러
        if(form.getPrice() != null && form.getQuantity() != null ){
            int resultPrice = form.getPrice() * form.getQuantity();
            if(resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        if(bindingResult.hasErrors()){
            log.info("arrors={}", bindingResult);
            return "validation/v4/editForm";
        }
        // update(id, param)
        Item itemParam = new Item();
        itemParam.setItemName(form.getItemName());
        itemParam.setPrice(form.getPrice());
        itemParam.setQuantity(form.getQuantity());


        itemRepository.update(itemId, itemParam);
        return "redirect:/validation/v4/items/{itemId}";
    }

}

