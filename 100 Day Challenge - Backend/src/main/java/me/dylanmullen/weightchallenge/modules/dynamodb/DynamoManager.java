package me.dylanmullen.weightchallenge.modules.dynamodb;

import java.util.Arrays;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

import lombok.Data;

@Data
public class DynamoManager
{

	private DynamoDB database;

	public DynamoManager()
	{
		init();
	}

	private void init()
	{
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "eu-west-2")).build();

		this.database = new DynamoDB(client);
		createTables();
	}

	private void createTables()
	{
		try
		{
			Table userTable = database.createTable("users", Arrays.asList(new KeySchemaElement("discordID", KeyType.HASH)),
					Arrays.asList(new AttributeDefinition("year", ScalarAttributeType.S)), new ProvisionedThroughput(10L, 10L));
			userTable.waitForActive();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	
}
