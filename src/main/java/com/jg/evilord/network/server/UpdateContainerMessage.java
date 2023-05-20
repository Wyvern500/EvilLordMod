package com.jg.evilord.network.server;

import java.util.function.Supplier;

import com.jg.evilord.utils.NBTUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;

public class UpdateContainerMessage {

	int id;
	
	public UpdateContainerMessage(int id) {
		this.id = id;
	}
	
	public static void encode(UpdateContainerMessage msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.id);
	}

	public static UpdateContainerMessage decode(FriendlyByteBuf buf) {
		return new UpdateContainerMessage(buf.readInt());
	}

	public static void handle(UpdateContainerMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();

		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			if(player.containerMenu.containerId == msg.id) {
				player.containerMenu.broadcastChanges();
			}
		});
		context.setPacketHandled(true);
	}
	
}
