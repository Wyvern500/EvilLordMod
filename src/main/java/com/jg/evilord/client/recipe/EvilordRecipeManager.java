package com.jg.evilord.client.recipe;

import java.util.HashMap;
import java.util.Map;

import com.jg.evilord.Evilord;
import com.jg.evilord.registries.ItemRegistries;

public enum EvilordRecipeManager {
	
	INSTANCE;
	
	public static final String ARTIFACTS = Evilord.MODID + ":Artifacts";
	public static final CraftingTab ARTIFACTSTAB = new CraftingTab(Evilord.MODID 
			+ ":Artifacts", () -> ItemRegistries.grimoireOfEvil.get());
	public static final String BOOKSTUFF = Evilord.MODID + ":BookStuff";
	public static final CraftingTab BOOKSTUFFTAB = new CraftingTab(Evilord.MODID 
			+ ":BookStuff", () -> ItemRegistries.commonMagicInk.get());
	private Map<CraftingTab, RecipeHandler> recipeHandlers;
	
	private EvilordRecipeManager() {
		recipeHandlers = new HashMap<>();
	}
	
	public Map<CraftingTab, RecipeHandler> getRecipeHandlers(){
		return recipeHandlers;
	}
	
	public RecipeHandler getRecipeHandler(CraftingTab key) {
		recipeHandlers.putIfAbsent(key, new RecipeHandler(key));
		return recipeHandlers.get(key);
	}
	
}
