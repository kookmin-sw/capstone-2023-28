import boto3
import json

def lambda_handler(event, context):

    # Create CloudWatch Logs client
    client = boto3.client('logs')

    # Get log streams
    log_group_name = '/aws/lambda/police_protest_info'
    log_stream_response = client.describe_log_streams(logGroupName=log_group_name, orderBy='LastEventTime', descending=True, limit=1)
    log_streams = log_stream_response['logStreams']

    # Get latest log events
    log_events_response = client.filter_log_events(
        logGroupName=log_group_name,
        logStreamNames=[log_streams[0]['logStreamName']],
        limit=4
    )
    log_events = log_events_response['events']
    
    # Print latest 4 logs
    for event in log_events:
        print(json.dumps(event['message'],ensure_ascii=False))
    
    return {
        'statusCode': 200,
        'body': json.dumps('Latest 4 logs printed')
    }
