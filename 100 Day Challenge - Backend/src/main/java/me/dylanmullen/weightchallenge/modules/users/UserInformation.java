package me.dylanmullen.weightchallenge.modules.users;

import java.sql.SQLException;

import org.json.simple.JSONObject;

import lombok.Data;
import me.dylanmullen.weightchallenge.util.mysql.SQLFactory;
import me.dylanmullen.weightchallenge.util.mysql.callbacks.SQLCallback;
import me.dylanmullen.weightchallenge.util.mysql.util.SQLTicket;

@Data
public class UserInformation
{

	public enum WeightType
	{
		WEIGHT("weight", 0), MUSCLE("muscle_mass", 1), FAT("fat_mass", 2), WATER("water_mass", 3);

		private String column;
		private int id;

		private WeightType(String column, int id)
		{
			this.column = column;
			this.id = id;
		}

		public String getColumn()
		{
			return column;
		}

		public int getId()
		{
			return id;
		}

		public static WeightType getType(int id)
		{
			for (WeightType type : WeightType.values())
				if (type.id == id)
					return type;
			return null;
		}
	}

	private long discordID;
	private int age;

	private double weight;
	private double height;
	private double muscleMass;
	private double fatMass;
	private double waterMass;

	public UserInformation(long discordID)
	{
		this.discordID = discordID;
		loadInformation(discordID);
	}

	/**
	 * Loads a Users data from the database using their DiscordID to find their row.
	 * If there is no records, it leaves the data as their default values.
	 * 
	 * @param discordID User's DiscordID
	 */
	private void loadInformation(long discordID)
	{
		SQLTicket ticket = SQLFactory.selectData("users", "*", new String[] { "id" }, new String[] { discordID + "" },
				new SQLCallback()
				{

					@Override
					public boolean callback()
					{
						try
						{
							if (!hasEntries())
								return false;

							setAge(result.getInt("age"));
							setWeight(result.getDouble("weight"));
							setHeight(result.getDouble("height"));
							setMuscleMass(result.getDouble("muscle_mass"));
							setFatMass(result.getDouble("fat_mass"));
							setWaterMass(result.getDouble("water_mass"));

						} catch (SQLException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return false;
					}
				});
		SQLFactory.sendTicket(ticket);
	}

	public boolean setWeights(double[] weights)
	{
		try
		{
			setWeight(weights[0]);
			setFatMass(weights[1]);
			setMuscleMass(weights[2]);
			setWaterMass(weights[3]);
			
			SQLTicket ticket = SQLFactory.updateData("users", "id=" + discordID,
					new String[] { "weight", "fat_mass", "muscle_mass", "water_mass" },
					new String[] { getWeight() + "", getFatMass() + "", getMuscleMass() + "", getWaterMass() + "" }, null);
			SQLFactory.sendTicket(ticket);
			return true;
		}catch(Exception e)
		{
			return false;
		}
	}

	public boolean updateWeights(WeightType type, double value)
	{
		switch (type)
		{
			case WEIGHT:
				setWeight(value);
				break;
			case FAT:
				setFatMass(value);
				break;
			case MUSCLE:
				setMuscleMass(value);
				break;
			case WATER:
				setWaterMass(value);
				break;
			default:
				return false;
		}

		updateDatabase(type, value);
		return true;
	}

	private void updateDatabase(WeightType type, double value)
	{
		SQLTicket ticket = SQLFactory.updateData("users", "id=" + getDiscordID(), new String[] { type.getColumn() },
				new String[] { value + "" }, null);
		SQLFactory.sendTicket(ticket);
	}

	@SuppressWarnings("unchecked")
	public JSONObject getWeightJSON()
	{
		JSONObject weightData = new JSONObject();
		weightData.put("totalWeight", getWeight());
		weightData.put("fatMass", getFatMass());
		weightData.put("muscleMass", getMuscleMass());
		weightData.put("waterMass", getWaterMass());
		return weightData;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON()
	{
		JSONObject json = new JSONObject();
		json.put("age", getAge());
		json.put("height", getHeight());
		json.put("weight", getWeightJSON());
		return json;
	}

}
