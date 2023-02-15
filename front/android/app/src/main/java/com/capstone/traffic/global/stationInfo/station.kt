package com.capstone.traffic.global.stationInfo

object station {
    fun getLine2All() : List<Pair<String,List<info>>>
    {
        val mutableList = mutableListOf<Pair<String,List<info>>>()
        mutableList.add(Pair("본선", stationLine2_1))
        mutableList.add(Pair("성수 지선", stationLine2_2))
        mutableList.add(Pair("신도림 지선", stationLine2_3))

        return mutableList
    }

    fun getLine3All() : List<String> {
        return stationLine3
    }
    // 2호선
    private val stationLine2_1 = listOf(
        info("성수",211),
        info("건대입구",212),
        info("구의",213),
        info("강변",214),
        info("잠실나루",215),
        info("잠실",216),
        info("잠실새내",217),
        info("종합운동장",218),
        info("삼성",219),
        info("선릉",220),
        info("역삼",221),
        info("강남",222),
        info("교대",223),
        info("서초",224),
        info("방배",225),
        info("사당",226),
        info("낙성대",227),
        info("서울대입구",228),
        info("봉천",229),
        info("신림",230),
        info("신대방",231),
        info("구로디지털단지",232),
        info("대림",233),
        info("신도림",234),
        info("문래",235),
        info("영등포구청",236),
        info("당산",237),
        info("합정",238),
        info("홍대입구",239),
        info("신촌",240),
        info("이대",241),
        info("아현",242),
        info("충정로",243),
        info("시청",201),
        info("을지로입구",202),
        info("을지로3가",203),
        info("을지로4가",204),
        info("동대문역사문화공원",205),
        info("신당",206),
        info("상왕십리",207),
        info("왕십리",208),
        info("한양대",209),
        info("뚝섬",210),
        info("성수",211)
    )
    private val stationLine2_2 = listOf<info>(
        info("성수(지선)",211),
        info("용답",244),
        info("신답",245),
        info("용두",250),
        info("신설동",246)
    )

    private val stationLine2_3 = listOf<info>(
        info("신도림(지선)",234),
        info("도림천",247),
        info("양천구청",248),
        info("신정네거리",249),
        info("까치산",200)
    )

    // 3호선

    private val stationLine3 = listOf<String>(
            "대화",
            "주엽",
            "정발산",
            "마두",
            "백석",
            "대곡",
            "화정",
            "원당",
            "원흥",
            "삼송",
            "지축",
            "구파발",
            "연신내",
            "불광",
            "녹번",
            "홍제",
            "무악재",
            "독립문",
            "경복궁",
            "안국",
            "종로3가",
            "을지로3가",
            "충무로",
            "동대입구",
            "약수",
            "금호",
            "옥수",
            "압구정",
            "신사",
            "잠원",
            "고속터미널",
            "교대",
            "남부터미널",
            "양재",
            "매봉",
            "도곡",
            "대치",
            "학여울",
            "대청",
            "일원",
            "수서",
            "가락시장",
            "경찰병원",
            "오금",)
}