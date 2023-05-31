package com.jg.evilord.client.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.jg.evilord.Evilord;
import com.jg.evilord.client.handler.ClientHandler;
import com.jg.evilord.client.handler.ClientsHandler;
import com.jg.evilord.network.server.DeathEntityResolutionMessage;
import com.jg.evilord.utils.NBTUtils;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
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
	
	public static void showInfoBlocks(CompoundTag data) {
		if(data != null) {
			byte inputs = data.getByte("inputs");
			byte outputs = data.getByte("outputs");
			List<int[]> in = new ArrayList<>();
			List<int[]> out = new ArrayList<>();
			for(int i = 0; i < inputs; i++) {
				int[] ia = data.getIntArray("input" + i);
				if(ia.length > 0) {
					in.add(data.getIntArray("input" + i));
				}
			}
			for(int i = 0; i < outputs; i++) {
				int[] oa = data.getIntArray("output" + i);
				if(oa.length > 0) {
					out.add(data.getIntArray("output" + i));
				}
			}
			ClientHandler client = ClientsHandler.getClient(Minecraft.getInstance().getUser());
			client.getConnectionManipulator().fillVisualInfoBlocks(in, out);
			client.getConnectionManipulator().fillInfo(inputs, outputs, 
					data.getByte("remaining_input_slots"), 
					data.getByte("remaining_output_slots"));
			//LogUtils.getLogger().info("Hehehehe " + data.getAsString());
		} else {
			LogUtils.getLogger().info("Nullll");
		}
	}
	
}
