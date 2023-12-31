package hello.upload.controller;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/servlet/v1")
public class ServletUploadControllerV1 {
  @GetMapping("/upload")
  public String newFile() {
    return "upload-form";
  }

  @PostMapping("/upload")
//  public String saveFileV1(MultipartHttpServletRequest request) throws ServletException, IOException { // note: HttpServletRequest 보다 MultipartHttpServletRequest를 사용하는것이 더 편하지만 MultipartFile이 더 편하다.
  public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {
    log.info("request={}", request);

    String itemName = request.getParameter("itemName");
    log.info("itemName={}", itemName);

    Collection<Part> parts = request.getParts();
    log.info("parts={}", parts);

    return "upload-form";
  }
}
