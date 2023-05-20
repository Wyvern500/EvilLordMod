package com.jg.evilord.network.server;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;

public class CraftArtifactMessage {

	ItemStack result;
	int[] data;
	
	public CraftArtifactMessage(ItemStack result, int[] data) {
		this.result = result;
		this.data = data;
	}
	
	public static void encode(CraftArtifactMessage msg, FriendlyByteBuf buf) {
		buf.writeItemStack(msg.result, true);
		buf.writeVarIntArray(msg.data);
	}

	public static CraftArtifactMessage decode(FriendlyByteBuf buf) {
		return new CraftArtifactMessage(buf.readItem(), buf.readVarIntArray());
	}

	public static void handle(CraftArtifactMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();

		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			for(int i = 0; i < msg.data.length; i += 2) {
				player.getInventory().removeItem(msg.data[i], msg.data[i + 1]);
			}
			player.getInventory().add(msg.result);
		});
		context.setPacketHandled(true);
	}
	
}
