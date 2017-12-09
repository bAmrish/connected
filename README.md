# Connected
> A Simple application for bringing people together


Connected is a simple grails application that allows its users to send messages to each other. 

This application is built to demo capbilities for Messaging Queue technologies like RabbitMQ


## Setup

### Prerequisites:

- You should have docker installed on your machine

### Docker

In order to run this application you need a rabbitmq server running. Do the following:

- Create a docker volume for this container. As long as you have this volume and keep your container hostname the same, you won't have to run the setup again.
    - `docker volume create rabbitmq-data`
- Create a rabbitmq container using the following command: 
    - `docker run -d --rm --hostname mq-dev --name mq-dev --mount type=volume,src=rabbitmq-data,target=/var/lib/rabbitmq -p 15672:15672 -p 5672:5672 rabbitmq:3.6.14-management`
-`exec` into the rabbitmq container using the following command
    - `docker exec -it mq-dev /bin/bash`
- Setup your data using the following commands
    - `rabbitmqctl add_user ccm-admin hare123`
    - `rabbitmqctl set_user_tags ccm-admin administrator`
    - `rabbitmqctl add_user ccm-dev coney123`
    - `rabbitmqctl add_vhost ccm-dev-vhost`
    - `rabbitmqctl set_permissions -p ccm-dev-vhost ccm-dev   ".*" ".*" ".*"`
    - `rabbitmqctl set_permissions -p ccm-dev-vhost ccm-admin ".*" ".*" ".*"`
- exit out the container by using the `exit` command 
- You can view the management studio on http://localhost:15672/ 
    - default username password: guest/guest
    
