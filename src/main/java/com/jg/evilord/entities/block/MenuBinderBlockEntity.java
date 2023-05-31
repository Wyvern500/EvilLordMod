package com.jg.evilord.entities.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MenuBinderBlockEntity extends AbstractBinderBlockEntity<BlockEntity> implements MenuProvider {
	
	public MenuBinderBlockEntity(BlockEntityType<?> type, BlockPos pos, 
			BlockState state, int capacity, int inputSlots, int outputSlots) {
		super(type, pos, state, capacity, inputSlots, outputSlots);
	}
	
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
		return createMenu(id, inv);
	}
	
	protected abstract AbstractContainerMenu createMenu(int p_58627_, Inventory p_58628_);

}
