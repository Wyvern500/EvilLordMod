package com.jg.evilord.block.blocks;

import com.jg.evilord.block.EvilordBaseBlockEntity;
import com.jg.evilord.entities.block.SpellExplorerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SpellExplorerBlock extends EvilordBaseBlockEntity {

	public SpellExplorerBlock(Properties p_49795_) {
		super(p_49795_);
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
		return new SpellExplorerBlockEntity(p_153215_, p_153216_);
	}
}
