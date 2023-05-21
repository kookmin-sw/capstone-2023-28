import requests
# Create your tests here.
endpoint = "http://localhost:8000/user/signup/"

get_response = requests.post(endpoint, json={"user_email": "user88@kookmin.ac.kr",
                                             "user_nickname": "user88",
                                             "password": "1234"})
print(get_response)