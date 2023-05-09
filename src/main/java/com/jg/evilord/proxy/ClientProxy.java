package com.jg.evilord.proxy;

import com.jg.evilord.client.handler.ClientEventHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.NetworkEvent.Context;

public class ClientProxy implements IProxy {

	@Override
	public void registerModEventListeners(IEventBus bus) {
		ClientEventHandler.registerModEventListeners(bus);
	}

	@Override
	public void registerForgeEventListeners(IEventBus bus) {
		ClientEventHandler.registerForgeEventListeners(bus);
	}

	@Override
	public Player getPlayerFromContext(Context context) {
		return context.getSender();
	}

	@Override
	public Level getClientWorld() {
		return Minecraft.getInstance().level;
	}

}
