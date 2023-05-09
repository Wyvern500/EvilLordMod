package com.jg.evilord.proxy;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.NetworkEvent.Context;

public interface IProxy {
	public void registerModEventListeners(IEventBus bus);

	public void registerForgeEventListeners(IEventBus bus);

	public Player getPlayerFromContext(Context context);
	
	public Level getClientWorld();
}
