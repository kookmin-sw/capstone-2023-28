import boto3
import json

def separate_time_steps(log_messages):
    time_steps = []
    current_time_step = []
    prev_timestamp = None

    for log_message in log_messages:
        timestamp = log_message.split('\t')[0]

        if prev_timestamp and timestamp != prev_timestamp:
            time_steps.append(current_time_step)
            current_time_step = []

        current_time_step.append(log_message)
        prev_timestamp = timestamp

    if current_time_step:
        time_steps.append(current_time_step)

    return time_steps

def get_latest_log_stream(log_group):
    client = boto3.client('logs')
    response = client.describe_log_streams(
        logGroupName=log_group,
        orderBy='LastEventTime',
        descending=True,
        limit=1
    )

    if 'logStreams' in response and response['logStreams']:
        return response['logStreams'][0]['logStreamName']
    else:
        return None

def get_all_log_messages_from_latest_log_stream(log_group):
    client = boto3.client('logs')
    latest_log_stream = get_latest_log_stream(log_group)
    if not latest_log_stream:
        return []

    messages = []

    response = client.get_log_events(
        logGroupName=log_group,
        logStreamName=latest_log_stream,
        limit=10000,
        startFromHead=True
    )

    if 'events' in response:
        events = response['events']
        for log_event in events:
            log_message = log_event['message']
            messages.append(log_message)

    return messages

def lambda_handler(event, context):
    log_group = '/aws/lambda/police_protest_info'

    log_messages = get_all_log_messages_from_latest_log_stream(log_group)

    if log_messages:
        time_steps = separate_time_steps(log_messages)
        
        for list in time_steps:
            for data in list:
                if "data" in data:
                    print(data)
        return {
            'statusCode': 200,
            'body': {
                'time_steps': time_steps
            }
        }
    else:
        return {
            'statusCode': 404,
            'body': 'No matching log events found.'
        }
