package in.ajay.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.ajay.bindings.AppForm;
import in.ajay.service.ArService;

@RestController
public class AppRegRestController {

	@Autowired
	private ArService arService;
	
	@PostMapping("/app")
	public ResponseEntity<String> createApp(@RequestBody AppForm appForm){
		String status = arService.createApplication(appForm);
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}
	
	@GetMapping("/apps/{userId}")
	public List<AppForm> getApps(Integer userId){
		return arService.fetchApps(userId);
	}
}
