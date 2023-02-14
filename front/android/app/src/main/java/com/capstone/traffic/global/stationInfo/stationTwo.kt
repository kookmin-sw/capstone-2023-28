package com.capstone.traffic.global.stationInfo

object stationTwo {
    fun getAll() : MutableList<Pair<String,MutableList<info>>>
    {
        val mutableList = mutableListOf<Pair<String,MutableList<info>>>()
        mutableList.add(Pair("본선",getStationMain()))
        mutableList.add(Pair("성수 지선",getStationTwo()))
        mutableList.add(Pair("신도림 지선", getStationThird()))

        return mutableList
    }
    private fun getStationMain() : MutableList<info> = stationMain
    private fun getStationTwo() : MutableList<info> = stationTwo
    private fun getStationThird() : MutableList<info> = stationThird

    private val stationMain = mutableListOf(
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
    private val stationTwo = mutableListOf<info>(
        info("성수(지선)",211),
        info("용답",244),
        info("신답",245),
        info("용두",250),
        info("신설동",246)
    )

    private val stationThird = mutableListOf<info>(
        info("신도림(지선)",234),
        info("도림천",247),
        info("양천구청",248),
        info("신정네거리",249),
        info("까치산",200)
    )
}