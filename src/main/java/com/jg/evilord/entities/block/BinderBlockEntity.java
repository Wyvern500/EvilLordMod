package com.jg.evilord.entities.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BinderBlockEntity extends BaseContainerBlockEntity implements IEnergyCapableBlockEntity<BinderBlockEntity>{

	protected String link;
	protected ContainerData data;
	
	public static final String LINK = "link";
	
	protected BinderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		
		data = new ContainerData() {
			
			@Override
			public void set(int i, int v) {
				switch(i) {
				case 0:
					getSoulEnergyStorage().receiveEnergy(v, false);
					break;
				}
			}
			
			@Override
			public int getCount() {
				return 1;
			}
			
			@Override
			public int get(int i) {
				return getSoulEnergyStorage().getEnergyStored();
			}
		};
	}

	public void linkWith(float x, float y, float z) {
		linkWith(new BlockPos(x, y, z));
	}
	
	public void linkWith(BlockPos pos) {
		if(link != null) {
			((BinderBlockEntity)level.getBlockEntity(pos)).unbind();
		}
		link = String.valueOf(pos.getX()) + "," + String.valueOf(pos.getY()) + "," + 
				String.valueOf(pos.getZ());
	}

	public void unbind() {
		link = null;
	}
	
	public abstract void linkChanged(BlockState pState, Level pLevel, BlockPos pPos);
	
	public abstract void drops();
	
	public abstract void receiveMessageFromClient(CompoundTag nbt);
	
	public abstract void receiveMessageFromServer(CompoundTag nbt);
	
	// Getters and setters
	
	public ContainerData getData() {
		return data;
	}
	
}
