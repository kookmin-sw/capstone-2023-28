from rest_framework.views import exception_handler
from rest_framework.exceptions import AuthenticationFailed
from rest_framework.response import Response

def custom_exception_handler(exc, context):
    if isinstance(exc, AuthenticationFailed):
        return Response({
            'status': 'Error',
            'res': {
                'error_name': 'user_not_found',
                'error_id': 1
            }
        }, status=401)
    response = exception_handler(exc, context)
    return response
