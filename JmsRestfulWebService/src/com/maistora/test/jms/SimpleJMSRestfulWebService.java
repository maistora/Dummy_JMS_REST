package com.maistora.test.jms;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Stateless
@Path("/jms")
public class SimpleJMSRestfulWebService {

	@Resource(mappedName = "jms/SimpleConnectionFactory")	// = connection factorie's JNDI name
	private ConnectionFactory connectionFactory;

	@Resource(mappedName = "jms/SimpleQueue")				// = Queue's JNDI name
	private Queue queue;

	@GET
	@Path("/sendMsg")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String putMessage() throws Exception {
		System.out.println("called putMessage");
		
		final Connection connection = connectionFactory.createConnection();
		final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		final MessageProducer messageProducer = session.createProducer(queue);
		
		final long start = System.currentTimeMillis();

		for (int i = 0; i < 1; i++) {
			final TextMessage message = session.createTextMessage();
			message.setText(i + ". Hello, JMS! #" + start);
			messageProducer.send(message);
		}
		
		final long end = System.currentTimeMillis();
		
		return "JMS produced 1000 messages within " + (end - start) + "ms";
	}
}
