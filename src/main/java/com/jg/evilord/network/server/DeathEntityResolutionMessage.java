package com.jg.evilord.network.server;

import java.util.function.Supplier;

import com.jg.evilord.utils.NBTUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;

public class DeathEntityResolutionMessage {

	public DeathEntityResolutionMessage() {
		
	}
	
	public static void encode(DeathEntityResolutionMessage msg, FriendlyByteBuf buf) {

	}

	public static DeathEntityResolutionMessage decode(FriendlyByteBuf buf) {
		return new DeathEntityResolutionMessage();
	}

	public static void handle(DeathEntityResolutionMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();

		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			ItemStack stack = player.getMainHandItem();
			NBTUtils.setSouls(stack, NBTUtils.getSouls(stack) + 1);
		});
		context.setPacketHandled(true);
	}
	
}
