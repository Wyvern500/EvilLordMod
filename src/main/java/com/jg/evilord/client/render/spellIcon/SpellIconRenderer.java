package com.jg.evilord.client.render.spellIcon;

import net.minecraft.resources.ResourceLocation;

public class SpellIconRenderer {

	public ResourceLocation path;
	public int sourceX;
	public int sourceY;
	public int size;
	
	public SpellIconRenderer(String path, int sourceX, int sourceY) {
		this.path = new ResourceLocation(path);
		this.sourceX = sourceX;
		this.sourceY = sourceY;
		this.size = 16;
	}
	
	public static SpellIconRenderer create(String path, int sourceX, int sourceY) {
		return new SpellIconRenderer(path, sourceX, sourceY);
	}
	
}
