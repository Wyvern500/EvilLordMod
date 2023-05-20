package com.jg.evilord.network.server;

import java.util.function.Supplier;

import com.jg.evilord.container.AbstractSoulEnergyCapableContainer;
import com.jg.evilord.entities.block.BinderBlockEntity;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;

public class CraftArtifactWithSoulsMessage {

	ItemStack result;
	int[] data;
	int souls;
	int containerId;
	BlockPos pos;
	
	public CraftArtifactWithSoulsMessage(ItemStack result, int[] data, int souls, 
			int containerId, BlockPos pos) {
		this.result = result;
		this.data = data;
		this.souls = souls;
		this.containerId = containerId;
		this.pos = pos;
	}
	
	public static void encode(CraftArtifactWithSoulsMessage msg, FriendlyByteBuf buf) {
		buf.writeItemStack(msg.result, true);
		buf.writeVarIntArray(msg.data);
		buf.writeInt(msg.souls);
		buf.writeInt(msg.containerId);
		buf.writeBlockPos(msg.pos);
	}

	public static CraftArtifactWithSoulsMessage decode(FriendlyByteBuf buf) {
		return new CraftArtifactWithSoulsMessage(buf.readItem(), buf.readVarIntArray(), 
				buf.readInt(), buf.readInt(), buf.readBlockPos());
	}

	public static void handle(CraftArtifactWithSoulsMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();

		context.enqueueWork(() -> {
			LogUtils.getLogger().info("Crafting with souls");
			ServerPlayer player = context.getSender();
			for(int i = 0; i < msg.data.length; i += 2) {
				player.getInventory().removeItem(msg.data[i], msg.data[i + 1]);
			}
			player.getInventory().add(msg.result);
			BinderBlockEntity be = (BinderBlockEntity) player.level.getBlockEntity(msg.pos);
			be.getSoulEnergyStorage().extractEnergy(msg.souls, false);
			if(player.containerMenu != null) {
				player.containerMenu.broadcastChanges();
			}
		});
		context.setPacketHandled(true);
	}
	
}
