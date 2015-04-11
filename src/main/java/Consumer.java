/**
 * Created by Eranda on 4/11/15.
 */

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

public class Consumer {
	private final static String QUEUE_NAME = "hello";
	private final static boolean DURABLE =false;
	private final static boolean EXCLUSIVE =false;
	private final static boolean AUTO_DELETE =false;

	public static void main(String[] argv) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		try{
			//checking whether queue is already declared throws exception
			// if its not exist or already declared as exclusive
			channel.queueDeclarePassive(QUEUE_NAME);
		} catch (IOException e) {
			channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, DURABLE, EXCLUSIVE, AUTO_DELETE, null);
		}

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(QUEUE_NAME, true, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println(" [x] Received '" + message + "'");
		}
	}
}
