import com.rabbitmq.client.ConnectionFactory
import connected.mq.RabbitMQService

// Place your Spring DSL code here
beans = {

    ConnectionFactory factory = new ConnectionFactory()
    factory.username = "ccm-dev"
    factory.password = "coney123"
    factory.virtualHost = "ccm-dev-vhost"
    factory.port = 5672
    factory.host = "localhost"

    mqService(RabbitMQService, factory)
}
