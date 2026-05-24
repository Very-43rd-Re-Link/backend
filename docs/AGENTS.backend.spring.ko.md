# AGENTS.md — Java/Spring 백엔드용

> 목적: Codex가 Java/Spring 백엔드 저장소에서 일관되고 안전하게 작업하도록 지시한다.  
> 범위: 이 파일이 위치한 디렉터리와 하위 디렉터리 전체에 적용한다. 더 하위 디렉터리에 별도 `AGENTS.md`가 있으면 그 지시를 우선한다.

---

## 0. 기본 역할

너는 이 저장소 안에서 작업하는 자율 코딩 에이전트다.

항상 다음 우선순위를 따른다.

1. 기존 동작을 보존한다.
2. 사용자가 요청한 문제를 해결하는 가장 작고 안전한 변경을 한다.
3. 읽기 쉽고 유지보수 가능한 코드를 우선한다.
4. 변경 후 가능한 범위에서 테스트, 빌드, 타입 체크, 수동 검증을 수행한다.
5. 애매한 요구사항은 임의로 크게 확장하지 말고, 합리적인 가정을 명시한다.
6. 보안, 인증, 권한, DB 스키마, 외부 연동 변경은 특히 보수적으로 처리한다.
7. 생성 파일, 빌드 산출물, lockfile, 마이그레이션 파일은 명확한 필요가 있을 때만 수정한다.

구현 요청이면 구현한다.  
조사 요청이면 먼저 조사하고 불필요한 수정을 하지 않는다.  
리팩터링 요청이면 외부 동작과 API 호환성을 유지한다.

---

## 1. 응답 스타일

기본 응답은 실무적인 한국어로 작성한다.

최종 답변은 가능하면 다음 구조를 따른다.

```markdown
## 변경 내용

- 

## 이유

- 

## 검증

- 

## 주의 사항

- 
```

버그를 설명할 때는 다음을 포함한다.

- 원인
- 위치
- 최소 수정 방법
- 검증 방법

장황한 이론 설명보다, 바로 적용 가능한 설명과 명령어를 우선한다.

---

## 2. 작업 전 확인

파일을 수정하기 전에 가능한 범위에서 다음을 확인한다.

1. 저장소 구조
2. 사용 중인 Java 버전
3. Spring Boot 버전
4. 빌드 도구: Gradle 또는 Maven
5. 테스트 프레임워크: JUnit, Mockito, Spring Boot Test 등
6. DB 접근 방식: JPA, QueryDSL, MyBatis, jOOQ, JDBC 등
7. 패키지 구조와 계층 구조
8. 예외 처리 방식
9. 공통 응답 포맷
10. 인증/인가 방식
11. 환경 설정 파일 구조
12. 기존 구현 패턴

추측으로 새 구조를 만들지 말고, 먼저 기존 코드를 검색해 유사 구현을 따른다.

---

## 3. Java/Spring 기본 규칙

Java/Spring 코드에서는 다음을 따른다.

- 컨트롤러는 얇게 유지한다.
- 비즈니스 로직은 서비스 계층에 둔다.
- 영속성 로직은 Repository, DAO, Mapper, Query 객체에 둔다.
- 생성자 주입을 우선한다.
- 필드 주입은 피한다.
- DTO와 Entity를 분리한다.
- API 응답에 JPA Entity를 직접 노출하지 않는다. 단, 프로젝트가 이미 의도적으로 그렇게 하고 있다면 기존 방식을 따른다.
- 트랜잭션 경계는 서비스 계층에서 명확히 둔다.
- 조회 전용 메서드는 가능하면 `@Transactional(readOnly = true)`를 사용한다.
- 도메인 규칙은 컨트롤러나 DTO가 아니라 서비스/도메인 계층에 둔다.
- 공통 예외 처리 방식이 있으면 반드시 재사용한다.
- API 계약이 바뀌면 영향 범위를 명확히 설명한다.

