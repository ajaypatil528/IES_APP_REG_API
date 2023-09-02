package in.ajay.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.ajay.bindings.AppForm;

@Service
public interface ArService {

	public String createApplication(AppForm appForm);
	
	public List<AppForm> fetchApps(Integer userId);
}
