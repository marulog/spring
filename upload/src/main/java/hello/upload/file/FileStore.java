package hello.upload.file;

import hello.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName){
        return fileDir + fileName;
    }

    //사진 여러개를 저장하는 메소드
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()){
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    // 문서 파일(단일)을 저ㅈ장하는 메소드
    // 업로드된 파일을 서버 디렉토리에 저장하고 그 결과를 UploadFile 객체를 만들어 반환
    // 서버에 저장할 때는 중복된 파일명이 존재하면 안됨
    // -> uuid사용
    // 근데 확장자도 만들고 싶음 -> ext사용
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()){
            return null;
        }

        // 오리지널 파일 =>image
        String originalFilename = multipartFile.getOriginalFilename();

       // 확장자가 포함된 서버에 저장할 파일명 => qqq-wwww.png
        String storeFileName = createStoreFileName(originalFilename);

        // 서버에 파일 저장
        multipartFile.transferTo(new File(getFullPath(storeFileName))); // 실제 파일 저장
        return new UploadFile(originalFilename, storeFileName); // 원본/저장명 함께 반환
    }


    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        // 서버에 저장하는 파일명인데 확장자를 포함하고 싶음
        String uuid = UUID.randomUUID().toString(); // "qeq-qew-1234-qda + .png"
        return  uuid + "." + ext; // qwew-qwe.png
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }


}
