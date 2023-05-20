package com.jg.evilord.block.blocks;

import javax.annotation.Nullable;

import com.jg.evilord.containers.SoulManipulatorContainer;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class SoulManipulatorBlock extends Block {

	private static final Component CONTAINER_TITLE = new TranslatableComponent(
			"com.jg.container.soul_manipulator_container");
	
	public SoulManipulatorBlock(Properties p_49795_) {
		super(p_49795_);
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
			BlockHitResult hit) {
		if (!worldIn.isClientSide) {
			NetworkHooks.openGui((ServerPlayer) player, this.getMenuProvider(state, worldIn, pos), pos);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public MenuProvider getMenuProvider(BlockState p_60563_, Level p_60564_, BlockPos p_60565_) {
		return new SimpleMenuProvider((p_52229_, p_52230_, p_52231_) -> {
			return new SoulManipulatorContainer(p_52229_, p_52230_, 
					ContainerLevelAccess.create(p_60564_, p_60565_));
		}, CONTAINER_TITLE);
	}

}
