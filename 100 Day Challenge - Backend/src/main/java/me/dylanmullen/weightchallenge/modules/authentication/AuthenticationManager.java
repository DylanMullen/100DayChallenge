package me.dylanmullen.weightchallenge.modules.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import lombok.Getter;
import me.dylanmullen.weightchallenge.core.App;
import me.dylanmullen.weightchallenge.util.mysql.SQLFactory;
import me.dylanmullen.weightchallenge.util.mysql.callbacks.SQLCallback;
import me.dylanmullen.weightchallenge.util.mysql.util.SQLTicket;

public class AuthenticationManager
{

	@Getter
	private Map<Long, String> authCodes;

	private final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!?";
	private final int LENGTH = 16;

	public AuthenticationManager()
	{
		this.authCodes = new HashMap<>();
		load();
	}

	private void load()
	{
		SQLTicket ticket = SQLFactory.selectData("authentication", "*", new String[] { "1" }, new String[] { "1" },
				new SQLCallback()
				{

					@Override
					public boolean callback()
					{
						if (!hasEntries())
							return false;

						try
						{
							do
							{
								try
								{
									getAuthCodes().put(getResult().getLong("id"), getResult().getString("auth_code"));
								} catch (SQLException e)
								{
									continue;
								}
							} while (getResult().next());
							return true;
						} catch (SQLException e)
						{
							return false;
						}
					}
				});
		SQLFactory.sendTicket(ticket);
	}

	public boolean authorised(long discordID, String authCode)
	{
		if(authCode == null)
			return false;
		
		if (!authCodes.containsKey(discordID))
			return false;
		return authCodes.get(discordID).equals(authCode);
	}

	/**
	 * Retrieves users authentication code. If they do not have one, it creates one.
	 * 
	 * @param discordID User's DiscordID
	 * @return Authentication code
	 */
	public String getAuthCode(long discordID)
	{
		if (authCodes.get(discordID) != null)
			return authCodes.get(discordID);
		return generateAuthCode(discordID);
	}

	/**
	 * Generates a new Authentication code for a user. If a user already has an
	 * authentication code, it sends that one instead of making a new one.
	 * 
	 * @param discordID User's DiscordID
	 * @return Authentication Code
	 */
	public String generateAuthCode(long discordID)
	{
		if (getAuthCodes().get(discordID) != null)
			return getAuthCodes().get(discordID);

		StringBuilder builder = new StringBuilder();
		Random random = new Random(System.currentTimeMillis());

		for (int i = 0; i < LENGTH; i++)
			builder.append(CHARACTERS.charAt(random.nextInt(LENGTH)));

		storeCode(discordID, builder.toString());
		return builder.toString();
	}

	/**
	 * Stores the Authentication code of a user on the database.
	 * 
	 * @param discordID User's DiscordID
	 * @param authcode  Authentication code
	 */
	private void storeCode(long discordID, String authcode)
	{
		SQLTicket ticket = SQLFactory.insertData("authentication", new String[] { "id", "auth_code" },
				new String[] { discordID + "", authcode }, new SQLCallback()
				{

					@Override
					public boolean callback()
					{
						getAuthCodes().put(discordID, authcode);
						return false;
					}
				});
		SQLFactory.sendTicket(ticket);
	}

	/**
	 * Authenticates with Discord that the access token provided is acceptable. If
	 * it is, the discordID is returned. If not an error -1 is returned.
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

}
