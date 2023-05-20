package com.jg.evilord.entities.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MenuBlockEntity<L extends AbstractContainerMenu> extends BlockEntity 
				implements IJgMenuProvider<MenuBlockEntity<L>, L>{

	public MenuBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

}
