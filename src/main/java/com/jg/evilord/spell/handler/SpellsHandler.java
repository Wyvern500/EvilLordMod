package com.jg.evilord.spell.handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jg.evilord.spell.Spell;

import net.minecraft.resources.ResourceLocation;

public enum SpellsHandler {

	INSTANCE;
	
	private Map<String, Spell> spells;
	
	private SpellsHandler() {
		this.spells = new HashMap<>();
	}
	
	public Spell getSpell(ResourceLocation loc) {
		return spells.get(loc.toString());
	}
	
	public ResourceLocation getRersourceLocationForSpell(Spell spell) {
		for(Entry<String, Spell> e : spells.entrySet()) {
			if(e.getValue() == spell) {
				return new ResourceLocation(e.getKey());
			}
		}
		return null;
	}
	
	public void registerSpell(ResourceLocation loc, Spell spell) {
		spells.putIfAbsent(loc.toString(), spell);
	}
	
	public boolean hasSpell(ResourceLocation res) {
		return spells.containsKey(res.toString());
	}
	
	public boolean hasSpell(Spell spell) {
		return spells.containsValue(spell);
	}
	
	public Collection<Spell> getSpells(){
		return spells.values();
	}
	
}
