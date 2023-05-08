import boto3
import json

def get_latest_json_from_log_stream(log_group, log_stream):
    client = boto3.client('logs')

    response = client.get_log_events(
        logGroupName=log_group,
        logStreamName=log_stream,
        limit=1,
        startFromHead=True
    )

    if 'events' in response:
        events = response['events']
        if events:
            log_event = events[0]
            log_message = log_event['message']

            try:
                json_data = json.loads(log_message)
                return json_data
            except json.JSONDecodeError as e:
                print(f"Failed to decode JSON: {e}")

    return None

def lambda_handler(event, context):
    log_group = '/aws/lambda/police_protest_info'
    log_stream = '2023/05/08/[$LATEST]65ffdf297e424d2e8ce1687022ac797c'

    json_data = get_latest_json_from_log_stream(log_group, log_stream)

    if json_data:
        return {
            'statusCode': 200,
            'body': json.dumps(json_data)
        }
    else:
        return {
            'statusCode': 404,
            'body': 'No matching log events found.'
        }
