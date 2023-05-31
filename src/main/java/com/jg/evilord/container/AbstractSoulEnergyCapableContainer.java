package com.jg.evilord.container;

import com.jg.evilord.entities.block.AbstractBinderBlockEntity;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AbstractSoulEnergyCapableContainer<T extends AbstractBinderBlockEntity<?>> extends AbstractContainerMenu {
	
	protected T blockEntity;
	protected ContainerData data;
	
	protected AbstractSoulEnergyCapableContainer(MenuType<?> p_38851_, int p_38852_, 
			T be, ContainerData data) {
		super(p_38851_, p_38852_);
		this.blockEntity = be;
		this.data = data;
	}
	
	public T getBlockEntity() {
		return blockEntity;
	}
	
	public ContainerData getData() {
		return data;
	}

}
