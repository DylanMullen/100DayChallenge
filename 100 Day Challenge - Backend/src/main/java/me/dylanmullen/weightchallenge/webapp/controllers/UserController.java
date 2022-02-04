package me.dylanmullen.weightchallenge.webapp.controllers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.dylanmullen.weightchallenge.core.App;
import me.dylanmullen.weightchallenge.modules.users.User;

@RestController
@RequestMapping("/users")
public class UserController
{

	@GetMapping
	public ResponseEntity<JSONObject> getUsers(@RequestHeader("Authorization") String authCode,
			@RequestParam long discordID)
	{
		if (!App.getInstance().getAuthManager().authorised(discordID, authCode))
			return new ResponseEntity<>(new JSONObject(), HttpStatus.UNAUTHORIZED);

		return new ResponseEntity<>(App.getInstance().getUserManager().getUsersJSON(), HttpStatus.OK);
	}

	@GetMapping("/@me")
	public ResponseEntity<JSONObject> getUser(@RequestHeader("Authorization") String authCode)
	{
		long discordID = App.getInstance().getAuthManager().getIDFromAuthCode(authCode);

		User user = App.getInstance().getUserManager().getUser(discordID);
		if (user == null)
			return new ResponseEntity<>(new JSONObject(), HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(user.toJSON(), HttpStatus.OK);
	}

	@PostMapping("/@me/{type}")
	public ResponseEntity<JSONObject> setAge(@RequestHeader("Authorization") String authCode,
			@PathVariable("type") String type, @RequestBody String data)
	{
		long discordID = App.getInstance().getAuthManager().getIDFromAuthCode(authCode);
		JSONObject json = toJSON(data);

		if (json == null)
			return new ResponseEntity<>(new JSONObject(), HttpStatus.BAD_REQUEST);

		User user = App.getInstance().getUserManager().getUser(discordID);
		if (user == null)
			return new ResponseEntity<>(new JSONObject(), HttpStatus.BAD_REQUEST);

		switch (type)
		{
			case "age":
				user.getInformation().updateAge((int) json.get("value"));
				return new ResponseEntity<>(user.toJSON(), HttpStatus.OK);
			case "height":
				user.getInformation().updateHeight((int) json.get("value"));
				return new ResponseEntity<>(user.toJSON(), HttpStatus.OK);
			default:
				return new ResponseEntity<>(new JSONObject(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/user/{discordID}")
	public ResponseEntity<JSONObject> getUser(@RequestHeader("Authorization") String authCode,
			@PathVariable("discordID") long id, @RequestParam long discordID)
	{
		if (!App.getInstance().getAuthManager().authorised(discordID, authCode))
			return new ResponseEntity<>(new JSONObject(), HttpStatus.UNAUTHORIZED);

		User user = App.getInstance().getUserManager().getUser(id);
		if (user == null)
			return new ResponseEntity<>(new JSONObject(), HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(user.toJSON(), HttpStatus.OK);
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
}
