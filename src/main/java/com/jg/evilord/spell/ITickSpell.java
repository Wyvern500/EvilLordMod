package com.jg.evilord.spell;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ITickSpell {

	public void tick(ItemStack stack, Player player);
	
}
