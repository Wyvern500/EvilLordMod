package com.jg.evilord.entities.block;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface ICrafterBlockEntity {

	public boolean canCraft(Player player);
	
	public default void onCraft(ServerPlayer player) {
		onCraft(player, 0);
	}
	
	public void onCraft(ServerPlayer player, int souls);
	
}
