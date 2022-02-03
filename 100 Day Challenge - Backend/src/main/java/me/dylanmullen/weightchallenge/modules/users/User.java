package me.dylanmullen.weightchallenge.modules.users;

import lombok.Data;

@Data
public class User
{

	private long discordID;
	private UserInformation information;
	
	public User(long discordID)
	{
		this.discordID = discordID;
		this.information = new UserInformation(discordID);
	}
}
