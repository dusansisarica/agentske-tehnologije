package chatmanager;

import java.util.List;

import javax.ejb.Remote;

import models.User;

@Remote
public interface ChatManagerRemote {

	public boolean login(String username, String password);
	
	public boolean logout(String username);

	public boolean register(User user);

	public List<User> loggedInUsers();
	
	public List<User> registeredUsers();
	public void setRegisteredUsers(List<User> users);
	public void changeRegisteredUser(int index, User newUser);
}
