package com.jg.evilord.entities.blockentities;

import java.util.Arrays;

import com.jg.evilord.containers.ArtifactCrafterContainer;
import com.jg.evilord.entities.block.AbstractBinderBlockEntity;
import com.jg.evilord.entities.block.ICrafterBlockEntity;
import com.jg.evilord.entities.block.MenuBinderBlockEntity;
import com.jg.evilord.registries.BlockEntityRegistries;
import com.jg.evilord.utils.Pos;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

public class ArtifactCrafterBlockEntity extends MenuBinderBlockEntity implements ICrafterBlockEntity {
	
	private int soulEnergy;
	
	public ArtifactCrafterBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityRegistries.artifactCrafter.get(), pos, state, 100, 6, 0);
		data = new ContainerData() {
			
			@Override
			public void set(int i, int v) {
				switch(i) {
				case 0:
					energyStorage.receiveEnergy(v, false);
					break;
				case 1:
					soulEnergy = v;
					break;
				}
			}
			
			@Override
			public int getCount() {
				return 2;
			}
			
			@Override
			public int get(int i) {
				switch(i) {
				case 0:
					return energyStorage.getEnergyStored();
				case 1:
					return soulEnergy;
				default:
					return energyStorage.getEnergyStored();
				}
			}
		};
	}

	@Override
	public void linkChanged(BlockState pState, Level pLevel, BlockPos pPos) {
		
	}
	
	@Override
	public void onCraft(ServerPlayer player, int souls) {
		extractEnergyOnInputs(souls);
		onEnergyChanged(souls, true);
		if(player.containerMenu != null) {
			player.containerMenu.broadcastChanges();
		}
		calculateEnergy();
	}
	
	@Override
	public boolean canCraft(Player player) {
		return true;
	}
	
	private void extractEnergyOnInputs(int souls) {
		int remaining = souls;
		for(Pos pos : links.getInputs()) {
			if(!pos.isEmpty()) {
				if(remaining > 0) {
					int extracted = level.getBlockEntity(pos.toBlockPos())
						.getCapability(CapabilityEnergy.ENERGY).orElse(null)
						.extractEnergy(remaining, false);
					remaining -= extracted;
					LogUtils.getLogger().info("extracting at " + Arrays
							.toString(pos.pos));
				} else {
					break;
				}
			}
		}
	}
	
	@Override
	public void onInputChange(EnergyStorage storage, 
			AbstractBinderBlockEntity<?> be) {
		calculateEnergy();
	}
	
	public void calculateEnergy() {
		if(!level.isClientSide) {
			soulEnergy = 0;
			for(Pos pos : links.getInputs()) {
				if(!pos.isEmpty()) {
					BlockPos bpos = pos.toBlockPos();
					if(level.isLoaded(bpos)) {
						soulEnergy += level.getBlockEntity(bpos)
								.getCapability(CapabilityEnergy.ENERGY).orElse(null)
								.getEnergyStored();
					}
				}
			}
		}
	}
	
	@Override
	public void onEnergyChanged(int energy, boolean extracted) {
		
	}

	@Override
	public void receiveMessageFromClient(CompoundTag nbt) {
		
	}

	@Override
	public void receiveMessageFromServer(CompoundTag nbt) {
		
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("com.jg.evilord.artifact_crafter_container");
	}
	
	public ContainerData getData() {
		return data;
	}
	
	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory inv) {
		return new ArtifactCrafterContainer(id, inv, data, this);
	}

	@Override
	public void drops() {
		
	}

}
