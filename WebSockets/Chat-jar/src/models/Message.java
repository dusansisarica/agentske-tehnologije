package models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	private String sender;
	private String receiver;
	private String subject;
	private String content;
	private LocalDateTime date;
	public Message(String sender, String receiver, String subject, String content, LocalDateTime date) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.subject = subject;
		this.content = content;
		this.date = date;
	}
	public Message() {
		super();
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

	

}
