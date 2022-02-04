package me.dylanmullen.weightchallenge.modules.discord.util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageActivity.Application;

/**
 * @author Dylan
 * @date 28 Sep 2021
 * @project Drinkers Bot
 * @file beer.drinkers.drinkersbot.modules.bot.commands.Subcommand.java
 */
public abstract class Subcommand
{

	private Command command;

	public Subcommand(Command command)
	{
		this.command = command;
	}
	
	public abstract void run();

	/**
	 * @return the command
	 */
	public Command getCommand()
	{
		return command;
	}
	
	public JDA getBot()
	{
		return command.getDiscordManager().getBot();
	}
	
	public Member getBotMember()
	{
		return getCommand().getGuild().getMember(getBot().getSelfUser());
	}

	public String[] getArguments()
	{
		return getCommand().getArguments();
	}
}
