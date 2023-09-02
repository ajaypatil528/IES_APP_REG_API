package in.ajay.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import in.ajay.bindings.AppForm;
import in.ajay.constants.AppConstants;
import in.ajay.entity.AppEntity;
import in.ajay.entity.UserEntity;
import in.ajay.exception.SsaWebException;
import in.ajay.repository.AppRepo;
import in.ajay.repository.UserRepo;

@Component
public class ArServiceImpl implements ArService{
	
	/*
	 * @Autowired(required = true) private UserRepo userRepo;
	 */
	@Autowired
	private AppRepo appRepo;

	@Autowired
	private UserRepo userRepo;
	
	private static final String SSA_WEB_API_URL = "http://ssa.web.app/{ssn}";

	@Override
	public String createApplication(AppForm appForm) {
		
		
		// Since we are making web service call from this API to another web service
		// there is chance of the web service down(if web service is down) that is why we are  
		// writing this login in try catch block.
		try {
			WebClient webClient = WebClient.create();
			String stateName = webClient.get()
										.uri(SSA_WEB_API_URL, appForm.getSsn())
										.retrieve()
										.bodyToMono(String.class)
										.block();
			
			if(AppConstants.RI.equals(stateName)) {
				//valid citizen for the application
				
				UserEntity userEntity = userRepo.findById(appForm.getUserId()).get();
				AppEntity appEntity = new AppEntity();
				BeanUtils.copyProperties(appForm, appEntity);
				
				appEntity.setUser(userEntity);
				
				appEntity  = appRepo.save(appEntity);
				return "Applicatoin created with Case Num: "+appEntity.getCaseNum();
			}
			
			
		}catch(Exception e) {
			throw new SsaWebException(e.getMessage());
		}
		return AppConstants.INVALID_SSN;
	}

	@Override
	public List<AppForm> fetchApps(Integer userId) {
		
		UserEntity userEntity = userRepo.findById(userId).get();
		Integer roleId = userEntity.getRoleId();
		
		List<AppEntity> appEntities = null;
		
		if(roleId == 1) {
			appEntities = appRepo.fetchUserApps();
		}else {
			appEntities = appRepo.fetcCaseWorkerApps(userId);
		}
		
		List<AppForm> apps = new ArrayList<>();
		
		for(AppEntity entity : appEntities) {
			AppForm appForm = new AppForm();
			BeanUtils.copyProperties(entity, apps);
			apps.add(appForm);
		}
		return apps;
	}

}
