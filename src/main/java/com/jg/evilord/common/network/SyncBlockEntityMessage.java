package com.jg.evilord.common.network;

import java.util.Objects;
import java.util.function.Supplier;

import com.jg.evilord.Evilord;
import com.jg.evilord.entities.block.BinderBlockEntity;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent.Context;

public class SyncBlockEntityMessage {
	
	private final int x;
	private final int y;
	private final int z;
	private final CompoundTag nbt;
	
	public SyncBlockEntityMessage(BlockPos pos, CompoundTag nbt) {
		this(pos.getX(), pos.getY(), pos.getZ(), nbt);
	}
	
	public SyncBlockEntityMessage(int x, int y, int z, CompoundTag nbt) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.nbt = nbt;
	}
	
	public static void encode(SyncBlockEntityMessage msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.x).writeInt(msg.y).writeInt(msg.z);
		buf.writeNbt(msg.nbt);
	}

	public static SyncBlockEntityMessage decode(FriendlyByteBuf buf) {
		return new SyncBlockEntityMessage(buf.readInt(), buf.readInt(), buf.readInt(), 
				buf.readNbt());
	}

	public static void handle(SyncBlockEntityMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();

		if(context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				ServerLevel world = Objects.requireNonNull(context.getSender()).getLevel();
				LogUtils.getLogger().info("Server");
				BlockPos pos = new BlockPos(msg.x, msg.y, msg.z);
				if(world.isLoaded(pos)) {
					BlockEntity tile = world.getBlockEntity(pos);
					if(tile instanceof BinderBlockEntity)
						((BinderBlockEntity)tile).receiveMessageFromClient(msg.nbt);
				}
			});
		} else {
			context.enqueueWork(() -> {
				Level world = Evilord.proxy.getClientWorld();
				LogUtils.getLogger().info("Client");
				if(world!=null) { // This can happen if the task is scheduled right before leaving the world
					BlockEntity tile = world.getBlockEntity(new BlockPos(msg.x, msg.y, msg.z));
					if(tile instanceof BinderBlockEntity)
						((BinderBlockEntity)tile).receiveMessageFromServer(msg.nbt);
				}
			});
		}
		context.setPacketHandled(true);
	}
}
