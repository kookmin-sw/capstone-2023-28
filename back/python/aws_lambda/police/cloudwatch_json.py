import boto3

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

def get_all_log_messages_from_log_stream(log_group, log_stream):
    client = boto3.client('logs')
    messages = []

    response = client.get_log_events(
        logGroupName=log_group,
        logStreamName=log_stream,
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
    log_stream = '2023/05/08/[$LATEST]65ffdf297e424d2e8ce1687022ac797c'

    log_messages = get_all_log_messages_from_log_stream(log_group, log_stream)

    if log_messages:
        time_steps = separate_time_steps(log_messages)

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
