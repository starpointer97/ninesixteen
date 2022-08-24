package com.nishit.ninesixteen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nishit.ninesixteen.service.AcceptService;

@RestController
public class AcceptController {

	@Autowired
	private AcceptService acceptService;

	@RequestMapping(method = RequestMethod.POST, value = "/v0/accept")
	public void acceptRequest(@RequestBody String messageBody) {
		acceptService.processMessage(messageBody);
	}
}
