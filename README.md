# 나홀로 집콕

[앱 개발 프로젝트] 나 홀로 집콕 (2020.12 ~ 2021.02)

### 1. 소개

- 자가격리 챌린지 격려 어플
- 집에 있는 시간 타이머를 통해 기록
- [다운로드 링크](https://apps.apple.com/qa/app/%EB%82%98%ED%99%80%EB%A1%9C%EC%A7%91%EC%BD%95/id1551471700)

### 2. 기능

- 타이머
  - 초단위로 시간이 갱신되는 타이머
- 위치
  - 위 경도 통해 현재 집에 있는지 여부 확인
- 푸쉬알람
  - Fcm 서비스 사용해 알람허용을 한 회원에게 매 시간 푸쉬메세지 전송
- 회원
  - 카카오 로그인
  - 애플 로그인

### 3. 기술

- Spring boot, Jpql, Nginx, EC2, FCM

### 4. 학습

- #### 처음으로 스프링 서버 구조 파악
  - https://start.spring.io/ 활용 프로젝트 구조 파악
  - 주요 라이브러리 파악
- #### EC2 문제
  - EC2 서버 운영 중 원인모를 이유로 서버 다운 현상 발생
  - CPU 사용량이 급격하게 오르는 것을 보고 구글링 결과 메모리의 부족 현상이라는 것을 파악
  - EC2 모니터링 기능 활용과 Log를 볼 수 있게 됨
- #### git 활용
  - 협업을 위해 git 기능을 학습
  - branch를 이용해 병렬적으로 개발
  - conflict시 유연하게 처리할 수 있게 됨
- #### static 파일 통한 웹뷰 사용
  - nginx를 이용하여 정적 파일 제공
- #### 자바 진영 ORM 표준 기술 JPA 사용
  - SQL을 추상화한 JPQL 이라는 객체 지향 쿼리 언어 사용
  - JPQL은 엔티티 객체를 대상으로 쿼리
  - 영속성 컨텍스트의 기능과 역할 장점에 대한 이해

### 5. API 스펙

| Method  | URI                               | Description                        |
| :-----: | :-------------------------------- | :--------------------------------- |
|  `GET`  | /login/jwt                        | 자동 로그인 API                    |
| `POST`  | /users                            | 회원가입 API                       |
| `POST`  | /login/kakao                      | 자동 로그인 API                    |
|  `GET`  | /users/:userIdx                   | 회원 정보 조회API                  |
| `PATCH` | /users/:userIdx                   | 회원 정보 수정API                  |
| `POST`  | /login/apple                      | 애플 로그인 API                    |
|  `GET`  | /users/recently-locations         | 회원 최근 위치정보 리스트 조회 API |
|  `GET`  | /push-alarms                      | push알림 전송 API                  |
|  `GET`  | /users/challenge-times            | 회원 시간 조회 API                 |
|  `GET`  | /users/locations                  | 회원 현재 위치 정보조회 API        |
| `POST`  | /users/challenge-times            | 회원 시간 등록 API                 |
| `POST`  | /users/tokens                     | 기기토큰 등록 API                  |
| `POST`  | /challenges/:challengeIdx/success | 챌린지 성공 API                    |
| `POST`  | /challenges/:challengeIdx/fail    | 챌린지 실패 API                    |

### 6. UI

![KakaoTalk_20210203_224414740](https://user-images.githubusercontent.com/59005171/109386998-e1057b00-7941-11eb-9bda-a8e52b54fb2c.jpg)
![KakaoTalk_20210203_224414901](https://user-images.githubusercontent.com/59005171/109386957-a996ce80-7941-11eb-8328-dd7adae5c95f.jpg)
![KakaoTalk_20210203_222819910](https://user-images.githubusercontent.com/59005171/109386996-dfd44e00-7941-11eb-8977-c1f0ae06c920.jpg)
![KakaoTalk_20210203_224415021](https://user-images.githubusercontent.com/59005171/109386992-dba83080-7941-11eb-90e8-e7a6f79d815e.jpg)
