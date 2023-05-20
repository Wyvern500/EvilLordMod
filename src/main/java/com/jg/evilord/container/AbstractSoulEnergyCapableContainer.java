package com.jg.evilord.container;

import com.jg.evilord.entities.block.BinderBlockEntity;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public abstract class AbstractSoulEnergyCapableContainer<T extends BinderBlockEntity> extends AbstractContainerMenu {
	
	protected T blockEntity;
	
	protected AbstractSoulEnergyCapableContainer(MenuType<?> p_38851_, int p_38852_, 
			T be) {
		super(p_38851_, p_38852_);
		this.blockEntity = be;
	}
	
	public T getBlockEntity() {
		return blockEntity;
	}

}
