김영한의 스프링 MVC 2 활용편을 수강하며 작성한 Code Repository입니다.

## 내용 정리

### 클라이언트 검증, 서버 검증

1. 클라이언트 검증은 조작할 수 있으므로 보안에 취약하다.
2. 서버만으로 검증하면, 즉각적인 고객 사용성이 부족해진다.
3. 둘이 적절히 섞어서 사용하되, 최종적으로는 서버의 검증은 필수이다.
4. API 방식을 사용하면 API 스펙을 잘 정의해서 검증 오류를 API 응답 결과에 잘 남겨주어야한다.

## 세팅

### Encoding 세팅 
1. preference 열기
2. file encodings 선택
    1. global encodings: utf-8 선택
    1. project encodings: utf-8 선택
    1. default encodings for properties files: utf-8 선택
    1. transparent native-to-ascii conversion 체크박스 체크표시
3. apply 클릭
4. ok 클릭