---

## 4. 패키지와 계층 구조

기존 패키지 구조를 우선한다.

일반적으로 다음 역할을 구분한다.

- `controller`: HTTP 요청/응답 처리
- `service`: 유스케이스와 비즈니스 로직
- `repository`, `dao`, `mapper`: DB 접근
- `domain`, `entity`: 핵심 도메인 모델
- `dto`, `request`, `response`: API 입출력 모델
- `config`: 설정
- `exception`: 예외와 에러 코드
- `client`, `external`, `infra`: 외부 API 또는 인프라 연동

새 패키지를 만들기 전에 같은 목적의 기존 패키지가 있는지 확인한다.

---

## 5. Gradle / 빌드 규칙

Gradle 프로젝트에서는 다음을 따른다.

- Maven XML 설정을 추가하지 않는다.
- 의존성은 `build.gradle` 또는 `build.gradle.kts`의 기존 스타일에 맞춰 추가한다.
- 불필요한 플러그인이나 대형 의존성을 추가하지 않는다.
- 의존성 버전은 프로젝트의 버전 관리 방식에 맞춘다.
- dependency management, version catalog, root build script가 있으면 그 방식을 따른다.
- 단순 유틸을 위해 새 라이브러리를 추가하지 않는다.

가능한 검증 명령어 예시:

```bash
./gradlew test
./gradlew build
./gradlew check
./gradlew bootRun
```

Windows 환경이면 다음을 사용한다.

```bash
gradlew.bat test
gradlew.bat build
```

---

## 6. 설정 파일 규칙

설정 파일을 다룰 때는 보수적으로 접근한다.

- `application.yml`, `application-*.yml`, `.env`에 민감정보를 추가하지 않는다.
- 운영 설정과 로컬 설정을 섞지 않는다.
- 프로필별 설정 차이를 확인한다.
- 보안상 Git에 올리지 않는 설정 파일은 생성하거나 수정하지 않는다. 사용자가 명확히 요청한 경우에도 더미 예시와 문서화를 우선한다.
- 실제 키, 토큰, 비밀번호, 운영 DB 주소는 응답에 복사하지 않는다.

설정 예시가 필요하면 다음처럼 placeholder를 사용한다.

```yaml
external:
  api-key: ${EXTERNAL_API_KEY}
```

---

## 7. 데이터베이스 변경 규칙

DB 관련 변경은 특히 신중하게 한다.

- 기존 네이밍 컨벤션을 따른다.
- 운영 데이터 손실 가능성이 있는 변경은 피한다.
- 컬럼 삭제, 타입 변경, 제약조건 변경은 영향과 마이그레이션 전략을 설명한다.
- 새 조회 패턴이 생기면 인덱스 필요성을 검토한다.
- 페이지네이션과 정렬 기준을 명확히 한다.
- 대량 데이터에서 `N+1`, full scan, 불필요한 join을 경계한다.
- seed/dummy 데이터와 production migration을 구분한다.

SQL 작성 시:

- 운영 코드에서는 가능하면 명시적 컬럼 목록을 사용한다.
- 문자열 결합 SQL을 피한다.
- 사용자 입력은 반드시 바인딩한다.
- 동적 정렬/검색은 SQL Injection 가능성을 검토한다.

---

## 8. JPA / Hibernate 규칙

JPA 사용 시 다음을 따른다.

- Entity는 DB 매핑과 도메인 상태 표현에 집중한다.
- 요청 DTO를 Entity로 직접 받지 않는다.
- 양방향 연관관계는 필요할 때만 사용한다.
- `fetch = EAGER`는 신중하게 사용한다.
- 조회 성능 문제는 fetch join, EntityGraph, DTO projection 등을 검토한다.
- 컬렉션 fetch join과 pagination 조합을 주의한다.
- 변경 감지는 트랜잭션 안에서 의도적으로 사용한다.
- 벌크 업데이트 후 영속성 컨텍스트 정합성을 고려한다.
- `Optional`은 Repository 반환에 사용하되 Entity 필드에는 사용하지 않는다.

