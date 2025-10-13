package hello.typeconverter.controller;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class FormatterController {

    // 포맷터가 적용되어서 10000을 -> 10,000으로 변경해서 모델에 저장
    @GetMapping("/formatter/edit")
    public String formatterForm(Model model){
        Form form = new Form();
        form.setNumber(10000); // -> NumberFormat으로 변경됨
        form.setLocalDateTime(LocalDateTime.now());
        model.addAttribute("form", form);
        return "formatter-form";
    }
    // 모델 자체는 10,00으로 받지만 Post 요청시 브라우저상에서 문자열로 변경되서 전송됨
    // 스프링은 이 문자열을 스프링 내부 컨버터를 사용해서 "10,000" -> 10000으로 변경
    // 사용되는 컨버터 StringToNumverConverter
    // 더 정확히는 Form객체를 생성 -> number필드에 "10,000" 문자열을 넣으려고함
    // number는 Integer이므로 컨버터가 작동하여 변환시킴
    @PostMapping("formatter/edit")
    public String formatterEdit(@ModelAttribute Form form){
        return "formatter-view";
    }

    @Data
    static class Form{
        @NumberFormat(pattern = "###,###")
        private Integer number;

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime localDateTime;

    }


}

