package com.jg.evilord.client.recipe;

import net.minecraft.world.item.Item;

public class EvilordIngredient {

	private Item item;
	private int count;
	
	public EvilordIngredient(Item item, int count) {
		this.item = item;
		this.count = count;
	}

	public Item getItem() {
		return item;
	}

	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof EvilordIngredient) {
			return ((EvilordIngredient) obj).getItem().equals(item);
		}
		return super.equals(obj);
	}
	
}