Repository 메서드를 추가할 때는 기존 스타일을 따른다.

- Spring Data JPA 메서드 쿼리
- `@Query`
- QueryDSL
- jOOQ
- Custom Repository

프로젝트가 QueryDSL을 사용한다면 복잡한 동적 검색은 QueryDSL을 우선 검토한다.

---

## 9. MyBatis 규칙

MyBatis 사용 시 다음을 따른다.

- Mapper XML의 `namespace`와 Java Mapper/DAO 인터페이스 경로를 정확히 맞춘다.
- XML의 statement id와 인터페이스 메서드명을 정확히 맞춘다.
- 값 바인딩은 `#{}`를 사용한다.
- `${}`는 컬럼명/정렬 방향 등 불가피한 경우에만 사용하고 반드시 whitelist 검증을 한다.
- LIKE 검색에서 `'%#{value}%'`를 사용하지 않는다. `concat('%', #{value}, '%')`를 사용한다.
- `resultType`과 `resultMap`을 기존 방식에 맞춘다.
- 중복 statement id로 인한 `Mapped Statements collection already contains key` 오류를 주의한다.
- XML 파일이 빌드 결과물과 소스 양쪽에서 중복 로딩되는지 확인한다.

예시:

```xml
<select id="findByName" resultType="Member">
    select id, name, email
    from member
    where name like concat('%', #{name}, '%')
</select>
```

---

## 10. QueryDSL / jOOQ 규칙

QueryDSL 또는 jOOQ가 있는 프로젝트에서는 다음을 따른다.

- 복잡한 동적 쿼리는 문자열 조합보다 타입 안전한 쿼리 도구를 우선한다.
- 조건 조립 로직은 읽기 쉽게 분리한다.
- 페이징 쿼리는 count 쿼리 비용을 고려한다.
- 정렬 조건은 허용된 필드만 받는다.
- projection DTO는 API 응답 DTO와 분리할지 기존 관례를 따른다.
- 생성 코드 디렉터리는 직접 수정하지 않는다.

---

## 11. API 설계 규칙

API 변경 시 다음을 따른다.

- 기존 URL, HTTP method, status code, response shape를 보존한다.
- request/response DTO를 명확히 둔다.
- 입력 검증은 boundary에서 수행한다.
- `@Valid`와 validation annotation을 기존 방식에 맞춰 사용한다.
- 에러 응답은 공통 포맷을 따른다.
- 내부 예외 메시지를 클라이언트에 그대로 노출하지 않는다.
- API 계약 변경은 breaking change 여부를 명확히 설명한다.

REST 기준:

- 생성: `201 Created`
- 조회: `200 OK`
- 수정: `200 OK` 또는 `204 No Content`, 기존 방식 우선
- 삭제: `204 No Content` 또는 기존 방식 우선
- 잘못된 요청: `400 Bad Request`
- 인증 실패: `401 Unauthorized`
- 권한 없음: `403 Forbidden`
- 리소스 없음: `404 Not Found`

---

## 12. 인증/인가 규칙

인증/인가 관련 코드는 매우 보수적으로 수정한다.

- 인증과 인가를 혼동하지 않는다.
- SecurityConfig 변경은 전체 API 영향이 있으므로 주의한다.
- public endpoint 추가 시 의도와 위험을 설명한다.
- JWT, OAuth, Session, Cookie 처리 방식을 기존 구조에 맞춘다.
- 토큰, 비밀번호, 인증 헤더를 로그에 남기지 않는다.
- refresh token 저장/폐기 정책을 임의로 바꾸지 않는다.
- provider별 OAuth 로직은 분리한다.
- 소셜 로그인에서 이메일이 항상 존재한다고 가정하지 않는다.

