package me.dylanmullen.weightchallenge.core;

import lombok.Data;
import lombok.Getter;
import me.dylanmullen.weightchallenge.modules.users.UserManager;

@Data
public class App implements Runnable
{

	@Getter
	private Thread thread;
	private boolean running;
	
	private static App instance;
	
	private UserManager userManager;

	public App()
	{
		if(App.instance == null)
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

	public void run()
	{
		
	}
	
	public static App getInstance()
	{
		return App.instance;
	}
	
}
