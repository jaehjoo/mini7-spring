# mini7-spring

## 목표
### 1. 환자의 응급 등급 분류 및 병원 추천 AI 모델 정립
### 2. 위 모델을 활용한 Spring Boot 기반 웹 서비스 구축
### 3. Azure Web App에 Custom Container를 이용하여 서비스할 것
## 프로젝트 기간
### 2024/12/18 ~ 2024/12/26
## 개발 스택 및 개념도
![image](/images/rule.png)
### Infra
- Azure Web app Container
- virtual Network
### AI Server
- fastAPI
### AI Model
- Whisper-1
- Gpt-3
- KcELECTRA
### Server
- Spring Boot
- view template engine : thymeleaf
- DB : sqlite
## 구현 기능(뼈대)
### Infra
- [X] Azure Web App 기반 Custom Container 구축 
### AI Model
- [X] Whisper-1 - ChatGPT - KcELECTRA로 이어지는 응급 모델 구축
### AI Server
- [X] fastAPI 기반 RestAPI 서버 구축
### Server
- [X] Spring Boot 기반 MVC 서버 구축
## 구현 기능(살) : 구현중
### 사용자 페이지
- [X] 응급 신고자 정보 입력 및 DB 반영
- [X] 추천 병원 목록 전시
- [X] 응급실 경로 지도
### 관리자 페이지
- [X] Dashboard
    - [X] 응급 요청 이력
    - [X] 지역별 응급 요청 건수
    - [X] 최다 요청 병원
