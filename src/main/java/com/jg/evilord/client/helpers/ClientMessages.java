package com.jg.evilord.client.helpers;

import java.util.logging.Logger;

import com.jg.evilord.Evilord;
import com.jg.evilord.network.server.DeathEntityResolutionMessage;
import com.jg.evilord.utils.NBTUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.fml.DistExecutor;

public class ClientMessages {

	public static DistExecutor.SafeRunnable deathEntity(int killer, int killed) {
		return new DistExecutor.SafeRunnable() {
			@Override
			public void run() {
				Player player = Minecraft.getInstance().player;
				Entity killerEntity = player.level.getEntity(killer);
				if(killerEntity instanceof Player) {
					ItemStack stack = player.getMainHandItem();
					if(NBTUtils.hasContainer(stack) || player.getMainHandItem().getItem() instanceof SwordItem) {
						LivingEntity killedEntity = (LivingEntity)player.level.getEntity(killed);
						Logger.getGlobal().info("Entity name: " + killedEntity.getDisplayName().getString());
						Evilord.channel.sendToServer(new DeathEntityResolutionMessage());
					}
				}
			}
		};
	}
	
}
