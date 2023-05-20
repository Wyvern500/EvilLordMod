package com.jg.evilord.containers;

import java.util.logging.Logger;

import org.apache.logging.log4j.LogManager;

import com.jg.evilord.container.inventory.ResultInventory;
import com.jg.evilord.container.inventory.SimpleInventory;
import com.jg.evilord.item.SoulContainerItem;
import com.jg.evilord.registries.BlockRegistries;
import com.jg.evilord.registries.ContainerRegistries;
import com.jg.evilord.utils.NBTUtils;
import com.jg.evilord.utils.Utils;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SoulManipulatorContainer extends AbstractContainerMenu {

	private ContainerLevelAccess access;
	private SimpleInventory inventory;
	private ResultInventory result;

	public SoulManipulatorContainer(int id, Inventory inv) {
		this(id, inv, ContainerLevelAccess.NULL);
	}

	public SoulManipulatorContainer(int id, Inventory inv, final ContainerLevelAccess access) {
		super(ContainerRegistries.soulContainerManipulator.get(), id);

		this.access = access;

		inventory = new SimpleInventory(this, 2);
		result = new ResultInventory(this, 2);

		addSlot(new Slot(inventory, 0, 12, 33) {
			@Override
			public void onTake(Player p_150645_, ItemStack p_150646_) {
				super.onTake(p_150645_, p_150646_);
				// Remove result items if there is no sword
				result.setItem(0, ItemStack.EMPTY);
				result.setItem(1, ItemStack.EMPTY);
				LogManager.getLogger().info("Removing result items 1");
				broadcastChanges();
			}
		});

		addSlot(new Slot(inventory, 1, 61, 33) {
			@Override
			public void onTake(Player p_150645_, ItemStack p_150646_) {
				super.onTake(p_150645_, p_150646_);
				// Remove result items if sword has no container
				if(!inventory.getItem(0).isEmpty()) {
					result.setItem(0, ItemStack.EMPTY);
					result.setItem(1, ItemStack.EMPTY);
					LogManager.getLogger().info("Removing result items 1");
					broadcastChanges();
				}
			}
		});

		addSlot(new Slot(result, 0, 119, 33) {
			@Override
			public void onTake(Player p_150645_, ItemStack p_150646_) {
				super.onTake(p_150645_, p_150646_);
				SoulManipulatorContainer.this.onTake(p_150646_, 0);
			}
		});

		addSlot(new Slot(result, 1, 147, 33) {
			@Override
			public void onTake(Player p_150645_, ItemStack p_150646_) {
				super.onTake(p_150645_, p_150646_);
				SoulManipulatorContainer.this.onTake(p_150646_, 1);
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
	public void slotsChanged(Container container) {
		super.slotsChanged(container);
		
		try {
			ItemStack slot0 = inventory.getItem(0);
			ItemStack slot1 = inventory.getItem(1);
			if(!slot0.isEmpty()) {
				if(NBTUtils.hasContainer(slot0) && !slot1.isEmpty()) {
					// Swap soul containers
					int oldSouls = 0;
					int oldTier = 0;
					oldSouls = NBTUtils.getSouls(slot0);
					oldTier = NBTUtils.getContainerTier(slot0);
					
					ItemStack copy = slot0.copy();
					
					NBTUtils.setContainerTier(copy, ((SoulContainerItem)slot1.getItem())
							.getTier().ordinal());
					NBTUtils.setSouls(copy, NBTUtils.getSouls(slot1));
					
					LogManager.getLogger().info("Point 1");
					result.setItem(0, copy);
					ItemStack newContainer = new ItemStack(Utils.getItemForTier(oldTier));
					NBTUtils.setSouls(newContainer, oldSouls);
					result.setItem(1, newContainer);
					broadcastChanges();
				} else if(NBTUtils.hasContainer(slot0)) {
					// Extract soul container
					int oldSouls = 0;
					int oldTier = 0;
					oldSouls = NBTUtils.getSouls(slot0);
					oldTier = NBTUtils.getContainerTier(slot0);
					
					ItemStack copy = slot0.copy();
					
					NBTUtils.setSouls(copy, 0);
					NBTUtils.setContainerTier(copy, -1);
					NBTUtils.setHasContainer(copy, false);

					ItemStack newContainer = new ItemStack(Utils.getItemForTier(oldTier));
					NBTUtils.setSouls(newContainer, oldSouls);
					LogManager.getLogger().info("Point 4");
					result.setItem(0, copy);
					result.setItem(1, newContainer);
					broadcastChanges();
				}/* else if(slot1.isEmpty()) {
					// Remove result items
					result.setItem(0, ItemStack.EMPTY);
					result.setItem(1, ItemStack.EMPTY);
					LogManager.getLogger().info("Removing result items 1");
					broadcastChanges();
				}*/
				if(!slot1.isEmpty()) {
					// Put soul container
					ItemStack copy = slot0.copy();
					NBTUtils.setContainerTier(copy, ((SoulContainerItem)slot1.getItem())
							.getTier().ordinal());
					NBTUtils.setSouls(copy, NBTUtils.getSouls(slot1));
					NBTUtils.setHasContainer(copy, true);
					
					LogManager.getLogger().info("Point 2");
					result.setItem(0, copy);
					LogManager.getLogger().info("Point 3");
					
					broadcastChanges();
				}
			}/* else {
				// Remove result items
				result.setItem(0, ItemStack.EMPTY);
				result.setItem(1, ItemStack.EMPTY);
				LogManager.getLogger().info("Removing result items 2");
				broadcastChanges();
			}*/
			LogManager.getLogger().info("Something changed on inventory hehe");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void onTake(ItemStack stack, int index) {
		ItemStack backup = ItemStack.EMPTY;
		if(index == 0) {
			
		} else {
			
		}
		// Remove result items
		inventory.setItem(0, ItemStack.EMPTY);
		inventory.setItem(1, ItemStack.EMPTY);
		LogManager.getLogger().info("Taking");
		broadcastChanges();
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		try {
			ItemStack stack = ItemStack.EMPTY;
			Slot slot = this.slots.get(index);
			Logger.getGlobal().info("Index: " + index);
			if (slot != null && slot.hasItem()) {
				ItemStack itemstack1 = slot.getItem();
				stack = itemstack1.copy();
				
				if(index >= 4) {
					if(itemstack1.getItem() instanceof SoulContainerItem) { // Luego lo cambiare por un item en especifico, es decir instanceof SoulContainerItem
						if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
							return ItemStack.EMPTY;
						}
					} else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else {
					if (!this.moveItemStackTo(itemstack1, 4, 40, false)) {
						return ItemStack.EMPTY;
					}
				}
	
				if (itemstack1.isEmpty()) {
					slot.set(ItemStack.EMPTY);
				}
	
				slot.setChanged();
				if (itemstack1.getCount() == stack.getCount()) {
					return ItemStack.EMPTY;
				}
	
				slot.onTake(player, itemstack1);
				this.broadcastChanges();
			}
			return stack;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void removed(Player p_38940_) {
		super.removed(p_38940_);
		try {
		inventory.removeItemNoUpdate(4);
		this.access.execute((p_39152_, p_39153_) -> {
			this.clearContainer(p_38940_, this.inventory);
		});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean stillValid(Player p_75145_1_) {
		return super.stillValid(ContainerLevelAccess.NULL, p_75145_1_, 
				BlockRegistries.soulManipulator.get());
	}

}