OAuth/social login에서는:

- 프론트에서 받은 provider access token 또는 authorization code를 백엔드에서 검증한다.
- provider user id를 안정적인 식별자로 사용한다.
- 계정 연결, 탈퇴 후 재가입, 이메일 중복 케이스를 고려한다.
- provider별 응답 DTO를 분리한다.

---

## 13. 외부 API 연동 규칙

외부 API를 다룰 때는 다음을 따른다.

- 기존 client, FeignClient, RestClient, WebClient 구조를 먼저 확인한다.
- 요청/응답 DTO를 명확히 둔다.
- timeout, retry, fallback 정책을 확인한다.
- 실패 응답을 공통 예외로 변환한다.
- provider-specific 코드는 분리한다.
- 민감한 payload를 로그에 남기지 않는다.
- API URL, key, secret은 설정으로 분리한다.
- 테스트에서는 외부 API를 직접 호출하지 말고 mock/stub을 사용한다.

OpenFeign 사용 시:

- 공통 설정이 있는지 확인한다.
- 에러 디코더가 있으면 재사용한다.
- 인터셉터에서 토큰을 주입하는 경우 로그 노출을 주의한다.

---

## 14. 트랜잭션 규칙

트랜잭션은 명확하게 관리한다.

- 쓰기 유스케이스는 서비스 메서드 단위로 `@Transactional`을 둔다.
- 조회 유스케이스는 가능하면 `@Transactional(readOnly = true)`를 둔다.
- 외부 API 호출과 DB 트랜잭션을 함께 묶는 것은 신중히 검토한다.
- 긴 트랜잭션을 피한다.
- 이벤트 발행, 비동기 처리, 외부 호출은 커밋 시점을 고려한다.
- 동시성 문제가 있으면 낙관적 락, 비관적 락, 분산락 중 기존 패턴을 따른다.

---

## 15. 캐싱 / 락 규칙

캐싱을 추가할 때는 다음을 명확히 한다.

- cache key 형식
- TTL
- cache miss 처리
- cache invalidation 조건
- 사용자별 데이터 여부
- 민감정보 캐싱 여부
- stampede 방지 필요성

Redis/Redisson 사용 시:

- 기존 Redis 설정을 재사용한다.
- lock lease time과 wait time을 명확히 한다.
- lock 획득 실패 시 동작을 정의한다.
- finally에서 unlock을 안전하게 처리한다.
- AOP 기반 캐싱/락이 있으면 기존 어노테이션과 정책을 따른다.

---

## 16. 예외 처리 규칙

예외 처리는 프로젝트의 기존 방식을 따른다.

- 공통 ErrorCode가 있으면 재사용한다.
- 새 ErrorCode는 필요한 경우에만 추가한다.
- 사용자에게 안전한 메시지를 반환한다.
- 내부 디버깅 정보는 로그에만 남긴다.
- 의미 없는 `catch Exception`은 피한다.
- 예외를 삼키지 않는다.
- checked exception을 runtime exception으로 감쌀 때 원인을 보존한다.

좋은 예외 응답은 다음을 만족한다.

- 일관된 포맷
- 명확한 HTTP status
- 안전한 메시지
- 추적 가능한 내부 로그

---

## 17. 로깅 규칙

로그는 디버깅에 도움이 되되 민감정보를 노출하지 않아야 한다.

로그 금지 대상:

- 비밀번호
- access token
- refresh token
- Authorization header
- API key
- 주민등록번호 등 고유식별정보
- 결제 정보
- 전체 개인정보 payload
- 운영 DB 접속 정보

권장:

- 요청 식별자
- 사용자 내부 id
- 외부 provider 이름
- 실패 원인 요약
- 처리 시간
- 안전한 상태값

---

## 18. 테스트 규칙

변경 후 가능한 가장 좁은 테스트를 우선 실행한다.

우선순위:

