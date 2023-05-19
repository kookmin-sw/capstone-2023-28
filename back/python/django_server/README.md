# 서버 환경 세팅 방법
## 가상환경 및 라이브러리 설치
* `python3 -m venv venv` : venv라는 이름으로 가상환경 생성
* `source venv/bin/activate` : 생성한 가상환경에 진입
* `pip install -r requirements.txt` : 서버 운영에 필요한 라이브러리, 의존성 통합 설치
* `pip install mysql-client` : 우분투 환경이라면 설치 (맥이라면 mysqlclient)
* `cd database/database`, `vim secrets.json` 후 시크릿 키 작성 : 시크릿 키는 보안사항으로 따로 생성해야함
## mysql 스키마 생성
로컬 환경에 mysql를 설치하고 mysqlworkbench에 들어가서 scheme 생성
<img width="879" alt="image" src="https://user-images.githubusercontent.com/39481106/230592742-e3c3f048-7d0c-4229-b8bf-944830e03db2.png">

## /database/ 에 시크릿 파일 생성
서버 운영을 위한 시크릿 키가 정의된 secrets.json을 생성한다
<img width="281" alt="image" src="https://user-images.githubusercontent.com/39481106/230593774-b1a74378-ae3f-48fc-b188-b91be72f0eb1.png">

## 마이그레이션 후 서버 실행
* `python manage.py makemigrations`: 정의한 models.py 대로 db 버전 관리가 가능한 마이그레이션 파일 생성
* `python manage.py migrate`: makemigrations와 다르게 실제 db에 변경사항을 적용함
* `python manage.py runserver`: (로컬) 서버 실행

