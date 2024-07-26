# <p style="color:white">🥔 Team.삶은감자
<hr>
<h3><p style="color:white">자영업자들과 인플루언서를 주대상으로 한 매장 홍보 플랫폼 구축
<h4 style="float: right">(주)소프트랩스</h4><a href="https://docs.google.com/spreadsheets/d/1DfgU2MyBw00vrAqEvihFkZCQ3IQRXy57VRqSmb1vDM4/edit?gid=1085621931#gid=1085621931">RFP 3번 프로젝트</a>
<br>
<br>
<hr>

##  <p style="color:white">🎒 아키텍쳐</p>
<hr>

<hr>


##  <p style="color:white">🎒 CI/CD</p>
<hr>

<hr>

##  <p style="color:white">👩🏻‍💻 개발 기획</p>
<hr>

<p>

<h3><p style="color:white">⚒️ 기술 Stack </p></h3>
- Spring Boot, Security, JPA
- MySQL
- Redis
- AWS, Docker
- Github, GithubAction
- Discord, Slack, Notion


<hr>
<h3><p style="color:white">🔎 패키지 구조 </p></h3>

- **main (DDD 기법)**
  - *domain*
    - test1(도메인명)
      - controller
      - service
      - model
        - request
        - response
        - type
        - entity
      - repository
      - exception
    - test2(도메인명)
  - *global*
    - config
    - exception
    - model
    - util
    - api
    - validation

<hr>

<h3><p style="color:white">💬  코드 컨벤션 </p></h3>

- 카멜 표기법
- Entity 생성시에 → Entity X → 명사
- DTO class -> Record class
    - request, response → **Request.java, **Response.java
- Controller, Service 매서드명 
  - Controller
    - 예시: getUserById, createUser, updateUser, deleteUser 등
  - Service
    - 예시: processOrder, calculateTotalPrice, cancelReservation 등

<hr>

<h3><p style="color:white">📜 Git Branch</p></h3>

- **main (최종본)**
- **release (배포)**
- **develop (통합)**
- **feature/... (기능들)**
- **refactor/... (수정 브랜치)**
- 예시 → feature/trip - feature/trip/discord

<h3><p style="color:white">📜 Git commit 컨벤션</p></h3>

- **feat : 새로운 기능 추가**
- **fix : 버그 수정**
- **docs : 문서 수정**
- **style : 코드 formatting, 세미콜론(;) 누락, 코드 변경이 없는 경우**
- **refactor : 코드 리팩터링**
- **test : 테스트 코드, 리팩터링 테스트 코드 추가(프로덕션 코드 변경 X)**
- **chore : 빌드 업무 수정, 패키지 매니저 수정(프로덕션 코드 변경 X)**
- **comment : 필요한 주석 추가 및 변경**
- **rename : 파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우**
- **remove : 파일을 삭제하는 작업만 수행한 경우**
- **!BREAKING CHANGE : 커다란 API 변경의 경우**
- **!HOTFIX : 급하게 치명적인 버그를 고쳐야 하는 경우**

<hr>

<h3><p style="color:white">🚇 URL 컨벤션</p></h3>

- **/api 공통적으로 들어가고 다음으로는 권한 그 다음으로는 API 명세서에 따른 URL**
- **/api/customer/..**
- **/api/influence/..**

<hr>


<h3><p style="color:white">📑 코드 구조</p></h3>
- Entity ←→ DTO

```jsx
public record TestRequest(
        String name,
        String title
) {

    public static TestRequest from(TestEntity entity) {
        return new TestRequest(
                entity.getName(),
                entity.getTitle()
        );
    }

}

```

```jsx
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String name;

    public static TestEntity from(TestRequest request) {
        return TestEntity.builder()
                .title(request.title())
                .name(request.name())
                .build();
    }

}

```

- Exception handling

```jsx
public interface ErrorCode {

    HttpStatus getStatus();

    String getMsg();

}

```

```jsx
@RequiredArgsConstructor
public abstract class GlobalException extends RuntimeException {

    @Getter
    private final ErrorCode errorCode;

    public abstract void exceptionHandling();

    public GlobalResponse getErrorResponse() {
        return new GlobalResponse(
                errorCode.getMsg(),
                errorCode.getStatus()
        );
    }

}

```

```jsx
public record GlobalResponse(
        String msg,
        HttpStatus status
) {
}

```

```jsx
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity errorResponse(
            GlobalException ex
    ) {
        ex.exceptionHandling();
        return API.ERROR(ex);
    }

}

```

- API 응답

```jsx
@Data
@AllArgsConstructor
@NoArgsConstructor
public class API {

    public static ResponseEntity OK(Object data) {
        return ResponseEntity.ok(data);
    }

    public static ResponseEntity OK() {
        return ResponseEntity.ok(new GlobalResponse("성공", HttpStatus.OK));
    }

    public static ResponseEntity ERROR(GlobalException ex) {
        return ResponseEntity.status(ex.getErrorCode().getStatus())
                .body(ex.getErrorResponse());
    }

}

```

- validation - 기존 라이브러리에 원하는 validation 존재하지 않을 경우 커스텀 어노테이션 대체
  - 커스텀 어노테이션

      ```jsx
      @Target({ElementType.PARAMETER, ElementType.FIELD})
      @Retention(RetentionPolicy.RUNTIME)
      @Constraint(validatedBy = TestValidator.class)
      public @interface TestValidation {
      
          String message() default "Invalid category";
      
          Class[] groups() default {};
      
          Class[] payload() default {};
      
      }
      
      ```

      ```jsx
      public class TestValidator implements ConstraintValidator<TestValidation, String> {
      
          @Override
          public void initialize(TestValidation constraintAnnotation) {
              ConstraintValidator.super.initialize(constraintAnnotation);
          }
      
          @Override
          public boolean isValid(String arg, ConstraintValidatorContext constraintValidatorContext) {
              return true;
          }
      
      }
      ```

  - 기존 라이브러리

      ```jsx
      @RestController
      public class TestController {
      
          @PostMapping
          public ResponseEntity test(
                  @RequestBody @Valid TestRequest request
          ) {
              return API.OK();
          }
      
      }
      ```

      ```jsx
      public record TestRequest(
              @Length(max = 1, message = "내가 만든 에러")
              String name
      ) {
      }
      
      ```
