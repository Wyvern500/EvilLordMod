package com.jg.evilord.client.recipe;

import java.util.function.Supplier;

import net.minecraft.world.item.Item;

public class CraftingTab {

	private String name;
	private Supplier<Item> icon;
	
	public CraftingTab(String name, Supplier<Item> icon) {
		this.name = name;
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public Supplier<Item> getIcon() {
		return icon;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CraftingTab) {
			return ((CraftingTab) obj).getName().equals(name);
		}
		return super.equals(obj);
	}
	
}
