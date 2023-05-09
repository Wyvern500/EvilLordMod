package com.jg.evilord.network.server;

import java.util.function.Supplier;

import com.jg.evilord.Evilord;
import com.jg.evilord.entities.block.VinculatorBlockEntity;
import com.jg.evilord.network.common.SyncBlockEntityMessage;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.PacketDistributor;

public class ProcessConnectionManipulatorWandMessage {

	private int fx;
	private int fy;
	private int fz;
	private int sx;
	private int sy;
	private int sz;
	
	public ProcessConnectionManipulatorWandMessage(BlockPos first, BlockPos second) {
		this(first.getX(), first.getY(), first.getZ(), second.getX(), second.getY(), 
				second.getZ());
	}
	
	public ProcessConnectionManipulatorWandMessage(int fx, int fy, int fz, int sx, int sy, 
			int sz) {
		this.fx = fx;
		this.fy = fy;
		this.fz = fz;
		this.sx = sx;
		this.sy = sy;
		this.sz = sz;
	}
	
	public static void encode(ProcessConnectionManipulatorWandMessage msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.fx);
		buf.writeInt(msg.fy);
		buf.writeInt(msg.fz);
		buf.writeInt(msg.sx);
		buf.writeInt(msg.sy);
		buf.writeInt(msg.sz);
	}

	public static ProcessConnectionManipulatorWandMessage decode(FriendlyByteBuf buf) {
		return new ProcessConnectionManipulatorWandMessage(buf.readInt(), buf.readInt(), 
				buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
	}

	public static void handle(ProcessConnectionManipulatorWandMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();

		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			BlockPos be1Pos = new BlockPos(msg.fx, msg.fy, msg.fz);
			BlockPos be2Pos = new BlockPos(msg.sx, msg.sy, msg.sz);
			BlockEntity be = player.level.getBlockEntity(be1Pos);
			BlockEntity be2 = player.level.getBlockEntity(be2Pos);
			LogUtils.getLogger().info("Executing");
			if(be != null && be2 != null) {
				if(be instanceof VinculatorBlockEntity && be2 instanceof VinculatorBlockEntity) {
					((VinculatorBlockEntity)be).vinculate = String.valueOf(msg.sx) + "," + 
							String.valueOf(msg.sy) + "," + String.valueOf(msg.sz);
					((VinculatorBlockEntity)be2).vinculate = String.valueOf(msg.fx) + "," + 
					String.valueOf(msg.fy) + "," + String.valueOf(msg.fz);
					LazyOptional<IEnergyStorage> lazy = 
							be.getCapability(CapabilityEnergy.ENERGY);
					lazy.ifPresent((cap) -> {
						cap.receiveEnergy(100, false);
					});
					Evilord.channel.send(PacketDistributor.TRACKING_CHUNK
							.with(() -> player.level.getChunk(be1Pos.getX(), be1Pos.getZ())), 
							new SyncBlockEntityMessage(be1Pos, be.serializeNBT()));
					LogUtils.getLogger().info("Energy stored: " + lazy.resolve().get().getEnergyStored());
					player.level.sendBlockUpdated(be1Pos, be.getBlockState(), be.getBlockState(), 2);
					player.level.sendBlockUpdated(be2Pos, be2.getBlockState(), be2.getBlockState(), 2);
				}
			}
		});
		context.setPacketHandled(true);
	}
}
