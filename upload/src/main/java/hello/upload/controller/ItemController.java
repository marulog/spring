package hello.upload.controller;

import hello.upload.domain.Item;
import hello.upload.domain.ItemRepository;
import hello.upload.domain.UploadFile;
import hello.upload.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm form){
        return "item-form";
    }

    @PostMapping("/items/new") // 업로드된 폼이 ItemForm에 바인딩됨
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

        //데이터베이스(레포지토리)에 저장
        // 파일 자체가 아닌 파일 이름 정보만 저장함
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);
        itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", item.getId());
        return "redirect:/items/{itemId}";

    }

    @GetMapping("items/{id}")
    public String items(@PathVariable Long id, Model model){
        Item item = itemRepository.findById(id);
        model.addAttribute("item", item);
        return "item-view";
    }
// item 객체 안
//    itemName = "MacBook"
//    attachFile = UploadFile("manual.pdf", "a9d21d2b-1234.pdf")
//    imageFiles = [
//    UploadFile("front.png", "4c9d3e6b-aaaa.png"),
//    UploadFile("back.png", "8a2f123c-bbbb.png")
//]


    @ResponseBody
    @GetMapping("/images/{fileName}")
    public Resource downloadImage(@PathVariable String fileName) throws MalformedURLException {
       // 123312-12319-12831.png -> fileStore.getFullPath -> C:\Users\a\Desktop\spring\file\12313.png상태로됨
        return new UrlResource("file:" + fileStore.getFullPath(fileName));
    }

    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
        Item item = itemRepository.findById(itemId);
        String storeFileName = item.getAttachFile().getStoreFileName(); // "c4a1e2b3-91f3-4d1f-bb2f-abc123.png"
        String uploadFileName = item.getAttachFile().getUploadFileName();
        // uploadFile를 반환함 -> uploadFilename 사용자가 업로드한, storeFileName 서버에 저장된 변경된값이 이 있음
        // 거기서 서버에 저장된 이름을 찾고 싶음 -> .getStoreFileName

        // 실제 서버에 저장된 경로 + file: -> "file:C:/Users/a/Desktop/spring/file/c4a1e2b3-91f3-4d1f-bb2f-abc123.png"
        // 로컬 파일을 가리키는 URL로 스프링이 내부적으로 file: 스트림을 해석해서 실제 파일에 접근함
        UrlResource urlResource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));
        log.info("uploadFileName={}", uploadFileName);
        //new UrlResource("file:C:/Users/a/Desktop/spring/file/abc123.png")
        // 브라우저가 GET /images/abc123.png 요청을 하면 그 파일의 바이트 데이터를 읽어서 HTTP 응답
        // 브라우저는 해당 응답을 이미지로 표시함

        //브라우저가 읽어서 보여준느게 아닌 다운로드를 하기위한 코드 -> 하나의 규약임
        // 이 헤더는 HTTP 응답 헤더 중 하나로 브라우저가 파일을 어떻게 처리할지 지시할 수 있음
        // attachment -> 다운로드 용이다
        // fileanem -> 다운로드할 때 기본으로 표시할 파일 이름 지정
        String encodedUpLoadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8); // 한글 깨짐 문제 방지
        String contentDisposition = "attachment; filename=\"" + encodedUpLoadFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }
}
