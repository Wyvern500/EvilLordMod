package com.jg.evilord.block;

import javax.annotation.Nullable;

import com.jg.evilord.entities.block.BinderBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public abstract class EvilordBaseBlockEntity extends BaseEntityBlock {

	protected EvilordBaseBlockEntity(Properties p_49224_) {
		super(p_49224_);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState p_49232_) {
		return RenderShape.MODEL;
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
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState,
			boolean p_60519_) {
		if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof BinderBlockEntity) {
                ((BinderBlockEntity) blockEntity).drops();
                ((BinderBlockEntity) blockEntity).linkChanged(pState, pLevel, 
                		pPos);
            }
        }
		super.onRemove(pState, pLevel, pPos, pNewState, p_60519_);
	}
	
}
