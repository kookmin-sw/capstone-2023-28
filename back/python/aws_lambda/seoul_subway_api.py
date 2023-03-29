import requests
from typing import List
from dataclasses import dataclass
from dataclasses import field
import json
import copy


def get_api(line_number):
    key = "7a6b6d72776b79763733596c54654b"
    url = 'http://swopenapi.seoul.go.kr/api/subway/{}/json/realtimePosition/0/300/{}호선'.format(key, line_number)

    # 통신
    response = requests.get(url)

    """ 
    통신 성공 체크 
    걀과 : 실패 -> 상태 코드 출력 및 False 반환 
    """
    if response.status_code != 200:
        print("Seoul Subway API ERROR!!, Error Code = {}".format(response.status_code))
        return False

    data = response.json()
    realtime_position_list = data["realtimePositionList"]

    line = copy.deepcopy(switch(line_number))

    for data in realtime_position_list:
        data_update(data, line_number, line)

    return line

def switch(key):
    line = {1: Line1, 2: Line2, 3: Line3, 4: Line4, 5: Line5, 6: Line6, 7: Line7, 8: Line8, 9: Line9}
    return line.get(key)


def data_update(data, line_number,line):
    name = data['statnNm']
    up_down = int(data['updnLine'])
    train_status = int(data['trainSttus'])
    express = int(data['directAt'])
    train_number = data['trainNo']
    for idx1, line_data in enumerate(line):
        for idx2, dt in enumerate(line_data.data):
            if name == dt.name:
                status = dt.status
                if express == 0 and up_down == 0 : status[0] = train_status + 1
                elif express == 0 and up_down == 1 : status[1] = train_status + 1
                elif express == 1 and up_down == 0 : status[2] = train_status + 1
                elif express == 1 and up_down == 1 : status[3] = train_status + 1
                line[idx1].data[idx2].status = status
                return

@dataclass
class SubwayInfo:
    """
    name : 노선 이름
    status : 위치 정보 (0 - 없음 , 1 - 진입, 2 - 도착, 3 - 출발)
    """
    name: str
    status: List[int] = field(default_factory=list)


@dataclass
class SubwayLineInfo:
    """
    name : 노선 이름 ex) 소요산 - 구로
    data : SubwayInfo 데이터
    """
    name: str
    express: str
    data: List[SubwayInfo] = field(default_factory=list)

# 노선 정보

