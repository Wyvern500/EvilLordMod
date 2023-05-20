package com.jg.evilord.client.events;

import com.jg.evilord.client.recipe.EvilordRecipe;
import com.jg.evilord.client.recipe.EvilordRecipeManager;

import net.minecraftforge.eventbus.api.Event;

public class RegisterRecipeEvent extends Event {

	public RegisterRecipeEvent() {
		
	}
	
	public void register(String key, EvilordRecipe recipe) {
		EvilordRecipeManager.INSTANCE.getRecipeHandler(key).addRecipe(recipe);
	}
	
}
