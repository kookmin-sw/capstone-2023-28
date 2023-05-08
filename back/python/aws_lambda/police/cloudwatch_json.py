import boto3

def get_all_log_messages_from_log_stream(log_group, log_stream):
    client = boto3.client('logs')
    messages = []

    response = client.get_log_events(
        logGroupName=log_group,
        logStreamName=log_stream,
        limit=10000,  # Increase the limit to retrieve more log events if necessary
        startFromHead=True
    )

    if 'events' in response:
        events = response['events']
        for log_event in events:
            log_message = log_event['message']
            messages.append(log_message)

    return messages

def lambda_handler(event, context):
    log_group = 'aws/lambda/police_protest_info'
    log_stream = '2023/05/08/[$LATEST]65ffdf297e424d2e8ce1687022ac797c'

    log_messages = get_all_log_messages_from_log_stream(log_group, log_stream)

    if log_messages:
        return {
            'statusCode': 200,
            'body': '\n'.join(log_messages)
        }
    else:
        return {
            'statusCode': 404,
            'body': 'No matching log events found.'
        }
