package com.jg.evilord.network.server;

import java.util.function.Supplier;

import com.jg.evilord.spell.handler.SpellsHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;

public class RunSpellMessage {

	String spellPath;
	
	public RunSpellMessage(String spellPath) {
		this.spellPath = spellPath;
	}
	
	public static void encode(RunSpellMessage msg, FriendlyByteBuf buf) {
		buf.writeUtf(msg.spellPath);
	}

	public static RunSpellMessage decode(FriendlyByteBuf buf) {
		return new RunSpellMessage(buf.readUtf(32727));
	}

	public static void handle(RunSpellMessage msg, Supplier<Context> ctx) {
		Context context = ctx.get();

		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			ItemStack stack = player.getMainHandItem();
			SpellsHandler.INSTANCE.getSpell(new ResourceLocation(msg.spellPath))
				.onLaunch(stack, player);
		});
		context.setPacketHandled(true);
	}
}
