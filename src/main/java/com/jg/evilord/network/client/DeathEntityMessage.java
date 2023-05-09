package com.jg.evilord.network.client;

import java.util.function.Supplier;

import com.jg.evilord.client.helpers.ClientMessages;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class DeathEntityMessage {

	public int killer;
	public int killed;
	
	public DeathEntityMessage(int killer, int killed) {
		this.killer = killer;
		this.killed = killed;
	}

	public static void encode(DeathEntityMessage msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.killer);
		buf.writeInt(msg.killed);
	}

	public static DeathEntityMessage decode(FriendlyByteBuf buf) {
		return new DeathEntityMessage(buf.readInt(), buf.readInt());
	}

	public static void handle(DeathEntityMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientMessages
				.deathEntity(msg.killer, msg.killed));
		context.setPacketHandled(true);
	}
	
}
