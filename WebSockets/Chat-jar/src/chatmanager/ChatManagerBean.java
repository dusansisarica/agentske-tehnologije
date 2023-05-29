package chatmanager;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Stateful;

import agentmanager.AgentManagerRemote;
import models.User;
import util.JNDILookup;

// TODO Implement the rest of Client-Server functionalities 
/**
 * Session Bean implementation class ChatBean
 */
@Singleton
@LocalBean
public class ChatManagerBean implements ChatManagerRemote, ChatManagerLocal {

	private List<User> registered = new ArrayList<User>();
	private List<User> loggedIn = new ArrayList<User>();
	@EJB
	private AgentManagerRemote agentManager;
	
	/**
	 * Default constructor.
	 */
	public ChatManagerBean() {
	}

	@Override
	public boolean register(User user) {
		registered.add(user);
		return true;
	}

	@Override
	public boolean login(String username, String password) {
		boolean exists = registered.stream().anyMatch(u->u.getUsername().equals(username) && u.getPassword().equals(password));
		if(exists) {
			loggedIn.add(new User(username, password));
			agentManager.startAgent(JNDILookup.UserAgentLookup, username);
		}
		return exists;
	}

	@Override
	public List<User> loggedInUsers() {
		return loggedIn;
	}

	@Override
	public boolean logout(String username) {
		for(User u : loggedIn){
			if (u.getUsername().equals(username)) {
				loggedIn.remove(u);
				return true;
			}
		}
		return false;
	}

	@Override
	public List<User> registeredUsers() {
		return registered;
	}

	@Override
	public void setRegisteredUsers(List<User> users) {
		registered = users;
	}

	@Override
	public void changeRegisteredUser(int index, User newUser) {
		registered.set(index, newUser);
		System.out.println("Provera");
		//chatManager.registeredUsers().set(indexReceiver, newUserReceiver);

	}
	

}
