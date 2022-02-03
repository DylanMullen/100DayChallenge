package me.dylanmullen.weightchallenge.util.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Config
{

	private String name;
	private File file;

	private JSONObject jsonFile;
	private Map<String, JSONObject> objects;
	private boolean loaded;

	public Config(String name, String path)
	{
		this(name, new File(path));
	}

	public Config(String name, File configFile)
	{
		this.name = name;
		this.file = configFile;
		this.objects = new HashMap<String, JSONObject>();
		this.loaded = load();

	}

	public void storeObject(Object parentKey, JSONObject object)
	{
		if (!(parentKey instanceof String))
		{
			return;
		}
		for (Object key : object.keySet())
		{
			if (!(key instanceof String))
				continue;
			Object jo = object.get(key);
			if (jo instanceof JSONObject)
			{
				objects.put(parentKey + "." + (String) key, (JSONObject) jo);
				storeObject(parentKey + "." + (String) key, (JSONObject) jo);
			}
		}
	}

	public JSONObject getJSONObject(String key)
	{
		return objects.get(key);
	}

	public Object getValue(String objectKey, String valueKey)
	{
		JSONObject obj = getJSONObject(objectKey);
		if (obj == null)
			return null;
		return obj.get(valueKey);
	}

	private boolean load()
	{
		JSONParser jsonParser = new JSONParser();
		FileReader fileReader = null;
		try
		{
			fileReader = new FileReader(file);
			JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);

			for (Object obj : jsonObject.keySet())
			{
				objects.put((String) obj, (JSONObject) jsonObject.get(obj));
				JSONObject j = (JSONObject) jsonObject.get(obj);
				storeObject(obj, j);
			}

			this.jsonFile = jsonObject;
		} catch (ParseException | IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				if (fileReader != null)
					fileReader.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return jsonFile == null;
	}

	@SuppressWarnings("unchecked")
	public void write(String objectKey, String valueKey, Object object)
	{
		JSONObject json = getJSONObject(objectKey);
		if (json == null)
			return;

		json.put(valueKey, object);
		try (FileWriter writer = new FileWriter(file))
		{
			writer.append(getJsonFile().toJSONString());
			writer.flush();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public Map<String, JSONObject> getObjects()
	{
		return objects;
	}

	public String getName()
	{
		return name;
	}

	public boolean isLoaded()
	{
		return loaded;
	}

	public JSONObject getJsonFile()
	{
		return jsonFile;
	}
}
