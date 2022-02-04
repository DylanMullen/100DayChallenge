package me.dylanmullen.weightchallenge.util.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Dylan
 * @date 27 Sep 2021
 * @project drinkersbot
 * @file beer.drinkers.drinkersbot.modules.io.IOController.java
 */
public class IOManager
{

	private Map<String, File> folders;
	
	private ConfigManager configManager;

	public IOManager()
	{
		this.folders = new HashMap<>();
		init();
	}

	private void init()
	{
		loadFolders();
		this.configManager = new ConfigManager(folders.get("configs"));
	}

	private void loadFolders()
	{
		loadFolder("configs", "configs");
	}

	private void loadFolder(String folder, String resourcePath)
	{
		File file = new File(folder);
		if (!file.exists())
			file.mkdirs();

		loadFolderContents(file, resourcePath);
		folders.put(folder, file);
		System.out.println("[INFO] Loaded resource folder: " + folder);
	}

	private void loadFolderContents(File diskFolder, String folderPath)
	{
		JarFile jar = null;
		try
		{
			jar = new JarFile(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
			Enumeration<JarEntry> entries = jar.entries();

			while (entries.hasMoreElements())
			{
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				if (name.startsWith(folderPath))
				{
					createFile(jar, entry, diskFolder, name.split(folderPath)[1]);
				}
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				if (jar != null)
					jar.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}

		}
	}

	private void createFile(JarFile jar, JarEntry entry, File diskFolder, String fileName)
	{
		File file = new File(diskFolder.getPath() + File.separator + fileName);
		if (file.exists())
			return;

		InputStream is = null;
		FileOutputStream fs = null;
		try
		{
			file.createNewFile();

			is = jar.getInputStream(entry);
			fs = new FileOutputStream(file);

			while (is.available() > 0)
				fs.write(is.read());
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				if (is != null)
					is.close();
				if (fs != null)
					fs.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public File getFolder(String name)
	{
		return folders.get(name);
	}

	public boolean folderExists(String name)
	{
		return folders.get(name) != null;
	}
	
	/**
	 * @return the controller
	 */
	public ConfigManager getConfigController()
	{
		return configManager;
	}

}
