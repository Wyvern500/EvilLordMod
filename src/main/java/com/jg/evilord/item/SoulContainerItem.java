package com.jg.evilord.item;

import net.minecraft.world.item.Item;

public class SoulContainerItem extends Item {

	private ContainerTier tier;
	
	public SoulContainerItem(Item.Properties properties, ContainerTier tier) {
		super(properties);
		this.tier = tier;
	}
	
	public ContainerTier getTier() {
		return tier;
	}
	
	public static enum ContainerTier {
		ZERO(10), ONE(20), TWO(30), THREE(40), FOUR(50), FIVE(60);
		
		private int max;
		
		private ContainerTier(int max) {
			this.max = max;
		}
		
		public int getMaxSouls() {
			return max;
		}
		
	}
	
}
