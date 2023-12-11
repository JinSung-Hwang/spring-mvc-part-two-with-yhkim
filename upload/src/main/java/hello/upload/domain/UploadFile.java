package hello.upload.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFile {
  private String uploadFileName;
  private String storeFileName; // note: A유저와 B유저가 같은 파일명으로 업로드하면 덮어씌워지기 때문에 UUID를 이용해서 storeFileName을 만들어준다.
}
