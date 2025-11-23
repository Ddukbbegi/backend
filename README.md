# 🥘 뚝배기 민족

## 📋 개요
> 배달의 민족을 오마주하여 제작한 음식 배달 플랫폼 "뚝배기 민족" 프로젝트입니다.
가게, 메뉴, 옵션 메뉴, 주문, 리뷰 등 배달 앱의 주요 기능을 구현했습니다.

***

## 🛠 사용 기술 스택
| 분류                 | 사용된 기술                                           |
|--------------------|--------------------------------------------------|
| **Language**       | Java 17                                          |
| **Framework**      | Spring Boot 3.4.4                                |
| **Build Tool**     | Gradle                                           |
| **Database**       | H2(테스트), MySQL                                        |
| **ORM**            | Spring Data JPA                                  |
| **Validation**     | Jakarta Bean Validation |
| **Authentication** | JWT, OAuth2 (Google)                             |
| **Security**       | Spring Security                                  |
| **Cache**          | Redis                                            |
| **Lombok**         | Getter/Setter, Constructor, Builder 자동 생성        |
| **Test**           | Spring Boot Starter Test (JUnit 기반), Testcontainers, RestAssured |
| **IDE**            | IntelliJ IDEA                                    |
| **협업 도구**          | Notion, erdcloud, figma 등                        |
| **Infrastructure** | Docker, Docker Compose, Redis                   |

***

## 🚀 실행 방법

### 사전 요구사항
- Java 17 이상
- MySQL 8.0 이상
- Redis
- Gradle (또는 Gradle Wrapper 사용)

### 1. 데이터베이스 설정
```sql
CREATE DATABASE ddukbbegi CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Redis 실행
```bash
# Docker를 사용하는 경우
docker-compose up -d redis

# 또는 로컬 Redis 실행
redis-server
```

### 3. 환경 설정
`src/main/resources/application-local.yml` 파일을 수정하여 데이터베이스 및 Redis 설정을 변경합니다.

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ddukbbegi
    username: [YOUR_USERNAME]
    password: [YOUR_PASSWORD]
  data:
    redis:
      host: localhost
      port: 6379
```

### 4. JWT Secret Key 설정
`application-local.yml`에서 JWT Secret Key를 설정합니다.

### 5. OAuth2 설정 (선택사항)
Google OAuth2를 사용하려면 `application.yml`에서 클라이언트 ID와 Secret을 설정합니다.

### 6. 애플리케이션 실행
```bash
# Gradle Wrapper 사용
./gradlew bootRun

# 또는
./gradlew build
java -jar build/libs/ddukbbegi-0.0.1-SNAPSHOT.jar
```

애플리케이션은 기본적으로 `http://localhost:8080`에서 실행됩니다.


***

## 📁 프로젝트 구조

```
src/main/java/com/ddukbbegi/
├── api/                          # API 레이어
│   ├── auth/                     # 인증/인가
│   │   ├── controller/          # AuthController
│   │   ├── dto/                 # 요청/응답 DTO
│   │   └── service/             # 인증 서비스
│   ├── menu/                     # 메뉴 관리
│   │   ├── controller/          # MenuController, OptionController
│   │   ├── entity/              # Menu, Option 엔티티
│   │   └── service/             # 메뉴 서비스
│   ├── order/                    # 주문 관리
│   │   ├── controller/          # OrderController, AdminOrderController
│   │   ├── entity/              # Order, OrderMenu 엔티티
│   │   └── service/             # 주문 서비스
│   ├── point/                    # 포인트 관리
│   ├── review/                   # 리뷰 관리
│   ├── store/                    # 가게 관리
│   │   ├── controller/          # StoreController, StoreOwnerController
│   │   ├── scheduler/           # 가게 상태 스케줄러
│   │   └── service/             # 가게 서비스
│   └── user/                     # 사용자 관리
│       ├── controller/          # UserController
│       ├── entity/              # User, OAuth2 관련 엔티티
│       └── service/             # 사용자 서비스
├── common/                       # 공통 모듈
│   ├── component/               # 공통 응답 컴포넌트
│   ├── config/                  # 설정 클래스
│   │   ├── SecurityConfig       # Spring Security 설정
│   │   ├── RedisConfig          # Redis 설정
│   │   └── JpaConfig            # JPA 설정
│   ├── exception/               # 예외 처리
│   ├── jwt/                     # JWT 유틸리티
│   └── oauth/                   # OAuth2 핸들러
└── DdukbbegiApplication.java    # 메인 애플리케이션
```

***

## 🎯 주요 기능

### 1. 인증/인가
- 회원가입, 로그인 (JWT 기반)
- OAuth2 (Google) 소셜 로그인
- JWT 토큰 재발급
- 로그아웃 (Redis를 이용한 토큰 블랙리스트 관리)

### 2. 사용자 관리
- 사용자 정보 조회/수정
- 이메일, 이름, 전화번호 변경
- 비밀번호 변경

### 3. 가게 관리
- 가게 목록 조회 (일반 사용자)
- 가게 상세 정보 조회
- 가게 등록/수정 (사장님)
- 가게 운영 정보 관리
- 가게 영업 상태 관리 (영업 중, 준비 중, 휴무 등)
- 스케줄러를 통한 자동 영업 상태 변경

### 4. 메뉴 관리
- 메뉴 목록 조회
- 메뉴 상세 정보 조회
- 메뉴 등록/수정/삭제 (사장님)
- 옵션 메뉴 관리

### 5. 주문 관리
- 주문 생성
- 주문 목록 조회
- 주문 취소
- 사장님 주문 관리
- 관리자 주문 통계

