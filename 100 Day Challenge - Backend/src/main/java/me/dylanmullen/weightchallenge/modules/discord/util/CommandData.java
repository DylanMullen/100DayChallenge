package me.dylanmullen.weightchallenge.modules.discord.util;

import java.util.List;

import org.json.simple.JSONObject;

/**
 * @author Dylan
 * @date 27 Sep 2021
 * @project Drinkers Bot
 * @file beer.drinkers.drinkersbot.modules.bot.commands.CommandData.java
 */
public class CommandData
{

	private String commandName;
	private String description;
	private List<String> aliases;
	private List<String> subcommands;

	public CommandData(String commandName, JSONObject commandJSON)
	{
		this.commandName = commandName;
		init(commandJSON);
	}

	@SuppressWarnings("unchecked")
	private void init(JSONObject commandJSON)
	{
		this.description = (String) commandJSON.get("description");
		if (commandJSON.get("aliases") != null)
			this.aliases = (List<String>) commandJSON.get("aliases");
		if (commandJSON.get("sub-commands") != null)
			this.aliases = (List<String>) commandJSON.get("sub-commands");
	}

	public boolean isCommand(String message)
	{
		return commandName.equalsIgnoreCase(message) || isAlias(message);
	}

	public boolean isAlias(String message)
	{
		for (String alias : aliases)
			if (alias.equalsIgnoreCase(message))
				return true;
		return false;
	}

	/**
	 * @return the commandName
	 */
	public String getCommandName()
	{
		return commandName;
	}

	/**
	 * @return the aliases
	 */
	public List<String> getAliases()
	{
		return aliases;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @return the subcommands
	 */
	public List<String> getSubCommands()
	{
		return subcommands;
	}

}
