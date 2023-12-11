package hello.upload.controller;

import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ItemForm {
  private Long ItemId;
  private String itemName;
  private MultipartFile attachFile; // note: 단일 파일일 때는 attachFile로 받는다.
  private List<MultipartFile> imageFiles; // note: 다중 파일일 때는 imageFiles로 받는다.
}
