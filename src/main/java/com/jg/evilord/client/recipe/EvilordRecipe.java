package com.jg.evilord.client.recipe;

import java.util.ArrayList;
import java.util.List;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EvilordRecipe {

	private ItemStack result;
	private List<EvilordIngredient> ingredients;
	
	public EvilordRecipe(ItemStack result, List<EvilordIngredient> ingredients) {
		this.result = result;
		this.ingredients = new ArrayList<>();
		for(int i = 0; i < ingredients.size(); i++) {
			EvilordIngredient item = ingredients.get(i);
			if(!this.ingredients.contains(item)) {
				this.ingredients.add(item);
			} else {
				EvilordIngredient old = this.ingredients.get(this.ingredients.indexOf(item));
				old.setCount(old.getCount() + item.getCount());
			}
		}
		LogUtils.getLogger().info("Ingredients size: " + this.ingredients.size());
	}
	
	public EvilordRecipe(ItemStack result) {
		this.result = result;
		this.ingredients = new ArrayList<>();
	}
	
	public EvilordRecipe add(Item item, int count) {
		EvilordIngredient ing = new EvilordIngredient(item, count);
		if(!this.ingredients.contains(ing)) {
			this.ingredients.add(ing);
		} else {
			EvilordIngredient old = this.ingredients.get(this.ingredients.indexOf(ing));
			old.setCount(old.getCount() + ing.getCount());
		}
		return this;
	}
	
	public String getKey() {
		return result.getItem().getRegistryName().toString();
	}
	
	public ItemStack getResultItem() {
		return result;
	}
	
	public List<EvilordIngredient> getIngredients(){
		return ingredients;
	}
	
}
