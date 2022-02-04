package me.dylanmullen.weightchallenge.webapp.controllers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.dylanmullen.weightchallenge.core.App;

@RestController
@RequestMapping("/authentication")
public class LoginController
{

	@SuppressWarnings("unchecked")
	@PostMapping("/login")
	public ResponseEntity<JSONObject> login(@RequestBody String requestBody)
	{
		try
		{
			JSONObject json = (JSONObject) new JSONParser().parse(requestBody);
			String accessToken = (String) json.get("accessToken");
			long discordID = App.getInstance().getAuthManager().authenticateWithDiscord(accessToken);
			
			if(discordID == -1)
				return new ResponseEntity<>(new JSONObject(), HttpStatus.BAD_REQUEST);
			
			json.clear();
			json.put("discordID", discordID);
			json.put("authentication", App.getInstance().getAuthManager().getAuthCode(discordID));
			return new ResponseEntity<>(json, HttpStatus.OK);
		} catch (ParseException e)
		{
			return new ResponseEntity<>(new JSONObject(), HttpStatus.BAD_REQUEST);
		}
	}

}
