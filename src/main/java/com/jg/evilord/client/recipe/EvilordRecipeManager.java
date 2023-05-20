package com.jg.evilord.client.recipe;

import java.util.HashMap;
import java.util.Map;

import com.jg.evilord.Evilord;

public enum EvilordRecipeManager {
	
	INSTANCE;
	
	public static final String ARTIFACTS = Evilord.MODID + ":Artifacts";
	public static final String BOOKSTUFF = Evilord.MODID + ":BookStuff";
	private Map<String, RecipeHandler> recipeHandlers;
	
	private EvilordRecipeManager() {
		recipeHandlers = new HashMap<>();
	}
	
	public Map<String, RecipeHandler> getRecipeHandlers(){
		return recipeHandlers;
	}
	
	public RecipeHandler getRecipeHandler(String key) {
		recipeHandlers.putIfAbsent(key, new RecipeHandler(key));
		return recipeHandlers.get(key);
	}
	
}
