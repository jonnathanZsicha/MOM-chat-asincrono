package sistemas.distribuidos.model;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Reciever implements Runnable{
	
	public QueueReceiver receiver = null;
	private String queueName;

	public QueueReceiver getReceiver() {
		return receiver;
	}

	public void setReceiver(QueueReceiver receiver) {
		this.receiver = receiver;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public Reciever() {
		super();
	}

	public Reciever(String queueName) {
		super();
		this.queueName = queueName;
		QueueConnection con = null;
		QueueSession session = null;

		try {
			InitialContext context = new InitialContext();
			QueueConnectionFactory factory = (QueueConnectionFactory) context.lookup("QueueConnectionFactory");
			System.out.println("factory default " +  factory.toString());
			Queue queue = (Queue) context.lookup(queueName);
			System.out.println("queue " + queue.toString());
			con = factory.createQueueConnection();
			System.out.println("conexion" + con.toString());
			session = con.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			receiver = session.createReceiver(queue);
			con.start();
			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			
			
		}
	}

	@Override
	public void run() {
		while (true) {
			// keep Recieving
		}
		
	}


}
