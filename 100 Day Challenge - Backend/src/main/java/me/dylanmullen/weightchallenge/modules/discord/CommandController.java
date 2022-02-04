package me.dylanmullen.weightchallenge.modules.discord;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import me.dylanmullen.weightchallenge.modules.discord.util.Command;
import me.dylanmullen.weightchallenge.modules.discord.util.CommandData;
import me.dylanmullen.weightchallenge.util.config.Config;
import net.dv8tion.jda.api.entities.Message;

/**
 * @author Dylan
 * @date 27 Sep 2021
 * @project Drinkers Bot
 * @file beer.drinkers.drinkersbot.modules.bot.commands.CommandHandler.java
 */
public class CommandController
{

	private Config config;
	private List<String> prefixes;
	private Map<CommandData, Class<? extends Command>> commands;

	public CommandController(Config config)
	{
		this.config = config;
		this.prefixes = new ArrayList<>();
		this.commands = new HashMap<CommandData, Class<? extends Command>>();
		init();
	}

	public void handle(Message message)
	{
		Command command = null;
		String[] arguments = message.getContentRaw().split(" ");
		if (arguments.length == 1)
			command = null;
		else
		{
			for (CommandData data : commands.keySet())
				if (data.isCommand(arguments[1]))
				{
					command = initialiseCommand(data, commands.get(data), message);
				}
		}

		if (command != null)
			command.run();
	}

	private Command initialiseCommand(CommandData data, Class<? extends Command> clazz, Message message)
	{
		try
		{
			Constructor<? extends Command> constructor = clazz.getConstructor(CommandData.class, Message.class);
			return constructor.newInstance(data, message);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private void init()
	{
		loadPrefixes();
		loadCommands();
	}

	@SuppressWarnings("unchecked")
	private void loadPrefixes()
	{
		JSONArray list = (JSONArray) config.getValue("settings", "prefixes");
		this.prefixes = list;
	}

	@SuppressWarnings("unchecked")
	private void loadCommands()
	{
		JSONObject commands = config.getJSONObject("commands");

		List<CommandData> data = new ArrayList<>();

		for (String key : (Set<String>) commands.keySet())
		{
			data.add(new CommandData(key, (JSONObject) commands.get(key)));
		}

		registerCommands(data);
	}

	private void registerCommands(List<CommandData> data)
	{
	}

	private CommandData getData(List<CommandData> data, String command)
	{
		for (CommandData cmd : data)
			if (cmd.getCommandName().equalsIgnoreCase(command))
				return cmd;
		return null;
	}

	public boolean isCommandPrefix(String message)
	{
		for (String prefix : prefixes)
			if (message.startsWith(prefix))
				return true;
		return false;
	}

	/**
	 * @return the prefixes
	 */
	public List<String> getPrefixes()
	{
		return prefixes;
	}

}
