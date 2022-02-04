package me.dylanmullen.weightchallenge.modules.discord.util;

import me.dylanmullen.weightchallenge.core.App;
import me.dylanmullen.weightchallenge.modules.discord.DiscordManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * @author Dylan
 * @date 27 Sep 2021
 * @project drinkersbot
 * @file beer.drinkers.drinkersbot.modules.bot.commands.Command.java
 */
public abstract class Command
{

	private CommandData data;

	private Member author;
	private Guild guild;

	private MessageChannel channel;
	private Message cmdMessage;

	public Command(CommandData data, Message cmdMessage)
	{
		this.data = data;
		this.cmdMessage = cmdMessage;
		this.author = cmdMessage.getMember();
		this.guild = cmdMessage.getGuild();
		this.channel = cmdMessage.getChannel();
	}

	public abstract void run();

	public void sendMessage(String message)
	{
		getMessageChannel().sendMessage(message).queue();
	}

	public void sendMessage(EmbedBuilder message)
	{
		getMessageChannel().sendMessageEmbeds(message.build()).queue();
	}

	public void sendPrivateMessage(User user, String message)
	{
		user.openPrivateChannel().queue((channel) ->
		{
			channel.sendMessage(message).queue();
		});
	}

	public void sendPrivateMessage(User user, EmbedBuilder message)
	{
		user.openPrivateChannel().queue((channel) ->
		{
			channel.sendMessageEmbeds(message.build()).queue();
		});
	}

	public Member getAuthor()
	{
		return getCommandMessage().getMember();
	}

	public DiscordManager getDiscordManager()
	{
		return App.getInstance().getDiscordManager();
	}

	/**
	 * @return the cmdMessage
	 */
	public Message getCommandMessage()
	{
		return cmdMessage;
	}

	/**
	 * @return the guild
	 */
	public Guild getGuild()
	{
		return guild;
	}

	/**
	 * @return the channel
	 */
	public MessageChannel getMessageChannel()
	{
		return channel;
	}

	public String[] getArguments()
	{
		return cmdMessage.getContentRaw().substring(cmdMessage.getContentRaw().indexOf(" ") + 1).split(" ");
	}

	/**
	 * @return the data
	 */
	public CommandData getData()
	{
		return data;
	}

	public boolean isPrivateMessage()
	{
		return channel.getType() == ChannelType.PRIVATE;
	}

	public boolean isMemberAdmin()
	{
		return author.hasPermission(Permission.ADMINISTRATOR);
	}

}
