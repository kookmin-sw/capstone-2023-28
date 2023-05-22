## AWS Lambda 개요
AWS Lambda는 Amazon Web Services (AWS)의 서버리스 컴퓨팅 서비스입니다. 이 서비스를 사용하면 개발자는 서버 프로비저닝, 관리 및 확장과 같은 작업 없이 애플리케이션 코드를 실행할 수 있습니다. AWS Lambda는 이벤트 기반 아키텍처로 작동하며, 요청 또는 이벤트가 발생할 때만 코드가 실행됩니다.

## AWS Lambda 설정

1. AWS 계정 생성
AWS Lambda를 사용하려면 AWS 계정이 있어야 합니다. AWS 웹사이트(https://aws.amazon.com/)에서 계정을 생성합니다.

2. AWS Lambda 콘솔 열기
AWS 관리 콘솔에 로그인한 후 "AWS Lambda"를 검색하여 AWS Lambda 콘솔을 엽니다.

3. 함수 생성
AWS Lambda 콘솔에서 "함수 생성" 버튼을 클릭하여 새 함수를 생성하세요. 함수를 생성할 때는 함수 이름, 런타임 환경, 실행 역할 등을 구성해야 합니다.

4. 코드 업로드
함수를 생성한 후에는 코드를 업로드해야 합니다. 코드는 zip 파일 또는 함수 코드 편집기를 통해 업로드할 수 있습니다. 또는 AWS Lambda 콘솔에서 코드를 직접 편집할 수도 있습니다.

5. 이벤트 트리거 구성
AWS Lambda 함수를 트리거할 이벤트를 구성해야 합니다. 예를 들어, Amazon S3 버킷에 파일이 업로드되었을 때 함수를 실행하도록 설정할 수 있습니다. 이벤트 트리거는 AWS Lambda 콘솔에서 구성할 수 있습니다.

6. 함수 테스트
함수를 테스트하기 위해 AWS Lambda 콘솔에서 테스트 이벤트를 생성하고 함수를 실행할 수 있습니다. 이를 통해 함수가 예상대로 작동하는지 확인할 수 있습니다.


## AWS Lambda Layer 추가

필요한 패키지를 설치하기 위해서는 AWS Lambda Layer를 추가하여 패키지를 설치해 주어야 합니다.

https://docs.aws.amazon.com/ko_kr/lambda/latest/dg/configuration-layers.html
