from rest_framework.views import exception_handler
from rest_framework.exceptions import AuthenticationFailed
from rest_framework.response import Response

def custom_exception_handler(exc, context):

    if isinstance(exc, AuthenticationFailed):
        return Response({
            'status': 'ERROR',
            'res': {
                'error_name': 'Authentication Failed',
                'error_id': 9
            }
        }, status=401)
    response = exception_handler(exc, context)
    return response
