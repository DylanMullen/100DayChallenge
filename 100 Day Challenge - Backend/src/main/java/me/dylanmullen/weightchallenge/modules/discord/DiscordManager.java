package me.dylanmullen.weightchallenge.modules.discord;

import javax.security.auth.login.LoginException;

import lombok.Data;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import okhttp3.OkHttpClient;

@Data
public class DiscordManager
{

	private JDA bot;

	public DiscordManager(String discordToken)
	{
		init(discordToken);
	}

	private void init(String discordToken)
	{
		createBot(discordToken);
	}

	private void createBot(String discordToken)
	{
		try
		{
			JDABuilder builder = JDABuilder.createDefault(discordToken);
			builder.setActivity(Activity.playing("Drinkers?"));
			setBot(builder.build());
		} catch (LoginException e)
		{
			System.err.println("[Error] Failed to login to Discord API");
		}
	}

	public void dispose()
	{
		OkHttpClient client = getBot().getHttpClient();
		client.connectionPool().evictAll();
		client.dispatcher().executorService().shutdown();
		getBot().shutdownNow();
	}
}