# 1호선
Line1 = [
    SubwayLineInfo(
        name="소요산 - 구로",
        express="1",
        data=[SubwayInfo(
            name="소요산",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="동두천",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="보산",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="동두천중앙",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="지행",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="덕정",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="덕계",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="양주",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="녹양",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="가능",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="소요산",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="의정부",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="회룡",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="망월사",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="도봉산",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="도봉",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="방학",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="창동",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="녹천",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="월계",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="광운대",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="석계",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="신이문",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="외대앞",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="회기",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="청량리",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="제기동",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="신설동",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="동묘앞",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="동대문",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="소요산",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="종로5가",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="종로3가",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="종각",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="시청",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="서울역",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="남영",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="용산",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="노량진",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="대방",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="신길",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="소요산",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="영등포",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="신도림",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="구로",
            status=[0, 0, 0, 0]
        )]
    ),
    SubwayLineInfo(
        name="경인선(구일 - 인천)",
        express="1",
        data=[SubwayInfo(
            name="구일",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="개봉",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="오류동",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="온수",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="역곡",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="소사",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="부천",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="중동",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="송내",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="부개",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="부평",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="백운",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="동암",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="간석",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="주안",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="도화",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="제물포",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="도원",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="동인천",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="인천",
            status=[0, 0, 0, 0]
        ),
        ]
    ),
    SubwayLineInfo(
        name="경부선(가산디지털단지 - 신창)",
        express="1",
        data=[SubwayInfo(
            name="가산디지털단지",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="독산",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="금천구청",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="석수",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="관악",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="안양",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="명학",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="금정",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="군포",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="당정",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="의왕",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="성균관대",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="화서",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="수원",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="세류",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="병점",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="세마",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="오산대",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="오산",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="진위",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="송탄",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="서정리",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="지제",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="평택",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="성환",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="직산",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="두정",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="천안",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="봉명",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="쌍용(나사렛대)",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="아산",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="탕정",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="배방",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="온양온천",
            status=[0, 0, 0, 0]
        ), SubwayInfo(
            name="신창(순천향대)",
            status=[0, 0, 0, 0]
        ),
        ]
    )
]
Line2 = [
    SubwayLineInfo(
        name="본선",
        express="0",
        data=[
            SubwayInfo(
                name="성수",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="건대입구",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="구의",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="강변",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="잠실나루",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="잠실",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="잠실새내",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="종합운동장",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="삼성",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="선릉",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="역삼",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="강남",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="교대",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="서초",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="방배",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="사당",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="낙성대",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="서울대입구",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="봉천",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신림",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신대방",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="구로디지털단지",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="대림",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신도림",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="문래",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="영등포구청",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="당산",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="합정",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="홍대입구",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신촌",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="이대",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="아현",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="충청로",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="시청",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="을지로입구",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="을지로3가",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="을지로4가",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="동대문역사문화공원",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신당",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="상왕십리",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="왕십리",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="한양대",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="뚝섬",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="성수",
                status=[0,0,0,0]
            ),
        ]
    ),SubwayLineInfo(
        name="성수지선",
        express="0",
        data=[
            SubwayInfo(
                name="성수지선",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="용답",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신답",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="용두",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신설동",
                status=[0,0,0,0]
            ),
        ]
    ),SubwayLineInfo(
        name="신정지선",
        express="0",
        data=[
            SubwayInfo(
                name="신도림지선",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="도림천",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="양천구청",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신정네거리",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="까치산",
                status=[0,0,0,0]
            ),
        ]
    )
]
Line3 = [
    SubwayLineInfo(
        name="본선",
        express="0",
        data=[
            SubwayInfo(
                name="대화",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="주엽",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="정발산",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="마두",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="백석",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="대곡",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="화정",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="원당",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="원흥",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="삼송",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="지축",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="구파발",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="연신내",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="불광",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="녹번",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="홍제",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="무악재",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="독립문",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="경복궁",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="안국",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="종로3가",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="을지로3가",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="충무로",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="동대입구",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="약수",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="금호",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="옥수",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="압구정",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신사",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="잠원",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="고속터미널",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="교대",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="남부터미널",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="양재",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="매봉",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="도곡",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="대치",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="학여울",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="대청",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="일원",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="수서",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="가락시장",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="경찰병원",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="오금",
                status=[0,0,0,0]
            ),
        ]
    )
]
Line4 = [
    SubwayLineInfo(
        name="본선",
        express="0",
        data=[
            SubwayInfo(
                name="진접",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="오남",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="풍양",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="별내별가람",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="당고개",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="상계",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="노원",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="창동",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="쌍문",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="수유",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="미아",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="미아사거리",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="길음",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="성신여대입구",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="한성대입구",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="혜화",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="동대문",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="동대문역사문화공원",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="충무로",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="명동",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="회현",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="서울역",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="숙대입구",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="삼각지",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신용산",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="이촌",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="동작",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="총신대입구(이수)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="사당",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="남태령",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="선바위",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="경마공원",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="대공원",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="과천",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="정부과천청사",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="인덕원",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="평촌",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="범계",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="금정",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="산본",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="수리산",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="대야미",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="반월",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="상록수",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="한대앞",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="중앙",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="고잔",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="초지",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="안산",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신길온천",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="정왕",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="오이도",
                status=[0,0,0,0]
            )
        ]
    )
]
Line5 = [
    SubwayLineInfo(
        name="본선",
        express="0",
        data=[
            SubwayInfo(
                name="방화",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="개화산",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="김포공항",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="송정",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="마곡",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="발산",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="우장산",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="화곡",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="까치산",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신정(은행정)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="목동",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="오목교(목동운동장앞)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="양평",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="영등포구청",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="영등포시장",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신길",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="여의도",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="여의나루",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="마포",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="공덕",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="애오개",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="충정로",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="서대문",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="광화문",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="종로3가",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="을지로4가",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="동대문역사문화공원",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="청구",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신금호",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="행당",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="왕십리",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="마장",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="답십리",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="장한평",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="군자(능동)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="아차산(어린이대공원후문)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="광나루(장신대)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="천호(풍납토성)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="강동",
                status=[0,0,0,0]
            ),
        ]
    ),SubwayLineInfo(
        name="하남 방향",
        express="0",
        data=[
            SubwayInfo(
                name="길동",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="굽은다리(강동구민회관앞)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="명일",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="고덕",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="상일동",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="강일",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="미사",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="하남풍산",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="하남시청",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="하남검단산",
                status=[0,0,0,0]
            )
        ]
    ),SubwayLineInfo(
        name="마천시전",
        express="0",
        data=[
            SubwayInfo(
                name="둔춘동",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="올림픽공원(한국체대)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="방이",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="오금",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="개롱",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="거여",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="마천",
                status=[0,0,0,0]
            ),
        ]
    )
]
Line6 = [
    SubwayLineInfo(
        name="응암순환",
        express="0",
        data=[
            SubwayInfo(
                name="역촌",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="불광",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="독바위",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="연신내",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="구산",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="응암",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="새절(신사)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="증산(명지대앞)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="디지털미디어시티",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="월드컵경기장(성산)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="마포구청",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="망원",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="합정",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="상수",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="광흥창",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="대흥(서강대앞)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="공덕",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="효창공원앞",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="삼각지",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="녹사평",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="이태원",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="한강진",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="버티고개",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="약수",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="청구",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신당",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="동묘앞",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="창신",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="보문",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="안암(고대병원앞)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="고려대",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="월곡(동덕여대)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="상월곡(한국과학기술연구원)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="들곶이",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="석계",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="태릉입구",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="화랑대(서울여대입구)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="봉화산",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신내",
                status=[0,0,0,0]
            )
        ]
    )
]
Line7 = [
    SubwayLineInfo(
        name="본선",
        express="0",
        data=[
            SubwayInfo(
                name="장암",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="도봉산",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="수락산",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="마들",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="노원",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="중계",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="하계",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="공릉(서울산업대입구)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="태릉입구",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="먹골",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="중화",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="상봉",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="면목",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="사가정",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="용마산",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="중곡",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="군자(능동)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="어린이대공원(세종대)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="건대입구",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="뚝섬유원지",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="청담",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="강남구청",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="학동",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="논현",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="반포",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="고속터미널",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="내방",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="총신대입구(이수)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="남성",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="숭실대입구(살피재)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="상도(중앙대앞)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="장승배기",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신대방삼거리",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="보라매",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신풍",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="대림",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="남구로",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="가산디지털단지",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="철산",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="광명사가리",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="천왕",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="온수",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="까치울",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="부천종합운동장",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="춘의",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신중동",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="부천시청",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="상동",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="삼산체육관",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="굴포천",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="부평구청",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="산곡",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="석남",
                status=[0,0,0,0]
            ),
        ]
    )
]
Line8 = [
    SubwayLineInfo(
        name="본선",
        express="0",
        data=[
            SubwayInfo(
                name="암사",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="천호(풍납토성)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="강동구청",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="몽촌토성(평화의문)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="잠실",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="석촌",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="송파",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="가락시장",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="문정",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="장지",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="복정",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="남위례",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="산성",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="남한산성입구(성남법원, 검찰청)",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="단대오거리",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신흥",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="수진",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="모란",
                status=[0,0,0,0]
            )
        ]
    )
]
Line9 = [
    SubwayLineInfo(
        name="본선",
        express="1",
        data=[
            SubwayInfo(
                name="개화",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="김포공항",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="공항시장",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신방화",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="마곡나루",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="양천향교",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="가양",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="증미",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="등촌",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="염창",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신목동",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="선유도",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="당산",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="국회의사당",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="여의도",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="샛강",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="노량진",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="노들",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="흑",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="동작",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="구반포",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신반포",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="고속터미널",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="사평",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="신논현",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="언주",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="선정릉",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="삼성중앙",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="봉은사",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="종합운동장",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="삼전",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="석촌고분",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="석촌",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="송파나루",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="한성백제",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="올림픽공원",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="둔촌오륜",
                status=[0,0,0,0]
            ),SubwayInfo(
                name="중앙보훈병원",
                status=[0,0,0,0]
            ),
        ]
    )
]


def get_json(r_line):
    data = {'response': [{'line': i.name,'express':i.express, 'value': [{'name' : j.name, 'status': j.status} for j in i.data]} for i in r_line]}
    return {'statusCode': 200, 'body': json.dumps(data,ensure_ascii=False)}


# lambda event
def lambda_handler(event, context):
    line = int(event['queryStringParameters']["line"])
    r_line = get_api(line)
    return get_json(r_line)
