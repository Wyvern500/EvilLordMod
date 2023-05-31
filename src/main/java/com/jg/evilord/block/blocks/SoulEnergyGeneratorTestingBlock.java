package com.jg.evilord.block.blocks;

import com.jg.evilord.block.EvilordBaseBlock;
import com.jg.evilord.entities.blockentities.TestGeneratorBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SoulEnergyGeneratorTestingBlock extends EvilordBaseBlock {

	public SoulEnergyGeneratorTestingBlock(Properties p_49224_) {
		super(p_49224_);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
		return new TestGeneratorBlockEntity(p_153215_, p_153216_);
	}

}
