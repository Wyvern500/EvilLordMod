package com.jg.evilord.client.events;

import com.jg.evilord.client.recipe.CraftingTab;
import com.jg.evilord.client.recipe.EvilordRecipe;
import com.jg.evilord.client.recipe.EvilordRecipeManager;

import net.minecraftforge.eventbus.api.Event;

public class RegisterRecipeEvent extends Event {

	public RegisterRecipeEvent() {
		
	}
	
	public void register(CraftingTab tab, EvilordRecipe recipe) {
		EvilordRecipeManager.INSTANCE.getRecipeHandler(tab).addRecipe(recipe);
	}
	
}
