package com.jg.evilord.client.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jg.evilord.Evilord;
import com.jg.evilord.client.screens.AnimationScreen.Pair;
import com.jg.evilord.network.server.CraftArtifactMessage;
import com.jg.evilord.network.server.CraftArtifactWithSoulsMessage;
import com.jg.evilord.registries.ItemRegistries;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RecipeHandler {

	private Map<String, EvilordRecipe> recipes; 
	private CraftingTab tab;
	
	public RecipeHandler(CraftingTab tab) {
		this.recipes = new HashMap<>();
		this.tab = tab;
	}
	
	public RecipeHandler(String key, Item item) {
		this.recipes = new HashMap<>();
		this.tab = new CraftingTab(key, () -> item);
	}
	
	public EvilordRecipe getRecipe(Item item) {
		return recipes.get(item.getRegistryName().toString());
	}
	
	public boolean addRecipe(EvilordRecipe recipe) {
		return recipes.putIfAbsent(recipe.getKey(), 
				recipe) == null;
	}
	
	public RecipeResultData checkRecipe(Item item, Player player) {
		return checkRecipe(item.getRegistryName().toString(), player);
	}
	
	public RecipeResultData checkRecipe(String recipeKey, Player player) {
		EvilordRecipe recipe = recipes.get(recipeKey);
		if(recipe == null) {
			return null;
		}
		Inventory inv = player.getInventory();
		List<Pair<Integer, Integer>> toRemove = new ArrayList<>();
		List<Integer> covered = new ArrayList<>();
		List<Integer> remaining = new ArrayList<>();
		int souls = 0;
		for(EvilordIngredient ing : recipe.getIngredients()) {
			if(ing.getItem() != ItemRegistries.soul.get()) {
				remaining.add(ing.getCount());
			} else {
				remaining.add(0);
				souls = ing.getCount();
			}
		}
		List<EvilordIngredient> ingredients = recipe.getIngredients();
		boolean finished = false;
		
		for(int i = 0; i < inv.items.size(); i++) {
			ItemStack item = inv.getItem(i);
			for(int j = 0; j < ingredients.size(); j++) {
				EvilordIngredient other = ingredients.get(j);
				// Check if there is no remaining ingredients to cover
				if(remaining.stream().allMatch((n) -> n <= 0)) {
					finished = true;
					break;
				}
				// Check if the current ingredient is covered
				if(!covered.contains(j)) {
					if(item.getItem().getRegistryName().toString().equals(other
							.getItem().getRegistryName().toString())) {
						if(remaining.get(j) > 0) {
							int toAdd = item.getCount() > remaining.get(j) ? 
									remaining.get(j) : item.getCount();
							toRemove.add(new Pair<>(i, toAdd));
							remaining.set(j, remaining.get(j) - toAdd);
						} else {
							covered.add(j);
						}
					}
				}
			}
			if(finished) {
				break;
			}
		}
		return new RecipeResultData(toRemove, recipe.getResultItem(), souls, finished);
	}
	
	public void processRecipe(Item key, Player player) {
		processRecipe(key.getRegistryName().toString(), player);
	}
	
	public void processRecipe(String key, Player player) {
		RecipeResultData data = checkRecipe(key, player);
		if(data.canCraft) {
			craft(data);
		}
	}
	
	public void processSoulRecipe(Item key, Player player, int souls, int containerId, 
			BlockPos pos) {
		RecipeResultData data = checkRecipe(key, player);
		LogUtils.getLogger().info("Required souls: " + souls + " canCraft: " + 
				data.canCraft);
		if(data.canCraft && souls >= data.souls) {
			LogUtils.getLogger().info("Crafting");
			craftWithSouls(data, pos, containerId);
		}
	}
	
	private void craft(RecipeResultData data) {
		int[] processedData = new int[data.toRemove.size() * 2];
		int j = 0;
		for(int i = 0; i < data.toRemove.size(); i++) {
			processedData[j] = data.toRemove.get(i).getLeft();
			processedData[j + 1] = data.toRemove.get(i).getRight();
			j += 2;
		}
		Evilord.channel.sendToServer(new CraftArtifactMessage(data.stack, processedData));
	}
	
	private void craftWithSouls(RecipeResultData data, BlockPos pos, int containerId) {
		int[] processedData = new int[data.toRemove.size() * 2];
		int j = 0;
		for(int i = 0; i < data.toRemove.size(); i++) {
			processedData[j] = data.toRemove.get(i).getLeft();
			processedData[j + 1] = data.toRemove.get(i).getRight();
			j += 2;
		}
		Evilord.channel.sendToServer(new CraftArtifactWithSoulsMessage(data.stack, 
				processedData, data.souls, containerId, pos));
	}
	
	public Map<String, EvilordRecipe> getRecipes(){
		return recipes;
	}
	
	public CraftingTab getTab() {
		return tab;
	}
	
	public static class RecipeResultData {
		
		private List<Pair<Integer, Integer>> toRemove;
		private ItemStack stack;
		private int souls;
		private boolean canCraft;
		
		public RecipeResultData(List<Pair<Integer, Integer>> toRemove, ItemStack stack, 
				int souls, boolean canCraft) {
			this.toRemove = toRemove;
			this.stack = stack;
			this.souls = souls;
			this.canCraft = canCraft;
		}

		public List<Pair<Integer, Integer>> getToRemove() {
			return toRemove;
		}

		public ItemStack getResult() {
			return stack;
		}
		
		public int getSouls() {
			return souls;
		}
		
		public boolean canCraft() {
			return canCraft;
		}
		
	}
	
}
