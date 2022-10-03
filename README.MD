
# 책첵 : 구매, 판매전에 책정가격을 체크!

<img width = "120" src="https://user-images.githubusercontent.com/107388110/190458944-477fdfa9-129c-4704-9e8e-481667262d06.PNG" width="80%">

 <br />

## ✨ 핵심기능

**`메인 페이지 - 카테고리 별 조회 & 무한스크롤(QueryDSL)`**

<img width = "300px" src="https://user-images.githubusercontent.com/81909140/193582276-884e37f5-86c7-428d-975f-8f9da0b457eb.gif">



QueryDSL을 이용한 동적 쿼리 작성으로 책정 카테고리 별 조회 기능과 [전체/진행중/진행완료] 별 검색 기능을 조합해서 사용할 수 있도록 했습니다.
 <br />
* * *
 <br />
 <br />

**`무중단 배포 기술`**

Git Action + Nginx 기술을 활용한 유동적인 개발 & 운영 서버를 관리하여 업무 효율성 증대


 <br />


**`실시간 알람 기능`**

<img width = "300px" src="https://user-images.githubusercontent.com/81909140/192606571-bd9b013e-5ff9-4b9c-9671-f3531fd156a9.gif">



현재 서비스를 이용중인 유저에게 SSE(Server Sent Event)를 이용하여 실시간 알람을 기능 제공하고 있습니다.

 <br />
 
------------------
------------------

<div align="center">
  **`이메일 알림 기능`**
</div>


<img width = "300px" src="https://user-images.githubusercontent.com/81909140/193575956-bfd2f776-ab26-415e-a7dd-226dc54dd5d9.gif">


사이트에 접속하지 않은 유저를 고려하여 발생한 해당 이벤트의 내용을 JavaMailSender를 이용하여 이메일 알림으로 받을 수 있도록 했습니다.

 <br />
* * *
* * *

**`이미지 리사이징 기술`**

다량의 이미지 리소스를 효율적이고 이미지 서버의 용량을 최적화하여 관리하기 위해 이미지 압축 기술을 도입

<img width="200" alt="originalImage" src="https://user-images.githubusercontent.com/81909140/193583776-ed9a1c7e-4d0d-4ccb-a584-47c3194ffa21.png">
원본이미지 7.2MB
<img width="1026" alt="resize crop" src="https://user-images.githubusercontent.com/81909140/193583768-278533bf-b18c-4758-8689-9523eac8b7bd.png">
1. 이미지 리사이징으로 1/10 가량의 압축 (**7.2MB → 699.8KB**)

2. 이미지 crop으로 썸네일 크기의 250배 가량 압축 (**7.2Mb → 27.7KB**)

 <br />
* * *
* * *

**`Santry 활용한 Error Log Monitoring`**

무분별한 에러 로그에 대한 모니터링을 효율적으로 관리하기 위해 사용

<img src="https://user-images.githubusercontent.com/108970153/193579404-ddf38508-4a43-4d3d-ba9b-5191344c45d0.png">
<img src="https://user-images.githubusercontent.com/108970153/193580032-350bde06-249b-4217-9385-49a67c6ec7ee.png">
 <br />

## 👥 Back-End팀 소개


#### `Backend`
* **홍창형 (팀장)**
* **박현욱**
* **김병준** 
#### `Frontend`
* **배근아 (부팀장)**
* **김단비** 
#### `Designer`
* **박연아**

<br />


## 🗓 프로젝트 기간
* 2022년 08월 26일 ~ 2022년 10월 06일
* 배포일 : 2022년 09월 27일

<br />

## 🎈 ERD
<img src="https://user-images.githubusercontent.com/81909140/193574565-becb44fa-8d42-47b6-84e8-fced552024e9.png">  
<br />

## 📜 아키텍쳐
<img src="https://user-images.githubusercontent.com/107388110/190461358-951eac84-df6b-4c4b-9ecf-3936a8550fa6.jpg">   

<br />

