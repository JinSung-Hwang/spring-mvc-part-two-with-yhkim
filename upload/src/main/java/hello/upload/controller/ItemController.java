package hello.upload.controller;

import hello.upload.domain.Item;
import hello.upload.domain.ItemRepository;
import hello.upload.domain.UploadFile;
import hello.upload.file.FileStore;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

  private final ItemRepository itemRepository;
  private final FileStore fileStore;

  // note: 파일 업로드하는 화면을 보여주는 API
  @GetMapping("/items/new")
  public String newItem(@ModelAttribute ItemForm form) {
    return "item-form";
  }

  // note: 파일 업로드하는 API
  @PostMapping("/items/new")
  public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
    // note: 파일 저장, 실무에서는 S3같은 곳에 따로 저장한다.
    UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
    List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

    // note: 데이터베이스에 file 이름 저장
    Item item = new Item();
    item.setItemName(form.getItemName());
    item.setAttachFile(attachFile);
    item.setImageFiles(storeImageFiles);
    itemRepository.save(item);

    redirectAttributes.addAttribute("itemId", item.getId());
    return "redirect:/items/{itemId}";
  }

  // note: 상품 상세 화면을 보여주는 API
  @GetMapping("/items/{id}")
  public String items(@PathVariable Long id, Model model) {
    Item item = itemRepository.findById(id);
    model.addAttribute("item", item);
    return "item-view";
  }

  // note: 상품 상세 화면에서 img 태그에 src로 보여주는 API
  @ResponseBody
  @GetMapping("/images/{filename}")
  public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
    return new UrlResource("file:" + fileStore.getFullPath(filename)); // note: 이 코드는  "file:/Users/.../upload-test/uuid.png" 의 위치의 파일을 찾아온다.
  }

  // note: 파일을 다운로드하는 API
  @GetMapping("/attach/{itemId}")
  public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId)
      throws MalformedURLException, UnsupportedEncodingException {
    Item item = itemRepository.findById(itemId);
    String storeFileName = item.getAttachFile().getStoreFileName();
    String uploadFileName = item.getAttachFile().getUploadFileName();

    UrlResource urlResource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));
    log.info("uploadFileName={}", uploadFileName);

    String encodedUploadFileName = new String(uploadFileName.getBytes("UTF-8"), "ISO-8859-1"); // note: 한글 파일명이 깨지지 않도록 인코딩한다.
    String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\""; // note: 파일 다운로드 해더 규약이다. Content-Disposition 헤더에 attachment를 설정하면 파일이 다운로드 된다.
    return ResponseEntity.ok()
//        .header("Content-Disposition", "attachment; filename=\"" + uploadFileName + "\"") // note: 파일 다운로드 해더 규약이다. Content-Disposition 헤더에 attachment를 설정하면 파일이 다운로드 된다.
        .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition) // note: 파일 다운로드 해더 규약이다. Content-Disposition 헤더에 attachment를 설정하면 파일이 다운로드 된다.
        .body(urlResource);
  }
}
