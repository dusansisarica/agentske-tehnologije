package agents;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import agentmanager.AgentManagerRemote;
import chatmanager.ChatManagerRemote;
import messagemanager.MessageManagerRemote;
import models.User;
import usermanager.UserManagerRemote;
import util.JNDILookup;
import ws.WSChat;
/**
 * Sledece nedelje cemo prebaciti poruke koje krajnji korisnik treba da vidi da se 
 * salju preko Web Socketa na front-end (klijentski deo aplikacije)
 * @author Aleksandra
 *
 */
@Stateful
@Remote(Agent.class)
public class UserAgent implements Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String agentId;
	@EJB
	private CachedAgentsRemote cachedAgents;
	@EJB
	private AgentManagerRemote agentManager;
	@EJB
	private WSChat ws;
	@EJB
	private UserManagerRemote userManager;
	@EJB
	private ChatManagerRemote chatManager;
	
	protected MessageManagerRemote msm() {
		return (MessageManagerRemote) JNDILookup.lookUp(JNDILookup.MessageManagerLookup, MessageManagerRemote.class);
	}

	
	@PostConstruct
	public void postConstruct() {
		System.out.println("Created User Agent!");
	}
	
	
	@Override
	public void handleMessage(Message msg) {
		TextMessage tmsg = (TextMessage) msg;
		String receiver = "";
		String sender = "";
		String subject;
		String content;
		LocalDateTime date;
		String response;
		String option = "";
		System.out.println("User Agent handleuje poruku...");
		try {
			option = (String) tmsg.getObjectProperty("command");
			switch (option) {
			case "MESSAGE":
				receiver = (String) tmsg.getObjectProperty("receiverUser");
				sender = (String) tmsg.getObjectProperty("sender");
				subject = (String) tmsg.getObjectProperty("subject");
				content = (String) tmsg.getObjectProperty("content");
				//date = (LocalDateTime) tmsg.getObjectProperty("date");
				List<User> iterateUsers = chatManager.registeredUsers();
				if (receiver.equals("all")) {
					//receiver = u.getUsername();
					models.Message message = new models.Message(sender, receiver, subject, content, LocalDateTime.now());
					sendMessageToAll(message);
					response = "RECEIVED_MESSAGE!" + content;
					ws.onMessage(response);
				}
				else {
					//sendMessage(new models.Message(sender, receiver, subject, content, LocalDateTime.now()));
					models.Message message = new models.Message(sender, receiver, subject, content, LocalDateTime.now());
					sendMessage(message);
					List<User> provera = chatManager.registeredUsers();
					String receivedMsg = "RECEIVED_MESSAGE!: " + content;
					String sentMsg = "SENT_MESSAGE!: " + content;
					ws.onMessage(receiver, receivedMsg);
					ws.onMessage(sender, sentMsg);
				}
				
				break;
			case "GET_MESSAGES":
				response = "GET_MESSAGES!";
				String user1 = (String) tmsg.getObjectProperty("user1");
				String user2 = (String) tmsg.getObjectProperty("user2");
				List<User> users = userManager.getLoggedIn();
				for (User u : chatManager.registeredUsers()) {
					if (u.getUsername().equals(user1)) {
						List<models.Message> messages = u.getMessages().get(user2);
						for (models.Message m : messages) {
							response += m.getContent() + ":" + m.getSender() + ":" + m.getDate().toString() + "|";
						}
					}
				}
				ws.onMessage(user1, response);
			default:
				response = "ERROR!Option: " + option + " does not exist.";
				break;
			}
			
		} catch (JMSException e) {
			e.printStackTrace();
		}	}

	@Override
	public String init(String name) {
		//agentId = generateId();
		//agentManager.startAgent(name);
		cachedAgents.addRunningAgent(name, this);
		return name;
	}

	private String generateId() {
		Random r = new Random();
		int low = 10;
		int high = 100;
		return Integer.toString(r.nextInt(high - low) + low);
	}

	@Override
	public String getAgentId() {
		return agentId;
	}

	@Override
	public String init() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void sendMessageToAll(models.Message message) {
		List<User> registered = chatManager.registeredUsers();
		int index = 0;
		for (User u : registered) {
			if (u.getUsername().equals(message.getSender())) { 
				for (User us : registered) {
					message.setReceiver(us.getUsername());
					addMessageSender(u, message);
					chatManager.changeRegisteredUser(index, u);
				}
			}
			else {
				addMessageReceiver(u, message);
			}
			chatManager.changeRegisteredUser(index, u);
			index++;
		}
	}
	
	public void sendMessage(models.Message message) {
		List<User> registered = chatManager.registeredUsers();
		User newUserSender = new User();
		User newUserReceiver = new User();
		int indexSender = 0;
		int indexReceiver = 0;
		int index = 0;
		for(User u : chatManager.registeredUsers()) {
			if (u.getUsername().equals(message.getSender())) {
				addMessageSender(u, message);
				newUserSender = u;
//				for (User us : registered) {
//					if (us.getUsername().equals(u.getUsername())) {
////						registered.remove(us);
////						registered.add(u);
//					}
//				}
				//chatManager.registeredUsers().remove(newUser)
				indexSender = index;
				System.out.println("aaaa");
			}
			else if (u.getUsername().equals(message.getReceiver())) {
				addMessageReceiver(u, message);
				newUserReceiver = u;
				for (User us : registered) {
					if (us.getUsername().equals(u.getUsername())) {
//						registered.remove(us);
//						registered.add(u);
					}
				}
				indexReceiver = index;
				System.out.println("aaaa");
			}
		//chatManager.setRegisteredUsers(registered);
			index++;
		}
//		chatManager.registeredUsers().set(indexSender, newUserSender);
//		chatManager.registeredUsers().set(indexReceiver, newUserReceiver);
		chatManager.changeRegisteredUser(indexReceiver, newUserReceiver);
		chatManager.changeRegisteredUser(indexSender, newUserSender);
		
	}
	public List<User> getLoggedIn() {
		// TODO Auto-generated method stub
		return chatManager.loggedInUsers();
	}
	
	public void addMessageSender(User u, models.Message message) {
		List<models.Message> mes = u.getMessages().get(message.getReceiver());
		if (mes == null) {
			mes = new ArrayList<models.Message>();
			mes.add(message);
			u.getMessages().put(message.getReceiver(), mes);
		}
		else {
			mes.add(message);
			u.getMessages().replace(message.getReceiver(), mes);
		}
	}
	
	public void addMessageReceiver(User u, models.Message message) {
		List<models.Message> mes = u.getMessages().get(message.getSender());
		if (mes == null) {
			mes = new ArrayList<models.Message>();
			mes.add(message);
			u.getMessages().put(message.getSender(), mes);
		}
		else {
			mes.add(message);
			u.getMessages().replace(message.getSender(), mes);
		}
	}

}
