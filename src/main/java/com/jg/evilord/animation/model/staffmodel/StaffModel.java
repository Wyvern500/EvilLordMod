package com.jg.evilord.animation.model.staffmodel;

import com.jg.evilord.animation.model.JgModel;
import com.jg.evilord.animation.model.JgModelPart;
import com.jg.evilord.client.handler.ClientHandler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class StaffModel extends JgModel {
	
	public StaffModel(JgModelPart[] modelParts, ClientHandler client) {
		super(modelParts, client);
	}

	// Main methods
	
	public abstract void shoot(ItemStack stack, Player player);
	
}
