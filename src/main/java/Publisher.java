/**
 * Created by Eranda on 4/11/15.
 */
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.IOException;

public class Publisher {
	private final static String EXCHANGE_NAME = "hello-exchange";
	private final static String EXCHANGE_TYPE = "direct";
	private final static String QUEUE_NAME = "hello";
	private final static boolean QUEUE_DURABLE =false;
	private final static boolean QUEUE_EXCLUSIVE =false;
	private final static boolean QUEUE_AUTO_DELETE =false;
	private final static String ROUTING_KEY = "hello-bind";

	public static void main(String[] argv) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);

		//Declaring the queue if it was not created from the consumers end
		try{
			channel.queueDeclarePassive(QUEUE_NAME);
		} catch (IOException e) {
			channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, QUEUE_DURABLE, QUEUE_EXCLUSIVE, QUEUE_AUTO_DELETE, null);
		}

		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

		String message = "Hello World!";
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");

		channel.close();
		connection.close();
	}
}
