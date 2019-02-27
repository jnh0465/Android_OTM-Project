# Android_OTM-Project
180729-181120 / Android side
___
`기능 : 
`   
~~~
- 어플에서 메인엑티비티 진입시(켤 때) 이미지 띄우기 _SplashActivity
- viewpager이용해서 광고처럼 이미지 넘기기 _ViewPagerAdapter

- sharepreference를 이용해 처음 실행 여부 파악해 uuid 발급, 토큰 생성 후 LexActivity로 넘겨주기 _MainActivity
- 리스트 어뎁터 이용해 가게선택 후 LexActivity로 넘겨주기 _MenuActivity, MyAdapter

- 위치권한, 마이크 권한 허용받기 및 체크
  오투미 챗봇(서버)을 웹뷰로 켜기, 웹뷰에서 웹뷰로 넘어갈 수 있게 하기
  토큰, uuid값 데이터로 담아서 url로 보내주기 _LexActivity
  
- 아임포트api 이용해 결제하기
- db에서 주문내역 끌어와 주문내역리스트 보여주기 _MypageActivity

- node.js 웹에서 보낸 푸시 분기해서 받기  _MyFirebaseMessagingService
- 푸시알림 허용여부, 설정액티비티 _NotificationActivity
- 푸시알림시 화면 깨우기 _PushUtils
~~~

`아쉬운 점 : 
`   
~~~
 푸시 알람 하나 구현하자고 안드로이드에 입문했기 때문에 버튼, 레이아웃 구성, 리스너 연결, 어뎁터 생성 및 연결 등 
특히 기초적인 면에서 미숙한 면이 많았다. 또, 토큰, uuid값을 전달하는 과정에서 네트워크통신을 이용해 post방식으로 
연결했었으나 두개의 서버와 팀원들 각자의 코드를 합치는 과정에서 프로젝트 마감의 압박과 http가 아닌 https와의 통신 
어려움, 값이 전달이 되지 않는 문제 때문에 프로젝트 막판 url를 이용하는 get방식으로 바꿨던것이 아쉽다.
또 단순히 fragment 사용이 어렵다는 이유로 거의 모든 페이지를 activity로 구성했다는 점과 listview와 adapter의 개념을
잘 알지 못하고 주먹구구식으로 완성에만 초점을 맞춰 어플 하나를 낸 것같아 많이 아쉽다. 프로젝트 이후 안드로이드를 
뷰부터 차근차근 공부하고 코드를 보니 좀 더 좋은 방식이 있었고 더 좋은 코드를 짤 수 있었을 텐데하며 아쉬운 점이 많다.

- 프로젝트 완료 시점 : 18/11/21
- README 작성 시점 : 19/02/27
~~~
___

`SplashActivity-----------------------MainActivity--------------------------MenuActivity-------------------------`<br>
<img src="https://user-images.githubusercontent.com/38582562/52386175-2ee38800-2ac8-11e9-98e6-1d5904b69465.jpg" width="32%"> 
<img src="https://user-images.githubusercontent.com/38582562/52386194-3efb6780-2ac8-11e9-89c9-49d6b944fed4.jpg" width="32%"> 
<img src="https://user-images.githubusercontent.com/38582562/52385562-8c2a0a00-2ac5-11e9-827c-8144885e8be9.png" width="32%"> 

`LexActivity-----------------------------------------------------------------------------------------------------`<br>
<img src="https://user-images.githubusercontent.com/38582562/52385566-8cc2a080-2ac5-11e9-8455-5059bb3055c4.png" width="32%"> 
<img src="https://user-images.githubusercontent.com/38582562/52385691-17a39b00-2ac6-11e9-8e05-c9341712b911.jpg" width="32%"> 
<img src="https://user-images.githubusercontent.com/38582562/52385565-8c2a0a00-2ac5-11e9-94f8-ed9a9b5286de.png" width="30.5%"> 

`LexActivity-----------------------------------------------------------------------------------------------------`<br>
<img src="https://user-images.githubusercontent.com/38582562/52385570-8d5b3700-2ac5-11e9-8bc2-668ba41a7be0.png" width="31.5%"> 
<img src="https://user-images.githubusercontent.com/38582562/52386019-69005a00-2ac7-11e9-9aa7-27f8a7ec2cf2.jpg" width="31.5%"> 
<img src="https://user-images.githubusercontent.com/38582562/52385894-fdb68800-2ac6-11e9-9616-704114da100a.jpg" width="31.7%"> 

`MypageActivity-----------------------NotificationActivity-------------------------------------------------------`<br>
<img src="https://user-images.githubusercontent.com/38582562/52386044-89301900-2ac7-11e9-81c4-b09a8605ad66.jpg" width="31.7%"> 
<img src="https://user-images.githubusercontent.com/38582562/52386411-5f77f180-2ac9-11e9-9684-5d6d92f95939.jpg" width="31.5%">
<img src="https://user-images.githubusercontent.com/38582562/52386410-5f77f180-2ac9-11e9-803f-e069b14bd4f7.jpg" width="31.7%">

`MyFirebaseMessagingService(ad)-----------------------------------------------------------------------------------`<br>
<img src="https://user-images.githubusercontent.com/38582562/52386589-12e0e600-2aca-11e9-91eb-b9b7dc905af8.png" width="65%"> 
<img src="https://user-images.githubusercontent.com/38582562/49933014-ed8e8c00-ff0d-11e8-900c-88d4f45e7428.jpg" width="32%"> 

`MyFirebaseMessagingService(alert)--------------------------------------------------------------------------------`<br>
<img src="https://user-images.githubusercontent.com/38582562/52386587-12e0e600-2aca-11e9-920c-46493431b971.png" width="65%"> 
<img src="https://user-images.githubusercontent.com/38582562/49933015-ed8e8c00-ff0d-11e8-9feb-7570505b78df.jpg" width="32%">
