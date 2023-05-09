package com.jg.evilord.container;

import com.jg.evilord.registries.ContainerRegistries;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SpellExplorerContainer extends AbstractContainerMenu {
	
	private Container entity;
	
	public SpellExplorerContainer(int id, Inventory inv) {
		this(id, inv, new SimpleContainer(4));
	}

	public SpellExplorerContainer(int id, Inventory inv, Container entity) {
		super(ContainerRegistries.spellExplorer.get(), id);

		this.entity = entity;
		
		addSlot(new Slot(entity, 0, 145, 15) {
			@Override
			public void onTake(Player p_150645_, ItemStack p_150646_) {
				super.onTake(p_150645_, p_150646_);
				// Remove result items if there is no sword
				//result.setItem(0, ItemStack.EMPTY);
				//result.setItem(1, ItemStack.EMPTY);
				//LogManager.getLogger().info("Removing result items 1");
				//broadcastChanges();
			}
		});
		
		addSlot(new Slot(entity, 1, 145, 15) {
			@Override
			public void onTake(Player p_150645_, ItemStack p_150646_) {
				super.onTake(p_150645_, p_150646_);
				// Remove result items if there is no sword
				//result.setItem(0, ItemStack.EMPTY);
				//result.setItem(1, ItemStack.EMPTY);
				//LogManager.getLogger().info("Removing result items 1");
				//broadcastChanges();
			}
		});
		
		addSlot(new Slot(entity, 2, 145, 15) {
			@Override
			public void onTake(Player p_150645_, ItemStack p_150646_) {
				super.onTake(p_150645_, p_150646_);
				// Remove result items if there is no sword
				//result.setItem(0, ItemStack.EMPTY);
				//result.setItem(1, ItemStack.EMPTY);
				//LogManager.getLogger().info("Removing result items 1");
				//broadcastChanges();
			}
		});
		
		addSlot(new Slot(entity, 3, 145, 15) {
			@Override
			public void onTake(Player p_150645_, ItemStack p_150646_) {
				super.onTake(p_150645_, p_150646_);
				// Remove result items if there is no sword
				//result.setItem(0, ItemStack.EMPTY);
				//result.setItem(1, ItemStack.EMPTY);
				//LogManager.getLogger().info("Removing result items 1");
				//broadcastChanges();
			}
		});
		
		for (int y1 = 0; y1 < 3; ++y1) {
			for (int x1 = 0; x1 < 9; ++x1) {
				this.addSlot(new Slot(inv, x1 + y1 * 9 + 9, 8 + x1 * 18, 84 + y1 * 18));
			}
		}

		for (int x1 = 0; x1 < 9; ++x1) {
			this.addSlot(new Slot(inv, x1, 8 + x1 * 18, 142));
		}
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public void removed(Player p_38940_) {
		super.removed(p_38940_);
		try {
			//entity.removeItemNoUpdate(3);
			//this.clearContainer(p_38940_, entity);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean stillValid(Player p_75145_1_) {
		return entity.stillValid(p_75145_1_);
	}

}