### 6. 리뷰 관리
- 리뷰 작성/수정/삭제
- 리뷰 목록 조회
- 리뷰 좋아요
- 사장님 리뷰 답변
- 가게별 평점 통계

### 7. 포인트 관리
- 포인트 조회
- 포인트 사용 내역 조회

***

## 📡 주요 API 엔드포인트

### 인증
- `POST /api/auth/signup` - 회원가입
- `POST /api/auth/login` - 로그인
- `POST /api/auth/logout` - 로그아웃
- `POST /api/auth/reissue` - 토큰 재발급

### 사용자
- `GET /api/users/me` - 내 정보 조회
- `GET /api/users/{userId}` - 사용자 정보 조회
- `PATCH /api/users/me/email` - 이메일 변경
- `PATCH /api/users/me/name` - 이름 변경
- `PATCH /api/users/me/phone` - 전화번호 변경
- `PATCH /api/users/changePassword` - 비밀번호 변경
- `DELETE /api/users` - 회원 탈퇴

### 가게
- `GET /api/stores` - 가게 목록 조회
- `GET /api/stores/{storeId}` - 가게 상세 조회
- `POST /api/owner/stores` - 가게 등록 (사장님)
- `GET /api/owner/stores` - 내 가게 목록 (사장님)
- `PATCH /api/owner/stores/{storeId}/basic-info` - 가게 기본 정보 수정
- `PATCH /api/owner/stores/{storeId}/operation-info` - 운영 정보 수정
- `PATCH /api/owner/stores/{storeId}/temporarily-close` - 임시 휴무
- `PATCH /api/owner/stores/{storeId}/permanently-close` - 영구 휴무

### 메뉴
- `GET /api/menus` - 메뉴 목록 조회
- `GET /api/menus/{menuId}` - 메뉴 상세 조회
- `GET /api/owner/menus` - 내 가게 메뉴 목록 (사장님)
- `POST /api/owner/menus` - 메뉴 등록 (사장님)
- `PUT /api/owner/menus/{menuId}` - 메뉴 수정 (사장님)
- `PATCH /api/owner/menus/{menuId}` - 메뉴 상태 변경 (사장님)

### 주문
- `POST /api/orders` - 주문 생성
- `GET /api/orders` - 내 주문 목록 조회
- `GET /api/owner/stores/{storeId}/orders` - 가게 주문 목록 (사장님)
- `PATCH /api/orders/{orderId}/cancel` - 주문 취소
- `PATCH /api/owner/orders/{orderId}` - 주문 상태 변경 (사장님)

### 리뷰
- `POST /api/reviews` - 리뷰 작성
- `GET /api/users/reviews` - 내 리뷰 목록
- `GET /api/stores/{storeId}/reviews` - 가게 리뷰 목록
- `PATCH /api/reviews/{reviewId}` - 리뷰 수정
- `DELETE /api/reviews/{reviewId}` - 리뷰 삭제
- `POST /api/reviews/{reviewId}/likes` - 리뷰 좋아요
- `POST /api/owners/reviews/{reviewId}/reply` - 리뷰 답변 (사장님)

### 포인트
- `GET /api/points` - 포인트 조회
- `GET /api/points/history` - 포인트 사용 내역

***

## 🔒 보안 기능

- JWT 기반 인증/인가
- Spring Security를 통한 엔드포인트 보호
- 비밀번호 BCrypt 암호화
- Redis를 이용한 토큰 블랙리스트 관리
- OAuth2 소셜 로그인 지원
- 사장님 권한 검증 (AOP 기반)

***

## 🧪 테스트

- JUnit 기반 단위 테스트
- Testcontainers를 이용한 통합 테스트
- RestAssured를 이용한 API 테스트
- Spring Security Test를 이용한 인증/인가 테스트

테스트 리포트는 `build/reports/tests/test/index.html`에서 확인할 수 있습니다.

***

## 📝 개발 순서
### 1. [노션 생성](https://www.notion.so/teamsparta/20-1d62dc3ef5148053aa5cd14469fafe83?p=1e32dc3ef5148011b438cb3f4888c01e&pm=s)
### 2. Brainstorming
### 3. MVP 정의
### 4. 🔨 [wireframe 설계](https://www.figma.com/design/IkLKjHDcvBtkaDKLO69DhR/ddukbegi?node-id=0-1&t=YrGKiWKWhyDuf7RH-1)
### 5. 📪 [ERD 설계](https://www.erdcloud.com/d/X5Xzxh5QTnYxzR7jZ)
### 6. ⌚️ [API 명세서 구현 및 S.A 작성](https://www.notion.so/teamsparta/S-A-1dd2dc3ef5148037bf05c4961b647801)

***

## 👨‍💻 팀원 소개
|                                                                팀원                                                                |                                                                 팀원                                                                 |                                                            팀원                                                            |
|:--------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------:|
| <a href="https://github.com/YongLeeCode" target="_blank"><img src="https://github.com/YongLeeCode.png" width="100"/><br/>이용환</a> | <a href="https://github.com/sinyoung0403" target="_blank"><img src="https://github.com/sinyoung0403.png" width="100"/><br/>박신영</a> | <a href="https://github.com/mxcoogi" target="_blank"><img src="https://github.com/mxcoogi.png" width="100"/><br/>김국민</a> |
|    <a href="https://github.com/NCookies" target="_blank"><img src="https://github.com/NCookies.png" width="100"/><br/>유승우</a>    |  <a href="https://github.com/yeonjookang" target="_blank"><img src="https://github.com/yeonjookang.png" width="100"/><br/>강연주</a>  |                                                                                                                          |

***

## 📄 라이선스
이 프로젝트는 교육 목적으로 제작되었습니다.

