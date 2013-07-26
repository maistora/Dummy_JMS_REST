package com.maistora.test.jms;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(mappedName = "jms/SimpleQueue")
public class SimpleMDB implements MessageListener {

	public static class MessagePatch {

		private Map<String, MessagePropertyPatch<?>> properties = new HashMap<String, SimpleMDB.MessagePropertyPatch<?>>();
		private TextMessage mesg;

		public MessagePatch(TextMessage mesg) {
			this.mesg = mesg;
		}
		
		public void addStringProperty(final String name, final String value) throws Exception {
			final String defaultValue = mesg.getStringProperty(name);
			
			properties.put(name, MessagePropertyPatch.forStringField(name, value, defaultValue));
		}

		public void addBooleanProperty(final String name, final Boolean value) throws Exception {
			final Boolean defaultValue = mesg.getBooleanProperty(name);
			
			properties.put(name, MessagePropertyPatch.forBooleanField(name, value, defaultValue));
		}

		public void addObjectProperty(final String name, final Object value) throws Exception {
			final Object defaultValue = mesg.getObjectProperty(name);
			
			properties.put(name, MessagePropertyPatch.forObjectField(name, value, defaultValue));
		}

		public void applyTo(Message mesg) throws Exception {
			for (MessagePropertyPatch<?> propertyPatch : properties.values()) {
				propertyPatch.applyTo(mesg);
			}
		}
	}
	
	public abstract static class MessagePropertyPatch<T> {
		private String name;
		private T	  value;
		private T	  defaultValue;

		public MessagePropertyPatch(String name, T value, T defaultValue) {
			this.name = name;
			this.value = value;
			this.defaultValue = defaultValue;
		}

		public T getValue() {
			return value == null ? defaultValue : value;
		}

		public String getName() {
			return name;
		}

		public abstract void applyTo(Message mesg) throws JMSException;

		public static MessagePropertyPatch<String> forStringField(String name, String value, String defaultValue) {
			return new MessagePropertyPatch<String>(name, value, defaultValue) {

				@Override
				public void applyTo(Message mesg) throws JMSException {
					mesg.setStringProperty(getName(), getValue());
				}

			};
		}

		public static MessagePropertyPatch<Boolean> forBooleanField(String name, Boolean value, Boolean defaultValue) {
			return new MessagePropertyPatch<Boolean>(name, value, defaultValue) {

				@Override
				public void applyTo(Message mesg) throws JMSException {
					mesg.setBooleanProperty(getName(), getValue());
				}

			};
		}

		public static MessagePropertyPatch<Object> forObjectField(String name, Object value, Object defaultValue) {
			return new MessagePropertyPatch<Object>(name, value, defaultValue) {

				@Override
				public void applyTo(Message mesg) throws JMSException {
					mesg.setObjectProperty(getName(), getValue());
				}

			};
		}

	}

	private static final String PROPERTY_NAME = "ASDF";

	@Override
	public void onMessage(Message message) {
		try {
			final TextMessage textMsg = (TextMessage) message;
//			textMsg.clearProperties();
//			
//			textMsg.setStringProperty("BUUU", "Fuck!");
			
			MessagePatch patch = new MessagePatch(textMsg);
//			patch.addStringProperty("asdffffff", null);
			patch.addStringProperty("BUUU", "ioooo");
			patch.addStringProperty(PROPERTY_NAME, "are you crazyyyyyY???");
			
			textMsg.clearProperties();
			patch.applyTo(textMsg);
			
//			textMsg.clearProperties();
//			System.out.println("Message received: " + textMsg.getText() + "@" + System.currentTimeMillis());
//			
//			textMsg.setStringProperty(PROPERTY_NAME, "asdfasdfasdfasdf");
//			System.out.println(textMsg.getStringProperty(PROPERTY_NAME));
//			
//			textMsg.setStringProperty(PROPERTY_NAME, "ffffffffffffffffffF");
//			System.out.println(textMsg.getStringProperty(PROPERTY_NAME));
//			
//			textMsg.clearProperties();
			System.out.println(textMsg.getStringProperty(PROPERTY_NAME));
			System.out.println(textMsg.getStringProperty("BUUU"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
