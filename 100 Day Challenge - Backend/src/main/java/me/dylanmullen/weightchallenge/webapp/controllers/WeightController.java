package me.dylanmullen.weightchallenge.webapp.controllers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.dylanmullen.weightchallenge.core.App;
import me.dylanmullen.weightchallenge.modules.users.User;
import me.dylanmullen.weightchallenge.modules.users.UserInformation.WeightType;

@RestController
@RequestMapping("/weight")
public class WeightController
{

	@PostMapping("")
	public ResponseEntity<JSONObject> updateType(@RequestHeader("Authorization") String authCode,
			@RequestBody String data)
	{
		JSONObject json = toJSON(authCode);

		ResponseEntity<JSONObject> response = validate(json, authCode);
		if (response != null)
			return response;

		User user = App.getInstance().getUserManager().getUser((long) json.get("discordID"));

		WeightType type = WeightType.getType((int) json.get("type"));
		double value = (double) json.get("value");
		return user.getInformation().updateWeights(type, value) ? new ResponseEntity<>(user.toJSON(), HttpStatus.OK)
				: new ResponseEntity<>(new JSONObject(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/set")
	public ResponseEntity<JSONObject> update(@RequestHeader("Authorization") String authCode, @RequestBody String data)
	{
		JSONObject json = toJSON(authCode);

		ResponseEntity<JSONObject> response = validate(json, authCode);
		if (response != null)
			return response;

		User user = App.getInstance().getUserManager().getUser((long) json.get("discordID"));
		double[] values = new double[] { (double) json.get("weight"), (double) json.get("fat_mass"),
				(double) json.get("muscle_mass"), (double) json.get("water_mass") };

		return user.getInformation().setWeights(values) ? new ResponseEntity<>(user.toJSON(), HttpStatus.OK)
				: new ResponseEntity<>(new JSONObject(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private JSONObject toJSON(String data)
	{
		try
		{
			return (JSONObject) new JSONParser().parse(data);
		} catch (ParseException e)
		{
			return null;
		}
	}

	private ResponseEntity<JSONObject> validate(JSONObject json, String authCode)
	{
		if (json == null)
			return new ResponseEntity<>(new JSONObject(), HttpStatus.BAD_REQUEST);
		if (json.get("discordID") == null)
			return new ResponseEntity<>(new JSONObject(), HttpStatus.BAD_REQUEST);
		if (App.getInstance().getUserManager().getUser((long) json.get("discordID")) == null)
			return new ResponseEntity<>(new JSONObject(), HttpStatus.BAD_REQUEST);
		if (!App.getInstance().getAuthManager().authorised((long) json.get("discordID"), authCode))
			return new ResponseEntity<>(new JSONObject(), HttpStatus.UNAUTHORIZED);
		return null;
	}
}
