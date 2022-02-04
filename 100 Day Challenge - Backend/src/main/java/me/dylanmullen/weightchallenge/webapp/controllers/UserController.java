package me.dylanmullen.weightchallenge.webapp.controllers;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public ResponseEntity<JSONObject> getUsers(@RequestHeader("Authorization") String authCode, @RequestParam long discordID)
	{
		if(!isAuthorized(discordID, authCode))
			return new ResponseEntity<>(new JSONObject(), HttpStatus.UNAUTHORIZED);

		return new ResponseEntity<>(App.getInstance().getUserManager().getUsersJSON(), HttpStatus.OK);
	}

	@GetMapping("/{discordID}")
	public ResponseEntity<JSONObject> getUser(@RequestHeader("Authorization") String authCode, @PathVariable long id,
			@RequestParam long discordID)
	{
		if(!isAuthorized(discordID, authCode))
			return new ResponseEntity<>(new JSONObject(), HttpStatus.UNAUTHORIZED);

		User user = App.getInstance().getUserManager().getUser(id);
		if (user == null)
			return new ResponseEntity<>(new JSONObject(), HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(user.toJSON(), HttpStatus.OK);
	}

	private boolean isAuthorized(long discordID, String authCode)
	{
		if (authCode == null)
			return false;
		
		return App.getInstance().getAuthManager().authorised(discordID, authCode);
	}
	
}
