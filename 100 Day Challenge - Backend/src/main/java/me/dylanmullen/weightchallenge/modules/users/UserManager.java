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
	
	/**
	 * Authenticates with Discord that the access token provided is acceptable.
	 * If it is, the discordID is returned. If not an error -1 is returned.
	 * 
	 * @param accessToken User's Discord Access Token
	 * @return DiscordID OR -1
	 */
	public long authenticateWithDiscord(String accessToken)
	{
		try
		{
			URL url = new URL("https://discord.com/api/v9/users/@me");
			HttpURLConnection connect = (HttpURLConnection) url.openConnection();
			connect.setRequestMethod("GET");
			connect.setRequestProperty("Authorization", "Bearer " + accessToken);

			InputStream is = connect.getInputStream();
			if (connect.getResponseCode() != 200)
			{
				is.close();
				return -1;
			}

			JSONObject json = getDiscordJSONResponse(is);
			return (long) json.get("id");

		} catch (IOException e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	private JSONObject getDiscordJSONResponse(InputStream is)
	{
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(is));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = reader.readLine()) != null)
			{
				content.append(inputLine);
			}

			is.close();

			return (JSONObject) new JSONParser().parse(content.toString());

		} catch (IOException | ParseException e)
		{
			e.printStackTrace();
		} finally
		{
			if (reader != null)
				try
				{
					reader.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
		}
		return null;
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