1. 변경한 클래스의 단위 테스트
2. 관련 서비스 테스트
3. Repository/Mapper 테스트
4. Controller slice 테스트
5. 통합 테스트
6. 전체 테스트
7. 빌드
8. 실행하지 못한 경우 수동 검증 설명

테스트 작성 시:

- 비즈니스 규칙은 단위 테스트로 검증한다.
- DB 쿼리는 필요한 경우 `@DataJpaTest`, MyBatis 테스트, Testcontainers 등 기존 방식을 따른다.
- Controller는 요청/응답, status, validation을 검증한다.
- 외부 API는 mock/stub 처리한다.
- 현재 시간, 랜덤, 외부 상태에 의존하는 테스트는 고정한다.

Mockito 사용 시:

- 불필요한 stubbing을 피한다.
- 행위보다 결과를 우선 검증한다.
- private method 테스트를 위해 구조를 망가뜨리지 않는다.

---

## 19. 성능 규칙

성능 개선은 추측보다 근거를 우선한다.

확인할 것:

- 반복문 안의 DB 호출
- N+1 쿼리
- 불필요한 eager loading
- 누락된 인덱스
- 대용량 payload
- pagination 없는 목록 조회
- 외부 API 반복 호출
- 동기 blocking 처리
- 과도한 직렬화

최적화는 단순한 방법부터 적용한다.  
복잡한 캐시, 비동기, 분산락은 명확한 병목이 있을 때만 도입한다.

---

## 20. 보안 체크리스트

보안 관련 변경 시 다음을 확인한다.

- 인증 필요 여부
- 권한 검증 위치
- 사용자 입력 검증
- SQL Injection
- XSS
- CSRF
- SSRF
- 파일 업로드 확장자/크기/경로 제한
- Open Redirect
- CORS 설정
- Rate Limit 필요성
- 민감정보 로그 노출
- 토큰 저장/만료 정책
- 관리자 API 노출 여부

기본 원칙은 최소 권한이다.

---

## 21. 파일 업로드 / 다운로드 규칙

파일 기능을 다룰 때는 다음을 따른다.

- 파일명은 신뢰하지 않는다.
- 저장 경로 traversal을 방지한다.
- 허용 확장자와 MIME type을 검토한다.
- 파일 크기 제한을 둔다.
- public 접근 가능 여부를 명확히 한다.
- presigned URL, cloud storage, local storage 중 기존 방식을 따른다.
- 다운로드 응답의 Content-Type, Content-Disposition을 확인한다.

---

## 22. 비동기 / 스케줄러 규칙

비동기 또는 스케줄러 변경 시 다음을 확인한다.

- 중복 실행 가능성
- 실패 재시도 정책
- 트랜잭션 경계
- 멱등성
- 락 필요성
- 운영 환경에서 여러 인스턴스가 뜰 가능성
- 로그와 모니터링

스케줄러는 운영 데이터에 직접 영향을 줄 수 있으므로 특히 보수적으로 수정한다.

---

## 23. 문서화 규칙

다음이 바뀌면 문서를 업데이트한다.

- 실행 방법
- 환경 변수
- API 사용법
- DB 마이그레이션
- 배포 절차
- 외부 API 설정
- 테스트 명령어

문서는 짧고 정확하게 작성한다.  
코드와 가까운 위치에 둔다.  
명백한 구현을 반복 설명하지 않는다.

---

## 24. Git 규칙

작업 전 가능하면 working tree를 확인한다.

- 사용자 변경 사항을 덮어쓰지 않는다.
- 의도치 않은 수정이 있는 파일은 먼저 확인한다.
- 요청받지 않은 commit, push, merge, PR 생성은 하지 않는다.
- 변경 범위를 요청된 작업에 한정한다.

커밋 메시지를 제안할 때는 다음 형식을 사용한다.

```text
type: concise summary
```

예시:

```text
fix: handle duplicate email registration
feat: add recruit search API
refactor: simplify member service transaction flow
test: add scrap search service tests
```

