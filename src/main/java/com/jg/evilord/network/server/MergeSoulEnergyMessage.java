package com.jg.evilord.network.server;

import java.util.function.Supplier;

import com.jg.evilord.entities.block.AbstractBinderBlockEntity;
import com.jg.evilord.utils.Pos;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.network.NetworkEvent.Context;

public class MergeSoulEnergyMessage {

	private boolean add;
	private int amount;
	private int x;
	private int y;
	private int z;
	
	public MergeSoulEnergyMessage(boolean add, int amount, BlockPos target) {
		this(add, amount, target.getX(), target.getY(), target.getZ());
	}
	
	public MergeSoulEnergyMessage(boolean add, int amount, int[] target) {
		this(add, amount, target[0], target[1], target[2]);
	}
	
	public MergeSoulEnergyMessage(boolean add, int amount, int x, int y, 
			int z) {
		this.add = add;
		this.amount = amount;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static void encode(MergeSoulEnergyMessage msg, FriendlyByteBuf buf) {
		buf.writeBoolean(msg.add);
		buf.writeInt(msg.amount);
		buf.writeInt(msg.x);
		buf.writeInt(msg.y);
		buf.writeInt(msg.z);
	}

	public static MergeSoulEnergyMessage decode(FriendlyByteBuf buf) {
		return new MergeSoulEnergyMessage(buf.readBoolean(), 
				buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
	}

	public static void handle(MergeSoulEnergyMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();

		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			AbstractBinderBlockEntity<?> master = (AbstractBinderBlockEntity<?>)player.level
					.getBlockEntity(new BlockPos(msg.x, msg.y, msg.z));
			LogUtils.getLogger().info(master.getLinks().toString());
			if(true) { // msg.add
				for(Pos p : master.getLinks().getOutputs()) {
					if(!p.isEmpty()) {
						AbstractBinderBlockEntity<?> bve = 
								(AbstractBinderBlockEntity<?>)player.level
								.getBlockEntity(p.toBlockPos());
						/*int inserted = bve.getCapability(CapabilityEnergy.ENERGY)
								.orElse(null).receiveEnergy(msg.amount, false);
						bve.onEnergyChanged(inserted, false);*/
						int amount = 0;
						boolean extracted = true;
						if(msg.add) { 
							amount = bve.getCapability(CapabilityEnergy.ENERGY)
								.orElse(null).receiveEnergy(msg.amount, false);
							extracted = false;
						} else {
							amount = bve.getCapability(CapabilityEnergy.ENERGY)
									.orElse(null).extractEnergy(msg.amount, false);
						}
						bve.onEnergyChanged(amount, extracted);
					}
				}
			} else {
				for(Pos p : master.getLinks().getInputs()) {
					if(!p.isEmpty()) {
						AbstractBinderBlockEntity<?> bve = 
								(AbstractBinderBlockEntity<?>)player.level
								.getBlockEntity(p.toBlockPos());
						int extracted = bve.getCapability(CapabilityEnergy.ENERGY)
								.orElse(null).extractEnergy(msg.amount, false);
						bve.onEnergyChanged(extracted, true);
					}
				}
			}
		});
	}
	
}
