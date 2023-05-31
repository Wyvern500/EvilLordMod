package com.jg.evilord.entities.blockentities;

import com.jg.evilord.containers.SoulStorageContainer;
import com.jg.evilord.entities.block.InventoryBinderBlockEntity;
import com.jg.evilord.registries.BlockEntityRegistries;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SoulStorageBlockEntity extends InventoryBinderBlockEntity {
	
	public SoulStorageBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityRegistries.soulStorage.get(), pos, state, 300, 2, 1, 2);
	}

	@Override
	public void linkChanged(BlockState pState, Level pLevel, BlockPos pPos) {
		
	}

	@Override
	public void receiveMessageFromClient(CompoundTag nbt) {
		
	}

	@Override
	public void receiveMessageFromServer(CompoundTag nbt) {
		
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("com.jg.evilord.soul_storage_container");
	}

	@Override
	public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
		return new SoulStorageContainer(p_39954_, p_39955_, data, this);
	}

	@Override
	public void onEnergyChanged(int energy, boolean extracted) {
		
	}

}
