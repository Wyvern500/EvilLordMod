package com.jg.evilord.block.blocks;

import com.jg.evilord.block.EvilordBaseBlock;
import com.jg.evilord.entities.blockentities.SoulStorageBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SoulStorageBlock extends EvilordBaseBlock {

	public SoulStorageBlock(Properties p_49224_) {
		super(p_49224_);
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SoulStorageBlockEntity(pos, state);
	}

}
