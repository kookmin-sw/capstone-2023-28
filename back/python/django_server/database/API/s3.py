import uuid
from database import settings
import boto3

class S3ImageUploader:

    def __init__(self, file):
        self.file = file
        self.file_name = str(uuid.uuid4())
        #self.url = f'https://{settings.AWS_STORAGE_BUCKET_NAME}.s3.{settings.AWS_S3_REGION_NAME}.amazonaws.com/{self.file_name}'
        self.s3_client = boto3.client(
            's3',
            aws_access_key_id=settings.AWS_ACCESS_KEY_ID,
            aws_secret_access_key=settings.AWS_SECRET_ACCESS_KEY,
            region_name=settings.AWS_S3_REGION_NAME
        )
        self.url = None
    def get_image_url(self):
        url = self.s3_client.generate_presigned_url(
            'get_object',
            Params={
                'Bucket': settings.AWS_STORAGE_BUCKET_NAME,
                'Key': self.file_name,
            }
        )
        return url
    def get_image_name(self):
        return self.file_name
    def upload(self):
        self.s3_client.upload_fileobj(self.file, settings.AWS_STORAGE_BUCKET_NAME, self.file_name)
        return self.file_name
    def delete(self, file_name):
        self.s3_client.delete_object(Bucket=settings.AWS_STORAGE_BUCKET_NAME, Key=file_name)