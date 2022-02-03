package me.dylanmullen.weightchallenge.modules.users;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class UserManager
{

	// DiscordID : User
	@Getter
	private Map<Long, User> users;

	public UserManager()
	{
		this.users = new HashMap<>();
	}
	
	public void createUser(long discordID)
	{
		getUsers().put(discordID, new User(discordID));
	}
	
	public User getUser(long discordID)
	{
		return getUsers().get(discordID);
	}

}
