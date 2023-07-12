package com.jg.evilord.entities.blockentities;

import com.jg.evilord.containers.TestGeneratorContainer;
import com.jg.evilord.entities.block.MenuBinderBlockEntity;
import com.jg.evilord.registries.BlockEntityRegistries;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TestGeneratorBlockEntity extends MenuBinderBlockEntity {
	
	public TestGeneratorBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityRegistries.testGenerator.get(), pos, state, 0, 6, 6);
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("");
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int p_58627_, Inventory p_58628_) {
		return new TestGeneratorContainer(p_58627_, p_58628_, data, this);
	}
	
	@Override
	public void linkWith(BlockPos pos) {
		super.linkWith(pos);
	}
	
	@Override
	public void linkWith(float x, float y, float z) {
		super.linkWith(x, y, z);
	}

	@Override
	public void linkChanged(BlockState pState, Level pLevel, BlockPos pPos) {
		
	}

	@Override
	public void drops() {
		
	}

	@Override
	public void receiveMessageFromClient(CompoundTag nbt) {
		
	}

	@Override
	public void receiveMessageFromServer(CompoundTag nbt) {
		
	}

	@Override
	public void onEnergyChanged(int energy, boolean extracted) {
		// TODO Auto-generated method stub
		
	}

}
