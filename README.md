# Consumidor RabbitMQ com Java

Aplicação Java que atua como **consumidora de mensagens** em uma fila do **RabbitMQ**, utilizando o serviço **CloudAMQP**.

Neste aplicação temos somente 1 fila, 1 produtor e 1 consumidor.

## Tecnologias utilizadas

* **Java**
* **RabbitMQ**
* **CloudAMQP**
* **RabbitMQ Java Client**
* **Maven**
* **AMQPS**
* **UTF-8**

## Funcionamento

A aplicação:

1. Estabelece uma conexão segura com o RabbitMQ.
2. Cria um canal de comunicação.
3. Declara a fila `alo`.
4. Configura `basicos(1)` para receber uma mensagem por vez.
5. Aguarda novas mensagens.
6. Exibe a mensagem recebida no console.
7. Confirma o processamento utilizando `basicAck()`.


## Execução

Configure a URL de conexão do RabbitMQ no código:

```java
private static final String URL_RABBITMQ = "...";
```

Depois, compile o projeto:

```bash
mvn clean package
```

Execute a aplicação:

```bash
mvn exec:java
```

## Observação

A URL do RabbitMQ contém credenciais de acesso. **Não publique credenciais reais no código-fonte ou em repositórios públicos.** Prefira utilizar variáveis de ambiente ou arquivos de configuração seguros.


## Aplicação produtora

https://github.com/osmarbraz/alomundo_rabbitmq_produtor
