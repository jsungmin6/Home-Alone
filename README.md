# 나홀로 집콕 

[앱 개발 프로젝트] 나 홀로 집콕

### 1. 소개

- 자가격리 챌린지 격려 어플
- 집에 있는 시간 타이머를 통해 기록

### 2. 기능

- 타이머
  - 초단위로 시간이 갱신되는 타이머 
- 위치
  - 위 경도 통해 현재 집에 있는지 여부 확인
- 회원
  - 카카오 로그인
  - 애플 로그인
  - 회원가입
- 푸쉬알람
  - Fcm 서비스 사용해 알람허용을 한 회원에게 매 시간 푸쉬메세지 전송

### 3. 기술

- Spring boot, Spring Data Jpa, Spring Security(사용 예정), Nginx, EC2, FCM, JPql

### 4. 개발 중 어려웠던 부분

- EC2 인스턴스 메모리 부족 문제
- git branch 충돌
- static 파일 통한 웹뷰 사용
- 익숙한 SQL문 대신 JPQL 사용
- Java 11 문법 
- Spring 진입장벽

### 5. API 스펙

| Method | URI | Description |
|:---:|:---|:---|
| `GET` | /login/jwt | 자동 로그인 API |
| `POST` | /users | 회원가입 API |
| `POST` | /login/kakao | 자동 로그인 API |
| `GET` | /users/:userIdx | 회원 정보 조회API |
| `PATCH` | /users/:userIdx | 회원 정보 수정API |
| `POST` | /login/apple | 애플 로그인 API |
| `GET` | /users/recently-locations | 회원 최근 위치정보 리스트 조회 API |
| `GET` | /push-alarms | push알림 전송 API |
| `GET` | /users/challenge-times | 회원 시간 조회 API |
| `GET` | /users/locations | 회원 현재 위치 정보조회 API |
| `POST` | /users/challenge-times | 회원 시간 등록 API |
| `POST` | /users/tokens | 기기토큰 등록 API |
| `POST` | /challenges/:challengeIdx/success | 챌린지 성공 API |
| `POST` | /challenges/:challengeIdx/fail | 챌린지 실패 API |

### 6. UI
![KakaoTalk_20210203_224414740](https://user-images.githubusercontent.com/59005171/109386998-e1057b00-7941-11eb-9bda-a8e52b54fb2c.jpg)
![KakaoTalk_20210203_224414901](https://user-images.githubusercontent.com/59005171/109386957-a996ce80-7941-11eb-8328-dd7adae5c95f.jpg)
![KakaoTalk_20210203_222819910](https://user-images.githubusercontent.com/59005171/109386996-dfd44e00-7941-11eb-8977-c1f0ae06c920.jpg)
![KakaoTalk_20210203_224415021](https://user-images.githubusercontent.com/59005171/109386992-dba83080-7941-11eb-90e8-e7a6f79d815e.jpg)