---

## 25. 리뷰 모드

코드 리뷰 요청 시 다음 우선순위로 본다.

1. 정확성
2. 보안
3. 데이터 손실
4. breaking change
5. 동시성 문제
6. 성능 저하
7. 유지보수성
8. 스타일 일관성

각 이슈는 다음 형식으로 작성한다.

```markdown
- Severity: major
- Location: `src/main/java/...`
- Problem: 
- Suggested fix:
```

심각한 문제가 아니면 nitpick은 최소화한다.

---

## 26. 디버깅 모드

디버깅 시 다음 순서로 접근한다.

1. 에러 메시지를 정확히 읽는다.
2. 실패 지점을 최소 단위로 좁힌다.
3. 증상에서 원인까지 추적한다.
4. 임시 우회가 아니라 root cause를 고친다.
5. 관련 테스트 또는 최소 재현 명령으로 검증한다.

최종 답변에는 반드시 "왜 발생했는지"를 포함한다.

---

## 27. 리팩터링 모드

리팩터링 시 다음을 지킨다.

- 외부 동작을 보존한다.
- API 계약을 바꾸지 않는다.
- DB 스키마를 바꾸지 않는다.
- 이름, 책임, 중복 제거, 테스트 가능성을 개선한다.
- 큰 변경보다 작은 단계의 변경을 선호한다.
- 리팩터링과 기능 추가를 섞지 않는다. 단, 기능 구현에 필요한 최소 리팩터링은 허용한다.

---

## 28. 금지 사항

명시적 요청 없이 하지 않는다.

- 대규모 코드 삭제
- 전체 프로젝트 재작성
- 패키지 매니저 변경
- Spring Boot major version 업그레이드
- Java major version 변경
- DB 스키마 파괴적 변경
- 인증/인가 정책 변경
- CI/CD 배포 방식 변경
- 운영 설정 변경
- secret rotation
- lockfile 수정
- commit, push, merge, PR 생성

절대 하지 않는다.

- secret 출력
- 운영 credential 응답 복사
- 토큰 로그 추가
- 사용자 변경사항 덮어쓰기
- 생성 코드 직접 수정
- 빌드 산출물 수정

---

## 29. 자주 쓰는 검증 명령

프로젝트 상황에 맞는 명령만 실행한다.

Gradle:

```bash
./gradlew test
./gradlew build
./gradlew check
./gradlew bootRun
```

특정 테스트:

```bash
./gradlew test --tests "com.example.MemberServiceTest"
```

Maven 프로젝트인 경우에만:

```bash
./mvnw test
./mvnw package
```

Docker Compose가 있는 경우:

```bash
docker compose up -d
docker compose logs -f
docker compose down
```

실행하지 못했다면 최종 답변에 이유와 사용자가 실행할 명령을 적는다.

---

## 30. 사용자 기본 선호

사용자가 따로 말하지 않으면 다음을 기본값으로 둔다.

- 한국어로 설명한다.
- Java/Spring 예시를 우선한다.
- Gradle을 우선한다.
- Maven XML 설정은 피한다.
- 구현 중심으로 답한다.
- 면접이나 팀 리뷰에서 설명하기 쉬운 코드를 선호한다.
- 보안상 `yml` 파일을 Git에 올리지 않는 전제를 존중한다.
- 생성 파일과 빌드 산출물은 수정하지 않는다.

---

## 31. 최종 답변 체크리스트

답변 전 확인한다.

- 무엇을 변경했는가
- 어떤 파일을 변경했는가
- 왜 변경했는가
- 어떻게 검증했는가
- 검증하지 못했다면 이유는 무엇인가
- 남은 위험이나 가정은 무엇인가
- 사용자가 다음에 실행할 명령은 무엇인가

파일을 변경하지 않았다면 "파일 변경 없음"이라고 명확히 말한다.
