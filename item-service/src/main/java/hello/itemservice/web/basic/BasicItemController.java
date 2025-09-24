package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController { // basicItemController가 빈으로 등록되면서 의존성 주입

    private final ItemRepository itemRepository;
// -> RequiredArgsConstructor -> final붙은 객체 자동 의존성 주입
//    @Autowired// 생성자로 의존성 주입 -> 지워도됨 생성자가 하나라서 자동 주입
//    public basicItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    // 스프링 3.x버전부터 바이트코드를 파싱해서 매개변수 이름을 추론하려고 시도하지않음
    // 직접 이름을 명시하거나 빌드를 gradle를 선택하여 돌리면 됨
    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

//    @PostMapping("/add")
//    public String addItemV1(@RequestParam("itemName") String itemName,
//                       @RequestParam("price") int price,
//                       @RequestParam("quantity") Integer quantity,
//                       Model model){
//
//        Item item = new Item(itemName, price, quantity);
//        itemRepository.save(item);
//
//        model.addAttribute("item", item);
//
//        return "basic/item";
//    }

//    @PostMapping("/add")
//    public String addItemV2(@ModelAttribute("item") Item item){
//        itemRepository.save(item);
//        return "basic/item";
//    }

//    @PostMapping("/add")
//    public String addItemV3(@ModelAttribute Item item){
//        // 클래스 명의 앞글자를 소문자로 변경하여 모델에 키에 담김
//       //  엥 되는데?
//        itemRepository.save(item);
//        return "basic/item";
//    }
//
//    @PostMapping("/add") // 상품 등록 후 뷰를 보여줌 -> 새로고침 시 중복
//    public String addItemV4(Item item){
//        // 단순타입말고 객체나 우리가 만든 거는 자동으로 Model로 자동등록
//        itemRepository.save(item);
//        return "basic/item";
//    }

//    @PostMapping("/add") // PRG으로 중복 등록 방지
//    public String addItemV5(Item item){
//        itemRepository.save(item);
//        return "redirect:/basic/items/" + item.getId();
//    }

    @PostMapping("/add") //근데 url에 인코딩해서 보내야됨 -> RedirectAttribute
    public String addItemV6(Item item, RedirectAttributes redirectAttributes){
        Item saveditem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveditem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable("itemId") Long itemId, @ModelAttribute Item item){
       itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }

    /**
     *  테스트용 데이터 추가
     */
//    @PostMapping
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 100000, 10));
        itemRepository.save(new Item("itemB", 200000, 20));
    }


}
