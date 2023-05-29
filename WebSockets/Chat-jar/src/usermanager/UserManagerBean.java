package usermanager;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Stateless;

import agentmanager.AgentManagerBean;
import chatmanager.ChatManagerBean;
import chatmanager.ChatManagerRemote;
import models.Message;
import models.User;
import util.JNDILookup;

@Stateless
@LocalBean
public class UserManagerBean implements UserManagerLocal, UserManagerRemote{

	@EJB
	private ChatManagerRemote chatManager;// = JNDILookup.lookUp(JNDILookup.ChatManagerLookup, ChatManagerBean.class);
	
	public UserManagerBean() {
		
	}
	@Override
	public void sendMessage(Message message) {
		List<User> registered = chatManager.registeredUsers();
		for(User u : chatManager.registeredUsers()) {
			if (u.getUsername().equals(message.getSender())) {
				addMessageSender(u, message);
				for (User us : registered) {
					if (us.getUsername().equals(u.getUsername())) {
						registered.remove(us);
						registered.add(u);
					}
				}
			
				System.out.println("aaaa");
			}
			else if (u.getUsername().equals(message.getReceiver())) {
				addMessageReceiver(u, message);
				for (User us : registered) {
					if (us.getUsername().equals(u.getUsername())) {
						registered.remove(us);
						registered.add(u);
					}
				}
				System.out.println("aaaa");
			}
		chatManager.setRegisteredUsers(registered);
		}
		
	}
	@Override
	public List<User> getLoggedIn() {
		// TODO Auto-generated method stub
		return chatManager.loggedInUsers();
	}
	
	@Override
	public void addMessageSender(User u, Message message) {
		List<Message> mes = u.getMessages().get(message.getReceiver());
		if (mes == null) {
			mes = new ArrayList<Message>();
			mes.add(message);
			u.getMessages().put(message.getReceiver(), mes);
		}
		else {
			u.getMessages().replace(message.getReceiver(), mes);
		}
	}
	
	@Override
	public void addMessageReceiver(User u, Message message) {
		List<Message> mes = u.getMessages().get(message.getSender());
		if (mes == null) {
			mes = new ArrayList<Message>();
			mes.add(message);
			u.getMessages().put(message.getSender(), mes);
		}
		else {
			u.getMessages().replace(message.getSender(), mes);
		}
	}


	

}
