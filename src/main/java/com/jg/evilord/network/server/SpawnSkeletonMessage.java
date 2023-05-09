package com.jg.evilord.network.server;

import java.util.function.Supplier;
import java.util.logging.Logger;

import com.jg.evilord.utils.NBTUtils;
import com.mojang.logging.LogUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
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
			ItemStack stack = player.getMainHandItem();
			NBTUtils.setSouls(stack, 
					Math.min(NBTUtils.getMaxSoulsForTier(stack), 
							NBTUtils.getSouls(stack) + 1));
		});
		context.setPacketHandled(true);
	}
	
}
