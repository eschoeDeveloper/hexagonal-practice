# hexagonal-practice

Spring Boot 4 기반 헥사고널 아키텍처 연습 프로젝트. 사용자 관리 / 장바구니 / 주문 / 발송 도메인으로 구성.

## 구조

```
io.github.eschoe.hexagonal
├── common                        글로벌 인프라 (config, web, exception, actuator)
├── user    ├── domain            값 객체/엔티티/규칙
│           ├── application       ├── port.in  (유스케이스)
│           │                     └── port.out (외부 포트)
│           │                     └── service  (유스케이스 구현)
│           └── adapter           ├── in.web        (REST)
│                                 └── out.persistence (MyBatis)
├── cart    (동일 구조)
└── order   (+ adapter.out.cart  Cart 컨텍스트 게이트웨이)
```

경계는 `HexagonalArchitectureTest`(ArchUnit)가 강제:
- `domain`은 `application`/`adapter`/Spring 참조 금지
- `application`은 `adapter` 참조 금지
- `*Service`/`*Controller`/`*PersistenceAdapter` 위치 고정

## 실행

### 로컬 (PostgreSQL 직접)

```bash
# 기본 profile=local (localhost:5432/hexagonal, postgres/postgres)
./gradlew bootRun
```

### Docker Compose (앱 + Postgres)

```bash
docker compose up --build
```

- 앱: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Actuator: http://localhost:8080/actuator/{health,info,metrics}

### 프로파일 / 환경변수

| Profile | 용도 | 주요 설정 |
|---------|------|-----------|
| `local` | 로컬 개발 | `application-local.properties` 값 사용 |
| `prod`  | 배포 | `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `DB_POOL_MAX`, `DB_POOL_MIN` 환경변수 |
| `test`  | 테스트 | Testcontainers PostgreSQL + Flyway |

## DB 마이그레이션 (Flyway)

- 마이그레이션 위치: `src/main/resources/db/migration/V*.sql`
- 앱 기동 시 자동 적용 (`spring.flyway.enabled=true`)
- 변경 시 `V2__...`, `V3__...` 형태로 추가

## 주요 API

| Method | Path | 설명 |
|--------|------|------|
| POST   | `/api/users` | 회원 가입 |
| GET    | `/api/users/{id}` | 회원 조회 |
| PATCH  | `/api/users/{id}` | 이름 변경 |
| DELETE | `/api/users/{id}` | 회원 삭제 |
| GET    | `/api/users/{userId}/cart` | 장바구니 조회 |
| POST   | `/api/users/{userId}/cart/items` | 아이템 추가 |
| PATCH  | `/api/users/{userId}/cart/items/{productId}` | 수량 변경 |
| DELETE | `/api/users/{userId}/cart/items/{productId}` | 아이템 삭제 |
| POST   | `/api/users/{userId}/orders` | 장바구니로 주문 생성 |
| GET    | `/api/users/{userId}/orders` | 사용자 주문 목록 |
| GET    | `/api/orders/{id}` | 주문 상세 |
| POST   | `/api/orders/{id}/cancel` | 주문 취소 |
| POST   | `/api/orders/{id}/dispatch` | 발송 시작 (PAID + DISPATCHED) |
| POST   | `/api/orders/{id}/deliver` | 배송 완료 (COMPLETED + DELIVERED) |
| GET    | `/api/orders/{id}/shipment` | 발송 조회 |

에러 응답은 RFC 7807 `ProblemDetail` 포맷. Bean Validation 실패 시 `violations` 필드에 필드별 메시지.

## 테스트

```bash
./gradlew test                      # 전체 (Testcontainers 통합 테스트는 Docker 필요)
./gradlew test --tests "*Test"      # 단위 + 아키 테스트만
```

- 단위 테스트: 도메인/서비스/값 객체
- 아키 테스트: `HexagonalArchitectureTest` (ArchUnit)
- 통합 테스트(`*IT`): Testcontainers PostgreSQL + MyBatis 어댑터 + E2E REST 플로우

## 관찰성

- 요청마다 `X-Correlation-Id` 헤더 발급 + MDC 기록
- 로그 패턴에 `correlationId` 포함 (`logback-spring.xml`)
- Actuator `/actuator/info`에 `AppProperties` 값 노출
