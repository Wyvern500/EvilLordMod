package com.jg.evilord.network.server;

import java.util.function.Supplier;

import com.jg.evilord.utils.NBTUtils;
import com.mojang.logging.LogUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkEvent.Context;

public class SpawnSkeletonMessage {

	public SpawnSkeletonMessage() {
		
	}
	
	public static void encode(SpawnSkeletonMessage msg, FriendlyByteBuf buf) {

	}

	public static SpawnSkeletonMessage decode(FriendlyByteBuf buf) {
		return new SpawnSkeletonMessage();
	}

	public static void handle(SpawnSkeletonMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();

		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			/*MinionSkeleton sk = EntityRegistries.BASICSKELETONMINION.get().create(player.level);
			sk.setPos(player.position());
			player.level.addFreshEntity(sk);*/
			//LogUtils.getLogger().info("SpawnSkeleton");
			/*ItemStack stack = player.getMainHandItem();
			NBTUtils.setSouls(stack, 
					Math.min(NBTUtils.getMaxSoulsForTier(stack), 
							NBTUtils.getSouls(stack) + 1));*/
			/*LogUtils.getLogger().info("Added? " + ((ServerLevel)player.level)
					.addFreshEntity(new ItemEntity(player.level, 
							player.getX(), player.getY(), player.getZ(), 
							new ItemStack(Items.ACACIA_BOAT, 6))));*/
			
		});
		context.setPacketHandled(true);
	}
	
}
