package com.jg.evilord.entities.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class VinculatorBlockEntity extends BaseContainerBlockEntity {

	public String vinculate;
	
	protected VinculatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public static final String VINCULATE = "vinculate";
	
	public abstract void vinculationChanged(BlockState pState, Level pLevel, BlockPos pPos);
	
	public abstract void drops();
	
	public abstract void receiveMessageFromClient(CompoundTag nbt);
	
	public abstract void receiveMessageFromServer(CompoundTag nbt);
	
}
