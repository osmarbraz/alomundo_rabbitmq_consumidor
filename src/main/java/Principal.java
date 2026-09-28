import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * Exemplo de recebimento(Consumidor) de mensagens de uma fila do RabbitMQ
 * utilizando o serviço CloudAMQP.
 */
public class Principal {

    // URL de conexão com o servidor RabbitMQ
    private static final String URL_RABBITMQ = "amqp://guest:guest@localhost:5672";     
    
    // Nome da fila que será utilizada para receber as mensagens
    private static final String NOME_FILA = "alo";

    public static void main(String[] args) {

        try {

            // Cria a fábrica responsável por estabelecer conexões
            // com o servidor RabbitMQ.
            ConnectionFactory factory = new ConnectionFactory();

            try {
                // Configura a conexão utilizando a URL do RabbitMQ.
                factory.setUri(URL_RABBITMQ);
            } catch (URISyntaxException ex) {
                System.err.println("Erro: " + ex.getMessage());
            } catch (NoSuchAlgorithmException ex) {
                System.err.println("Erro: " + ex.getMessage());
            } catch (KeyManagementException ex) {
                System.err.println("Erro: " + ex.getMessage());
            }

            // Estabelece a conexão com o servidor RabbitMQ.
            Connection connection = factory.newConnection();

            // Cria um canal de comunicação com o RabbitMQ.
            Channel channel = connection.createChannel();

            // Define as características da fila.
            boolean durable = true;
            boolean exclusive = false;
            boolean autoDelete = false;

            /*
             * Declara a fila no RabbitMQ.
             *
             * durable = true:
             * A fila será mantida mesmo após uma reinicialização
             * do servidor RabbitMQ.
             *
             * exclusive = false:
             * A fila não pertence exclusivamente à conexão atual.
             *
             * autoDelete = false:
             * A fila não será excluída automaticamente.
             */
            channel.queueDeclare(
                    NOME_FILA,
                    durable,
                    exclusive,
                    autoDelete,
                    null
            );

            /*
             * Define que o consumidor receberá apenas uma mensagem
             * por vez, aguardando a confirmação antes de receber
             * outra mensagem.
             */
            channel.basicQos(1);

            /*
             * Define o comportamento executado quando uma
             * mensagem for recebida.
             */
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {

                // Converte os bytes recebidos para texto utilizando UTF-8.
                String mensagem = new String(
                        delivery.getBody(),
                        StandardCharsets.UTF_8
                );

                // Exibe a mensagem recebida no console.
                System.out.println(" Mensagem recebida: '" + mensagem + "'");

                /*
                 * Confirma ao RabbitMQ que a mensagem foi
                 * recebida e processada com sucesso.
                 */
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            };

            /*
             * Inicia o consumidor.
             *
             * false indica que será utilizada confirmação
             * manual das mensagens.
             *
             * O consumidor permanecerá ativo aguardando
             * novas mensagens.
             */
            channel.basicConsume(NOME_FILA,false,deliverCallback,consumerTag -> {} );

            // Informa ao usuário que o consumidor está aguardando.
            System.out.println("Aguardando mensagens. Para sair, pressione CTRL+C");

            /*
             * Mantém a aplicação em execução.
             *
             * Como o consumidor funciona de forma assíncrona,
             * precisamos manter o programa ativo para que ele
             * continue recebendo mensagens.
             */
            Thread.sleep(Long.MAX_VALUE);

        } catch (IOException | TimeoutException e) {

            // Exibe uma mensagem caso ocorra algum erro.
            System.err.println("Erro ao receber mensagens: " + e.getMessage());

        } catch (InterruptedException e) {

            // Restaura o estado de interrupção da thread.
            Thread.currentThread().interrupt();

            System.err.println("Consumidor interrompido.");
        }
    }
}