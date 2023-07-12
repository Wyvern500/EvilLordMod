package com.jg.evilord.registries;

import com.mojang.logging.LogUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;

public class JgMenuType<T extends AbstractContainerMenu> implements IForgeMenuType<T> {

	IContainerFactory<T> container;
	
	public JgMenuType(IContainerFactory<T> p_39984_) {
		this.container = p_39984_;
	}

	@Override
	public T create(int windowId, Inventory playerInv, FriendlyByteBuf extraData) {
		LogUtils.getLogger().info("Creating with additional data");
		return container.create(windowId, playerInv, extraData);
	}
	
	/*@Override
	public T create(int p_39986_, Inventory p_39987_) {
		LogUtils.getLogger().info("Creating normal");
		return super.create(p_39986_, p_39987_);
	}
	
	@Override
	public T create(int windowId, Inventory playerInv, FriendlyByteBuf extraData) {
		LogUtils.getLogger().info("Creating with additional data");
		return super.create(windowId, playerInv, extraData);
	}*/
	
}
