package com.jg.evilord.client.models.items;

import com.jg.evilord.animation.model.JgModel;
import com.jg.evilord.animation.model.JgModelPart;
import com.jg.evilord.client.handler.ClientHandler;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;

public class DeathStaffModel extends JgModel {

	public DeathStaffModel(ClientHandler client) {
		super(new JgModelPart[] {
			new JgModelPart("rightarm"),
			new JgModelPart("leftarm"),
			new JgModelPart("staff"),
			new JgModelPart("rightarmstaff"),
			new JgModelPart("all")
		}, client);
	}

	@Override
	public void render(LocalPlayer player, ItemStack stack, MultiBufferSource buffer, PoseStack matrix, int light) {
		
	}

	@Override
	public JgModelPart getModelPart() {
		return null;
	}

}
