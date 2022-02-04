package me.dylanmullen.weightchallenge.modules.users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
	
	@SuppressWarnings("unchecked")
	public JSONObject getUsersJSON()
	{
		JSONObject json = new JSONObject();
		for (long discordID : getUsers().keySet())
		{
			User user = getUsers().get(discordID);
			json.put(discordID + "", user.toJSON());
		}
		return json;
	}

}
