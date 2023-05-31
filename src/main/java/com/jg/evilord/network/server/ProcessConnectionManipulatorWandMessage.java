package com.jg.evilord.network.server;

import java.util.function.Supplier;

import com.jg.evilord.entities.block.AbstractBinderBlockEntity;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent.Context;

public class ProcessConnectionManipulatorWandMessage {

	private int fx;
	private int fy;
	private int fz;
	private int sx;
	private int sy;
	private int sz;
	private boolean add;
	
	public ProcessConnectionManipulatorWandMessage(boolean add, BlockPos first, BlockPos second) {
		this(add, first.getX(), first.getY(), first.getZ(), second.getX(), second.getY(), 
				second.getZ());
	}
	
	public ProcessConnectionManipulatorWandMessage(boolean add, int fx, int fy, 
			int fz, int sx, int sy, int sz) {
		this.add = add;
		this.fx = fx;
		this.fy = fy;
		this.fz = fz;
		this.sx = sx;
		this.sy = sy;
		this.sz = sz;
	}
	
	public static void encode(ProcessConnectionManipulatorWandMessage msg, FriendlyByteBuf buf) {
		buf.writeBoolean(msg.add);
		buf.writeInt(msg.fx);
		buf.writeInt(msg.fy);
		buf.writeInt(msg.fz);
		buf.writeInt(msg.sx);
		buf.writeInt(msg.sy);
		buf.writeInt(msg.sz);
	}

	public static ProcessConnectionManipulatorWandMessage decode(FriendlyByteBuf buf) {
		return new ProcessConnectionManipulatorWandMessage(buf.readBoolean(), 
				buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt(), 
				buf.readInt(), buf.readInt());
	}

	public static void handle(ProcessConnectionManipulatorWandMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();

		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			BlockPos be1Pos = new BlockPos(msg.fx, msg.fy, msg.fz);
			BlockPos be2Pos = new BlockPos(msg.sx, msg.sy, msg.sz);
			BlockEntity be = player.level.getBlockEntity(be1Pos);
			BlockEntity be2 = player.level.getBlockEntity(be2Pos);
			/*LogUtils.getLogger().info("Executing: " + (be instanceof 
					AbstractBinderBlockEntity<?>) + " 2: " + 
					(be2 instanceof AbstractBinderBlockEntity<?>));*/
			if(be instanceof AbstractBinderBlockEntity && be2 instanceof AbstractBinderBlockEntity) {
				AbstractBinderBlockEntity<?> vbe = ((AbstractBinderBlockEntity<?>)be);
				AbstractBinderBlockEntity<?> vbe2 = ((AbstractBinderBlockEntity<?>)be2);
				//LogUtils.getLogger().info("before be1: " + vbe.getLinks().toString() + 
				//		" be2: " + vbe2.getLinks().toString());
				LogUtils.getLogger().info("bbe1: " + vbe.getLinks().toString());
				LogUtils.getLogger().info("bbe2: " + vbe2.getLinks().toString());
				if(msg.add) {
					vbe.linkWith(vbe2);
					LogUtils.getLogger().info("Add true");
				} else {
					vbe.unbind(vbe2);
					LogUtils.getLogger().info("Remove true");
				}
				/*LogUtils.getLogger().info("after be1: " + vbe.getLinks().toString() + 
						" be2: " + vbe2.getLinks().toString());*/
				//vbe2.linkWith(msg.fx, msg.fy, msg.fz);
				// player.level.sendBlockUpdated(be1Pos, be.getBlockState(), be.getBlockState(), 2);
				// player.level.sendBlockUpdated(be2Pos, be2.getBlockState(), be2.getBlockState(), 2);
				//vbe.linkChanged(be.getBlockState(), player.level, be1Pos);
				//vbe2.linkChanged(be2.getBlockState(), player.level, be2Pos);
				LogUtils.getLogger().info("Finished");
			}
		});
		context.setPacketHandled(true);
	}
}
