package hello.typeconverter.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Slf4j
public class MyNumberFormatter implements Formatter<Number> {
    @Override // 문자 -> 숫자로 변환
    public Number parse(String text, Locale locale) throws ParseException {
       log.info("text={}, locale={}", text, locale);
        //"1,000" -> 1000 미국식
        //"1 000" -> 1000 프랑스식

        NumberFormat format = NumberFormat.getInstance(locale);
        return format.parse(text); //해당 나라에 맞춰서 변환
    }

    @Override // 객체 -> 문자로 변환
    public String print(Number object, Locale locale) {
        log.info("object={}, locale={}", object, locale);
        return NumberFormat.getInstance(locale).format(object);
    }
}
