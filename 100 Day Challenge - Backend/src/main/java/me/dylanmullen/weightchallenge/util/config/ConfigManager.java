package me.dylanmullen.weightchallenge.util.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dylan
 * @date 27 Sep 2021
 * @project Drinkers Bot
 * @file beer.drinkers.drinkersbot.modules.io.config.ConfigController.java
 */
public class ConfigManager
{
	
	private File folder;
	private List<Config> configs;

	public ConfigManager(File folder)
	{
		this.folder = folder;
		this.configs = new ArrayList<>();
		init();
	}
	
	private void init()
	{
		loadConfigs();
	}
	
	private void loadConfigs()
	{
		for(File config : folder.listFiles())
			configs.add(new Config(config.getName(), config));
	}
	
	public Config getConfig(String name)
	{
		for(Config config : configs)
			if(config.getName().equals(name))
				return config;
		return null;
	}

	/**
	 * @return the folder
	 */
	public File getFolder()
	{
		return folder;
	}
	
}
