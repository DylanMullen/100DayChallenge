package me.dylanmullen.weightchallenge.core;

import java.util.Scanner;

import lombok.Data;
import lombok.Getter;
import me.dylanmullen.weightchallenge.modules.authentication.AuthenticationManager;
import me.dylanmullen.weightchallenge.modules.discord.DiscordManager;
import me.dylanmullen.weightchallenge.modules.users.UserManager;
import me.dylanmullen.weightchallenge.util.config.IOManager;
import me.dylanmullen.weightchallenge.util.mysql.MySQLManager;

@Data
public class App implements Runnable
{

	@Getter
	private Thread thread;
	private boolean running;

	private static App instance;

	private IOManager iOManager;
	private UserManager userManager;
	private AuthenticationManager authManager;
	private MySQLManager mySQLManager;
	private DiscordManager discordManager;
	
	

	public App()
	{
		if (App.instance == null)
			App.instance = this;
	}

	public synchronized boolean start()
	{
		if (getThread() != null || isRunning())
			return false;
		
		this.thread = new Thread(this);
		thread.start();
		return true;
	}

	private void init()
	{
		setIOManager(new IOManager());
		setMySQLManager(new MySQLManager(getIOManager().getConfigController().getConfig("credentials.json")));
		setUserManager(new UserManager());
		setAuthManager(new AuthenticationManager());
	}

	public void run()
	{
		init();
		Scanner scanner = new Scanner(System.in);

		while (isRunning())
		{
			String command = scanner.nextLine();

			switch (command.toLowerCase())
			{
				case "quit":
					setRunning(false);
			}
		}

		scanner.close();
		quit();
	}

	public synchronized boolean quit()
	{
		if (getThread() == null)
			return false;

		try
		{
			getThread().join();
			setThread(null);
			return true;
		} catch (InterruptedException e)
		{
			return false;
		}
	}

	public static App getInstance()
	{
		return App.instance;
	}

}
