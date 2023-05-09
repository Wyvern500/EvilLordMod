package com.jg.evilord.utils;

import com.jg.evilord.Evilord;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

public class MessageUtils {

	public static void sendMessageFromServerToClientPlayer(ServerPlayer player, Object msg) {
		Evilord.channel.send(PacketDistributor.PLAYER.with(() -> player), msg);
	}
	
	public static void sendMessageFromServerToChunk(ServerLevel world, BlockPos chunk, 
			Object obj) {
		Evilord.channel.send(PacketDistributor.TRACKING_CHUNK
				.with(() -> world.getChunkAt(chunk)), obj);
	}
	
	public static void sendMessageFromServerToAll(Object obj) {
		Evilord.channel.send(PacketDistributor.ALL.noArg(), obj);
	}
	
	public static void sendMessageFromServerToNear(BlockPos pos, double radius, ResourceKey<Level> dimension, 
			Object obj) {
		Evilord.channel.send(PacketDistributor.NEAR.with(() -> new PacketDistributor
				.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), radius, dimension)), obj);
	}
	
	public static void sendMessageFromServerToTrackingAndSelf(ServerPlayer player, Object obj) {
		Evilord.channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), obj);
	}
	
}
