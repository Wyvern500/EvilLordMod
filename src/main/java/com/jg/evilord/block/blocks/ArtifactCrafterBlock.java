package com.jg.evilord.block.blocks;

import com.jg.evilord.block.EvilordBaseBlock;
import com.jg.evilord.containers.ArtifactCrafterContainer;
import com.jg.evilord.entities.blockentities.ArtifactCrafterBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class ArtifactCrafterBlock extends EvilordBaseBlock {

	public ArtifactCrafterBlock(Properties p_49224_) {
		super(p_49224_);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
		return new ArtifactCrafterBlockEntity(p_153215_, p_153216_);
	}

}
