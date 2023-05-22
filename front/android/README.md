# android에 해당하는 폴더입니다.

## 사용 라이브러리
~~~
1. Retrofit
  - 서버 통신을 위해 사용
2. Glide
  - 효율적인 이미지 로딩을 위해 사용
3. Firebase
  - FCM을 이용한 인앱 알람을 위해 사용
4. jsoup
  - html 코드를 통해, 링크 썸네일 및 제목, 내용을 가져오기 위해 사용
5. ted permission
  - 권한 체크
6. lottie
  - 로딩 이미지
~~~

## 구조도

~~~
com.capstone.traffic                    # Root Package
├── model                               # db 및 retrofit 
|   ├── network                         # REST API 통신 - Retrofit2
|   |   ├── kakao                       # Kakao API 사용을 위한 Retrofit 과 dataClass
|   |   |   └──  place                  # Kakao API 중 - 장소 키워드 API 관련
|   |   |       ├── Client              # Retrotit - Client
|   |   |       ├── Place               # 통신 응답 DataClass 
|   |   |       ├── Response            # 통신 응답 DataClass
|   |   |       └── Service             # Retrofit - Service
|   |   |
|   |   ├── seoul                       # 서울시 실시간 교통정보 API 사용을 위한 Retrofit과 dataClass
|   |   |   ├── arrive                  # 서울시 API 중 - 도착 정보 API 관련
|   |   |   |   ├── RealTimeArrivalList # 통신 응답 DataClass
|   |   |   |   ├── Seoul               # 통신 응답 DataClass
|   |   |   |   └── SeoulService        # Retrofit - Service
|   |   |   |
|   |   |   ├── locate                  # 서울시 API 중 - 실시간 열차 위치 API 관련
|   |   |   |   ├── RealTimePostionList # 통신 응답 DataClass
|   |   |   |   ├── Seoul               # 통신 응답 DataClass
|   |   |   |   └── SeoulService        # Retrofit - Service
|   |   |   |
|   |   |   └── SeoulClient             # Retrofit - arrive와 locate의 공용 Client  
|   |   |  
|   |   ├── sk                          # SK 길찾기 API 사용을 위한 Retrofit 과 dataClass
|   |   |   ├── direction               # 길찾기 API    
|   |   |   |   ├── dataClass           # 통신 응답 DataClass 모음
|   |   |   |   ├── Client              # Retrofit - Client
|   |   |   |   └── Service             # Retrofit - Service
|   |   |   |  
|   |   ├── subway                      # 실시간 열차 위치 API 관련
|   |   |   ├── Client                  # Retrofit - Client    
|   |   |   ├── Response                # 통신 응답 DataClass
|   |   |   ├── ResponseList            # 통신 응답 DataClass
|   |   |   ├── SubwayService           # Retrofit - Service 
|   |   |   └── Value                   # 통신 응답 DataClass
|   |   ├── twitter                     # 트위터 1~9호선 트윗 키워드 순위 API
|   |       ├── Client                  # Retrofit - Client    
|   |       ├── Data                    # 통신 응답 DataClass
|   |       ├── ResponseData            # 통신 응답 DataClass
|   |       └── RankService             # Retrofit - Service 
|   |     
|   ├── global                          # 공용으로 사용되는 것들 (AppKey, BaseActivity, MyApplication ..)     
|   |   ├── appKey                      # API KEY 모음
|   |   |    └── APIKEY                 # APIKEY 
|   |   |
|   |   ├── BaseActivity                # 공용 Activity 추상 클래스
|   |   └── MyApplication               # 앱 시작 시 설정되야할 것들
|   |  
├── ui                                  # 엑티비티/뷰 레이어 
│   ├── checkout                        # 회원가입
│   │
|   └── home                            # 홈 화면
|       ├── HomeActivity                # HomeActivity
|       ├── HomeViewModel               # ViewModel for data Binding  
│       ├── PageType                    # enum class : 하단 네비게이션 바
│       ├── board                       # 게시판
│       │   ├── bulletinBoard           # 열차 도착 정보 
│       │   │   ├──ArrivalInform        # data class 
│       │   │   ├──ArriveInformActivity # ArriveInformActivity 
│       │   │   ├──BulletinViewModel    # ViewMOdel for data Binding
│       │   │   └──informAdapter        # ViewHolder for RecyclerView
│       │   └── BoardFragment           # Fragment for Board
│       │
│       ├── direction                   # 길찾기
│       │   ├── DirectionActivity       # DirectionActivity
│       │   ├── DirectionViewModel      # ViewModel for data Binding
│       │   └── SlideUpDialog           # Dialog
│       │
│       ├── route                       # 노선도
│       │   ├── dataClass               # Data Class
│       │   │   ├── NeighborLineData
│       │   │   ├── SubwayData
│       │   │   └── SubwayExpressData
│       │   │
│       │   ├── line                    # 노선도
│       │   │   ├── LineFragment        # LineFragment
│       │   │   └── SubwayText          # 동적 생성을 위한 LinearLayout 
│       │   ├── search
│       │   │   ├── arrivalInform       # 도착 정보
│       │   │   ├── SearchActivity      # SearchActivity 
│       │   │   └── SearchViewModel     # ViewModel for Data Binding
│       │   ├── RouteFragment           # RouteFragment
│       │   ├── SubwayAdapter           # ViewHolder for RecylcerView
│       │   └── SubwayExpressAdapter    # ViewHolder for RecyclerView
│       │    
│       ├── inform                      # 정보 텝
│       │   ├── ranking                 # 트윗 랭킹
│       │   │   └── rankingAdapter      # ViewHolder for RecyclerView
│       │   └── informFragment          # informFragment
│       │
│       └── login                       # 로그인     
│           ├── loginActivity           # LoginActivity
│           └── LoginViewModel          # ViewModel for Data Binding
│  
└── utils                               # sharePreference 등 다양한 유틸
~~~
