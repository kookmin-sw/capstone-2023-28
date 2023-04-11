import boto3
import subprocess
import time
from threading import Thread
from functools import wraps

# Singleton class to ensure only one instance of the EC2Controller is created
class Singleton(type):
    _instances = {}

    def __call__(cls, *args, **kwargs):
        if cls not in cls._instances:
            cls._instances[cls] = super(Singleton, cls).__call__(*args, **kwargs)
        return cls._instances[cls]

# Decorator to ensure that the EC2Controller is initialized before calling any methods
def ensure_initialized(func):
    @wraps(func)
    def wrapper(self, *args, **kwargs):
        if not self.is_initialized:
            self.initialize()
        return func(self, *args, **kwargs)
    return wrapper

class EC2Controller(metaclass=Singleton):
    def __init__(self):
        self.ec2 = None
        self.instance_id = None
        self.is_initialized = False

    def initialize(self):
        # Initialize boto3 EC2 client
        self.ec2 = boto3.client('ec2')
        self.is_initialized = True

    @ensure_initialized
    def start_instance(self):
        # Start the EC2 instance
        response = self.ec2.start_instances(InstanceIds=[self.instance_id])
        print(response)

    @ensure_initialized
    def stop_instance(self):
        # Stop the EC2 instance
        response = self.ec2.stop_instances(InstanceIds=[self.instance_id])
        print(response)

    @ensure_initialized
    def get_instance_state(self):
        # Get the current state of the EC2 instance
        response = self.ec2.describe_instances(InstanceIds=[self.instance_id])
        state = response['Reservations'][0]['Instances'][0]['State']['Name']
        return state

    @ensure_initialized
    def set_instance_id(self, instance_id):
        self.instance_id = instance_id

def run_terminal_command(command):
    # Run a command in the terminal and return the output
    output = subprocess.check_output(command, shell=True)
    return output.decode('utf-8')

if __name__ == '__main__':
    # Create an instance of the EC2Controller
    ec2_controller = EC2Controller()

    # Set the EC2 instance ID
    ec2_controller.set_instance_id('INSTANCE_ID')

    # Start the EC2 instance
    ec2_controller.start_instance()

    # Wait for the instance to start up
    while True:
        instance_state = ec2_controller.get_instance_state()
        if instance_state == 'running':
            break
        time.sleep(5)

    # Control the terminal from Python code
    while True:
        command = input('Enter command: ')
        if command == 'exit':
            break
        output = run_terminal_command(command)
        print(output)

    # Stop the EC2 instance
    ec2_controller.stop_instance()
