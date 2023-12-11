package hello.upload.file;

import hello.upload.domain.UploadFile;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileStore {

  @Value("${file.dir}")
  private String fileDir;

  public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
    if (multipartFile.isEmpty()) {
      return null;
    }

    String originalFilename = multipartFile.getOriginalFilename(); // note: 사용자가 업로드한 파일 이름
    String storeFileName = createStoreFileName(originalFilename);
    multipartFile.transferTo(new File(getFullPath(storeFileName)));
    return new UploadFile(originalFilename, storeFileName);
  }

  public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) {
    return multipartFiles.stream()
        .map((multipartFile) -> {
          try {
            return storeFile(multipartFile);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        })
        .collect(Collectors.toList());
  }

  public String getFullPath(String filename) {
    return fileDir + filename;
  }

  private String createStoreFileName(String originalFilename) {
    String ext = extractExt(originalFilename);
    String uuid = UUID.randomUUID().toString();
    return uuid + "." + ext;
  }

  private String extractExt(String originalFilename) {
    int pos = originalFilename.lastIndexOf(".");
    return originalFilename.substring(pos + 1);
  }
}
