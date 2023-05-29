package models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private Map<String, List<Message>> messages = new HashMap<String, List<Message>>();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public User() {}

	@Override
	public String toString() {
		return username + "," + password;
	}

	public Map<String, List<Message>> getMessages() {
		return messages;
	}

	public void setMessages(Map<String, List<Message>> messages) {
		this.messages = messages;
	}
	
	public void addMessage(Message message) {
		List<Message> mes = this.messages.get(message.getSender());
		if (mes == null) {
			mes.add(message);
			this.messages.put(message.getSender(), mes);
		}
		else {
			this.messages.replace(message.getSender(), mes);
		}
	}
	
	

}