## 📜 Troubleshooting
- CI CD 적용(Continuous Integration/Continuous Deployment) - [상세보기 - WIKI 이동](https://github.com/pnci1029/hanghae_8D_BE/wiki/CI-CD-%EC%A0%81%EC%9A%A9(Continuous-Integration-Continuous-Deployment))
- OAuth2.0 Login Redis - [상세보기 - WIKI 이동](https://github.com/pnci1029/hanghae_8D_BE/wiki/OAuth2.0-Login---Redis)
- EC2 RAM 초과 & Memory 성능 이슈 - [상세보기 - WIKI 이동](https://github.com/pnci1029/hanghae_8D_BE/wiki/EC2-RAM-%EC%B4%88%EA%B3%BC-&-Memory-%EC%84%B1%EB%8A%A5-%EC%9D%B4%EC%8A%88)
- Image 리사이징 이슈 - [상세보기 - WIKI 이동](https://github.com/pnci1029/hanghae_8D_BE/wiki/Image-%EB%A6%AC%EC%82%AC%EC%9D%B4%EC%A7%95-%EC%9D%B4%EC%8A%88)
- QueryDSL 적용 - [상세보기 - WIKI 이동](https://github.com/pnci1029/hanghae_8D_BE/wiki/QueryDSL-%EC%A0%81%EC%9A%A9)
- SSE - [상세보기 - WIKI 이동](https://github.com/pnci1029/hanghae_8D_BE/wiki/SSE)

<br />

## 🎈 LINK
📔  **[노션 링크]( https://frost-class-aac.notion.site/2-02def98b9fe04e8dbf00e39cba77693d)**


## 🛠 Tech Stack

[//]: # (![리액트]&#40;https://user-images.githubusercontent.com/97425158/161745107-cc062718-9c52-4446-8f14-9faba0b9dea7.svg&#41;)

[//]: # (![자바스크립트]&#40;https://user-images.githubusercontent.com/97425158/161745127-a3fa5ed0-ceb6-427a-94d1-834d762fd3b4.svg&#41;)

[//]: # (![에이치티엠엘]&#40;https://user-images.githubusercontent.com/97425158/161745161-566f015b-0ec2-4bba-82aa-f3bb7498bdd7.svg&#41;)

[//]: # (![씨에스에스]&#40;https://user-images.githubusercontent.com/97425158/161745198-92ff3896-7ce0-4946-a8b4-e6d23223eb3b.svg&#41;)

[//]: # (![리덕스]&#40;https://user-images.githubusercontent.com/97425158/161745222-ea0ba9bf-86e4-48cb-8a44-f8d8bfec2d02.svg&#41;)

[//]: # (![악시오스]&#40;https://user-images.githubusercontent.com/97425158/161745239-453b4075-7bd0-4c63-9c5a-5c1d76021b8d.svg&#41;)

[//]: # (![스타일드컴포넌트]&#40;https://user-images.githubusercontent.com/97425158/161745269-27a8a71d-788d-4bdf-97e8-f86c97b224a9.svg&#41;)

[//]: # (![클라우드프론트]&#40;https://user-images.githubusercontent.com/97425158/161745404-114d6c7d-c720-4370-b0dd-4aea4893bb1d.svg&#41;)

[//]: # (![아마존S3]&#40;https://user-images.githubusercontent.com/97425158/161744999-3ae8a4d1-48d8-41fc-af06-c601f6e1fc4d.svg&#41;)


**Backend Tech Stack**  
<img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">
<img src="https://img.shields.io/badge/Springboot-6DB33F?style=for-the-badge&logo=Springboot&logoColor=white">
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/aws-232F3E?style=for-the-badge&logo=AmazonAWS&logoColor=white">
<img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white">
<img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white">
<img src="https://img.shields.io/badge/codedeploy-6DB33F?style=for-the-badge&logo=codedeploy&logoColor=white">
<img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
<img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=JUnit5&logoColor=white">
<img src="https://img.shields.io/badge/NGINX-009639?style=for-the-badge&logo=NGINX&logoColor=white">
<img src="https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">
<img src="https://img.shields.io/badge/ubuntu-FCC624?style=for-the-badge&logo=linux&logoColor=black">
<img src="https://img.shields.io/badge/linux-FCC624?style=for-the-badge&logo=linux&logoColor=black">

[//]: # (<img src="https://img.shields.io/badge/Apache JMeter-D22128?style=for-the-badge&logo=Apache JMeter&logoColor=white">)
