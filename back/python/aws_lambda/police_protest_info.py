import json
import requests
from bs4 import BeautifulSoup
from tabula import read_pdf

clickList = []
updatelist = []


def find_data():
    for i in range(1,4):
        selector = "#subContents > div > div.inContent > table > tbody > tr:nth-child({}) > td.subject > a".format(i)
        html = requests.get("https://www.smpa.go.kr/user/nd54882.do")
        soup = BeautifulSoup(html.text, 'html.parser').select_one(selector)
        time = soup.text.split(" ")[2]
        href_data = soup['href'].split(",")[2][1:9]
        updatelist.append(tuple((time,href_data)))

        """
        
        체크할거 여기 추가해야함 
        
        """

    for i,j in updatelist:
        url = "https://www.smpa.go.kr/user/nd54882.do?View&uQ=&pageST=SUBJECT&pageSV=&imsi=imsi&page=1&pageSC=SORT_ORDER&pageSO=DESC&dmlType=&boardNo={}&returnUrl=https://www.smpa.go.kr:443/user/nd54882.do".format(j)
        html = requests.get(url)
        selector = "#subContents > div > div.inContent > table > tbody > tr:nth-child(3) > td > a:nth-child(5)"
        soup = BeautifulSoup(html.text,'html.parser').select_one(selector)
        clickList.append(tuple((i,soup["onclick"].split(",")[1][1:9])))


   
    

def get_text(page):
    text = page.extract_text()
    return text


def pdf_tabula(pdf):
    df = read_pdf(pdf, pages="all")[0]
    df.dropna(axis=1, how='all', inplace=True)

    return df



def download(url, file_name):
    with open(file_name, "wb") as file:
        response = requests.get(url)
        file.write(response.content)





def lambda_handler(event, context):
    # TODO implement
    date_time = 0
    protest_data = []
    find_data()
    time_count = 1
    for time, board in clickList:
        
        url = "https://www.smpa.go.kr/common/attachfile/attachfileDownload.do?attachNo=" + board
        download(url, "/tmp/" + time + ".pdf")
        date_time += 1
        pdf_info = pdf_tabula("/tmp/" + time + ".pdf")
        protest_dict = {}
        protest_dict['date'] = time

        
        for i in range(1, int((pdf_info.shape[0] - 3)/3) + 2):
            
            place = ""

            for j in range(0, pdf_info.loc[i * 3 - 3].size):
                if(str(pdf_info.loc[i * 3 - 3][j]) != "nan"):
                    place += str(pdf_info.loc[i * 3 - 3][j])
            

            for k in range(0, pdf_info.loc[i * 3 - 1].size):
                if(str(pdf_info.loc[i * 3 - 1][k]) != "nan"):
                    region = str(pdf_info.loc[i * 3 - 1][k])
                    break

            
            
            protest_dict['protest'] = {'place' : place, 'region' : region, 'time' : str(pdf_info.loc[i * 3 - 2]['집회 일시']), 'people': str(pdf_info.loc[i * 3 - 2]['신고 인원'])}
            

            protest_data.append(protest_dict.copy())



        
    final_data = {}
        
    final_data['data'] = protest_data

    print(final_data)
    
    return {
    'statusCode': 200,
    'body': json.dumps(final_data, ensure_ascii=False)
}
        



        



print(lambda_handler("",""))
