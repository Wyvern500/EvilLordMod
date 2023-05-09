package com.jg.evilord.spell;

import com.jg.evilord.client.render.spellIcon.SpellIconRenderer;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Spell {

	protected SpellIconRenderer icon;
	protected String name;
	protected FireRate fireRate;
	
	protected RunSpell run;
	protected CanLaunch can;
	
	public Spell(String name, FireRate fireRate, SpellIconRenderer icon, RunSpell run, 
			CanLaunch can) {
		this.name = name;
		this.fireRate = fireRate;
		this.icon = icon;
		this.run = run;
		this.can = can;
	}
	
	public Spell(String name, FireRate fireRate, SpellIconRenderer icon) {
		this.name = name;
		this.fireRate = fireRate;
		this.icon = icon;
		this.run = (s, p) -> {};
		this.can = (s, p) -> { return true; };
	}
	
	public boolean canLaunch(ItemStack stack, Player player) {
		return can.canLaunch(stack, player);
	}
	
	public void onLaunch(ItemStack stack, Player player) {
		run.run(stack, player);
	}
	
	public SpellIconRenderer getIcon() {
		return icon;
	}

	public String getName() {
		return name;
	}

	public FireRate getFireRate() {
		return fireRate;
	}

	public static interface RunSpell {
		public void run(ItemStack stack, Player player);
	}
	
	public static interface CanLaunch {
		public boolean canLaunch(ItemStack stack, Player player);
	}
	
	public static enum FireRate {
		AUTO, SEMI;
	}
	
}
