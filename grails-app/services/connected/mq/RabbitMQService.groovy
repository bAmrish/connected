package connected.mq

import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.ShutdownListener
import com.rabbitmq.client.ShutdownSignalException
import grails.transaction.Transactional

import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Transactional
class RabbitMQService implements ShutdownListener{

    final    private ConnectionFactory factory
    volatile private Connection connection
    private  final   Executor executor

    RabbitMQService(ConnectionFactory factory) {
        this.factory = factory
        this.executor = Executors.newSingleThreadScheduledExecutor()
    }

    void start(){
        try {
            connection = factory.newConnection()
            //Adding this class Shutdown Listener, so we can respond to the shutdown request.
            connection.addShutdownListener(this)
        } catch (Exception e) {
            log.error """Error establishing connection to RabbitMQ.""", e
        } finally {
          asyncWaitAndReconnect()
        }
    }

    /**
     * This method is triggers the start method after waiting for 15 seconds.
     * This enables the application to reconnect to rabbitMQ service if connection to the
     * service is lost for any reason other than explicit application shutdown.
     */
    private void asyncWaitAndReconnect(){
        executor.schedule(new Runnable() {
            @Override
            void run() {
                start()
            }
        }, 15, TimeUnit.SECONDS)
    }

    @Override
    void shutdownCompleted(ShutdownSignalException cause){
        if(!cause.isInitiatedByApplication()) {
            log.warn "Unwanted shutdown for RabbitMQ connection detected. Retrying to connect.", cause
            connection = null
            asyncWaitAndReconnect()
        }
    }

    void stop(){
        executor.shutdownNow()

        if(connection != null){
            try{
                connection.close()
            } catch (Exception e) {
                log.error """ Exception occourred while trying to close RabbitMQ connection. """, e
            }
            finally {
                connection = null
            }
        }
    }

}
