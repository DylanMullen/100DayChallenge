package me.dylanmullen.weightchallenge.modules.users;

import org.json.simple.JSONObject;

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
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON()
	{
		JSONObject json = new JSONObject();
		json.put("discordID", getDiscordID());
		json.put("userInformation", information.toJSON());
		return json;
	}
}
