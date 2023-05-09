package com.jg.evilord.proxy;

import java.util.logging.Logger;

import com.jg.evilord.utils.NBTUtils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.NetworkEvent.Context;

public class ServerProxy implements IProxy {

	@Override
	public void registerModEventListeners(IEventBus bus) {
		
	}

	@Override
	public void registerForgeEventListeners(IEventBus bus) {
		bus.addListener(ServerProxy::onKillEntity);
	}
	
	private static void onKillEntity(LivingDeathEvent e) {
		/*Entity killer = e.getSource().getDirectEntity();
		if(killer instanceof Player) {
			Evilord.channel.sendTo(new DeathEntityMessage(killer.), 
					((ServerPlayer) killer).connection.connection,
					NetworkDirection.PLAY_TO_CLIENT);
		}*/
		Entity killer = e.getSource().getDirectEntity();
		if(killer instanceof Player) {
			ServerPlayer player = (ServerPlayer)killer;
			ItemStack stack = player.getMainHandItem();
			if(NBTUtils.hasContainer(stack) || player.getMainHandItem().getItem() instanceof SwordItem) {
				LivingEntity killed = e.getEntityLiving();
				NBTUtils.setSouls(stack, 
						Math.min(NBTUtils.getMaxSoulsForTier(stack), 
								NBTUtils.getSouls(stack) + 1)); // Mas adelante planeo
				// implementarle un multiplicador, como una especie de encantamiento, este 
				// estara presente en el contenedor de almas
				Logger.getGlobal().info("Entity name: " + killed.getDisplayName().getString());
			}
		}
	}

	@Override
	public Player getPlayerFromContext(Context context) {
		return context.getSender();
	}

	@Override
	public Level getClientWorld() {
		// TODO Auto-generated method stub
		return null;
	}

}
