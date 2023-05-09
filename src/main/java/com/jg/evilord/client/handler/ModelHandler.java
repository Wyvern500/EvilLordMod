package com.jg.evilord.client.handler;

import java.util.HashMap;
import java.util.Map;

import com.jg.evilord.client.model.item.DynamicGunModel;

public enum ModelHandler {
INSTANCE;
	
	protected Map<String, DynamicGunModel> cache;
	protected Map<String, DynamicGunModel> models;
	
	ModelHandler() {
		models = new HashMap<>();
		cache = new HashMap<>();
	}
	
	public void put(String id, DynamicGunModel model) {
		models.put(id, model);
	}
	
	public DynamicGunModel getModel(String id) {
		return models.get(id);
	}
	
	public Map<String, DynamicGunModel> getModels(){
		return models;
	}
	
	public Map<String, DynamicGunModel> getCache(){
		return cache;
	}
}
