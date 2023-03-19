import boto3
import json
import os
import gzip
import shutil


def get_path():
    s3 = boto3.resource('s3')
    bucket=s3.Bucket('police-log')
    for key in bucket.objects.all():
        if key.key.startswith(''):
           return key.key



def lambda_handler(event, context):
    
    OBJECT_NAME = get_path()
    
    BUCKET_NAME = 'police-log'  


    FILE_NAME = '/tmp/log.gz' 
    
    s3 = boto3.client('s3')
    s3.download_file(BUCKET_NAME, OBJECT_NAME, FILE_NAME)
    
    print(os.listdir("/tmp"))
    
    with gzip.open('/tmp/log.gz', 'rb') as f_in:
        with open('/tmp/000000.txt', 'wb') as f_out:
            shutil.copyfileobj(f_in, f_out)
    
    with open('/tmp/000000.txt', 'rb') as file:
        strings = file.readline()
        
        strings = strings[54:]
        str_len = len(strings)
        strings = strings[:str_len - 3]
        
        
        str_json = json.loads(strings)
        
        file.close()

    
    print("Done")
    
    return {
        'statusCode' : '200',
        'body' : json.dumps(str_json, ensure_ascii=False),
        'headers' : {
            'Content-Type' : 'application/json',
        },
    }
