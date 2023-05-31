package com.jg.evilord.network.client;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import com.jg.evilord.Evilord;
import com.jg.evilord.client.helpers.ClientMessages;
import com.jg.evilord.entities.block.AbstractBinderBlockEntity;
import com.jg.evilord.utils.MessageUtils;
import com.jg.evilord.utils.NBTUtils;
import com.jg.evilord.utils.Pos;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent.Context;

public class ShowInfoBlocksMessage {

	int[] pos;
	CompoundTag data;
	
	public ShowInfoBlocksMessage(BlockPos pos, CompoundTag data) {
		this(new int[] { pos.getX(), pos.getY(), pos.getZ() }, data);
	}
	
	public ShowInfoBlocksMessage(int[] pos, CompoundTag data) {
		this.pos = pos;
		this.data = data;
	}

	public static void encode(ShowInfoBlocksMessage msg, FriendlyByteBuf buf) {
		buf.writeVarIntArray(msg.pos);
		buf.writeNbt(msg.data);
	}

	public static ShowInfoBlocksMessage decode(FriendlyByteBuf buf) {
		return new ShowInfoBlocksMessage(buf.readVarIntArray(), buf.readNbt());
	}

	public static void handle(ShowInfoBlocksMessage msg, Supplier<Context> ctx) {
		/*Context context = ctx.get();
		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			MessageUtils.sendMessageFromServerToAll(new ShowInfoBlocksMessage(pos));
		});
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientMessages
				.showInfoBlocks(1, 1));
		LogUtils.getLogger().info("Holaaaaaaaaaaaaaaaaaaaaaaaaa");
		context.setPacketHandled(true);*/
		Context context = ctx.get();

		if(context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				ServerLevel world = Objects.requireNonNull(context.getSender()).getLevel();
				LogUtils.getLogger().info("Server");
				BlockPos pos = new BlockPos(msg.pos[0], msg.pos[1], msg.pos[2]);
				if(world.isLoaded(pos)) {
					BlockEntity tile = world.getBlockEntity(pos);
					if(tile instanceof AbstractBinderBlockEntity<?>) {
						AbstractBinderBlockEntity<?> bbe = 
						((AbstractBinderBlockEntity<?>)tile);
						int inputs = bbe.getLinks().getInputs().size();
						int outputs = bbe.getLinks().getOutputs().size();;
						int rInputs = inputs;
						int rOutputs = outputs;
						for(Pos p : bbe.getLinks().getInputs()) {
							if(!p.isEmpty()) {
								rInputs--;
							}
						}
						for(Pos p : bbe.getLinks().getOutputs()) {
							if(!p.isEmpty()) {
								rOutputs--;
							}
						}
						LogUtils.getLogger().info("Input size: " + inputs 
								+ " Output size: " + outputs + " name: " + 
								bbe.getBlockState().getBlock().getRegistryName()
								.toString());
						msg.data.putByte("remaining_input_slots", (byte)rInputs);
						msg.data.putByte("remaining_output_slots", (byte)rOutputs);
						MessageUtils.sendMessageFromServerToClientPlayer(
								context.getSender(), 
								new ShowInfoBlocksMessage(pos, NBTUtils
										.writeInfoDataToNbt(msg.data, bbe
												.getLinks().getInputs(), 
												bbe.getLinks().getOutputs())));
					}
			}
			});
		} else {
			context.enqueueWork(() -> {
				Level world = Evilord.proxy.getClientWorld();
				//LogUtils.getLogger().info("Client");
				if(world != null) { // This can happen if the task is scheduled right before leaving the world
					ClientMessages.showInfoBlocks(msg.data);
					//LogUtils.getLogger().info("World not null");
				}
			});
		}
		context.setPacketHandled(true);
	}
	
}
