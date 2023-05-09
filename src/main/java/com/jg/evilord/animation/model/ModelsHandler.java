package com.jg.evilord.animation.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mojang.logging.LogUtils;

public class ModelsHandler {

	private static final Map<String, JgModel> jgModels = new HashMap<>();
	private static boolean init;
	
	public static void register(String gun, JgModel gunModel) {
		jgModels.put(gun, gunModel);
		LogUtils.getLogger().info("registering gun: " + gun);
	}

	public static JgModel get(String gun) {
		if(jgModels.containsKey(gun)) {
			return jgModels.get(gun);
		}else {
			for(Entry<String, JgModel> entry : jgModels.entrySet()) {
				LogUtils.getLogger().error(entry.getKey());
			}
			//LogUtils.getLogger().error("No gunmodel linked with item: " + gun.getRegistryName().toString());
			return null;
		}
	}
	
	public static Map<String, JgModel> getMap(){
		return jgModels;
	}
	
	public static boolean getInit() {
		return init;
	}
	
	public static void setInit(boolean init) {
		ModelsHandler.init = init;
	}
	
}
