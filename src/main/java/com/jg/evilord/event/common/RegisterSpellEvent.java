package com.jg.evilord.event.common;

import com.jg.evilord.spell.Spell;
import com.jg.evilord.spell.handler.SpellsHandler;
import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;

public class RegisterSpellEvent extends Event {

	private String modid;
	
	public RegisterSpellEvent() {
		
	}
	
	public void push(String modid) {
		LogUtils.getLogger().info("Registering spells for ModId: " + modid);
		this.modid = modid;
	}
	
	public void pop() {
		this.modid = "";
	}
	
	public void registerSpell(String path, Spell spell) {
		SpellsHandler.INSTANCE.registerSpell(new ResourceLocation(modid, path), spell);
	}
	
}
