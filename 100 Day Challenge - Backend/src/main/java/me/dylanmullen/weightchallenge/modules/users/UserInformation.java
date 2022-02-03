package me.dylanmullen.weightchallenge.modules.users;

import java.sql.SQLException;

import lombok.Data;
import me.dylanmullen.weightchallenge.util.mysql.SQLFactory;
import me.dylanmullen.weightchallenge.util.mysql.callbacks.SQLCallback;
import me.dylanmullen.weightchallenge.util.mysql.util.SQLTicket;

@Data
public class UserInformation
{

	private int age;

	private double weight;
	private double height;
	private double muscleMass;
	private double fatMass;
	private double waterMass;

	public UserInformation(long discordID)
	{
		loadInformation(discordID);
	}

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
							if (!result.next())
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

}
