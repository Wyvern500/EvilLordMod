package com.jg.evilord.containers;

import com.jg.evilord.container.AbstractSoulEnergyCapableContainer;
import com.jg.evilord.entities.blockentities.SoulStorageBlockEntity;
import com.jg.evilord.registries.ContainerRegistries;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;

public class SoulStorageContainer extends AbstractSoulEnergyCapableContainer<SoulStorageBlockEntity>{

	public SoulStorageContainer(int id, Inventory inv, FriendlyByteBuf buf) {
		this(id, inv, new SimpleContainerData(1), (SoulStorageBlockEntity) inv.player.level
				.getBlockEntity(buf.readBlockPos()));
	}
	
	public SoulStorageContainer(int id, Inventory inv, ContainerData data,
			SoulStorageBlockEntity be) {
		super(ContainerRegistries.soulStorage.get(), id, be, data);
		
		addSlot(new Slot(be, 0, 46, 22));
		
		addSlot(new Slot(be, 1, 46, 50));
		
		for (int y1 = 0; y1 < 3; ++y1) {
			for (int x1 = 0; x1 < 9; ++x1) {
				this.addSlot(new Slot(inv, x1 + y1 * 9 + 9, 8 + x1 * 18, 84 + y1 * 18));
			}
		}

		for (int x1 = 0; x1 < 9; ++x1) {
			this.addSlot(new Slot(inv, x1, 8 + x1 * 18, 142));
		}
		
		addDataSlots(data);
	}

	@Override
	public boolean stillValid(Player p_38874_) {
		return true;
	}

}
