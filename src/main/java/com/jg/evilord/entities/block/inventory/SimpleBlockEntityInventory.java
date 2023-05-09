package com.jg.evilord.entities.block.inventory;

import javax.annotation.Nonnull;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class SimpleBlockEntityInventory implements Container, IItemHandler, INBTSerializable<CompoundTag> {

	private NonNullList<ItemStack> items;

	public SimpleBlockEntityInventory(int size) {
		this.items = NonNullList.withSize(size, ItemStack.EMPTY);
	}

	public int getContainerSize() {
		return this.items.size();
	}

	public boolean isEmpty() {
		for (ItemStack itemstack : this.items) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	public ItemStack getItem(int p_39332_) {
		return p_39332_ >= this.getContainerSize() ? ItemStack.EMPTY : this.items.get(p_39332_);
	}

	public ItemStack removeItemNoUpdate(int p_39344_) {
		return ContainerHelper.takeItem(this.items, p_39344_);
	}

	public ItemStack removeItem(int p_39334_, int p_39335_) {
		return ContainerHelper.removeItem(this.items, p_39334_, p_39335_);
	}

	public void setItem(int p_39337_, ItemStack p_39338_) {
		this.items.set(p_39337_, p_39338_);
	}

	public void setChanged() {
	}

	public boolean stillValid(Player p_39340_) {
		return true;
	}

	public void clearContent() {
		this.items.clear();
	}

	public int getSize() {
		return items.size();
	}

	public void fillStackedContents(StackedContents p_39342_) {
		for (ItemStack itemstack : this.items) {
			p_39342_.accountSimpleStack(itemstack);
		}
	}

	@Override
	public int getSlots() {
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		validateSlotIndex(slot);
		return this.items.get(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (stack.isEmpty())
			return ItemStack.EMPTY;

		if (!isItemValid(slot, stack))
			return stack;

		validateSlotIndex(slot);

		ItemStack existing = this.items.get(slot);

		int limit = getStackLimit(slot, stack);

		if (!existing.isEmpty()) {
			if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
				return stack;

			limit -= existing.getCount();
		}

		if (limit <= 0)
			return stack;

		boolean reachedLimit = stack.getCount() > limit;

		if (!simulate) {
			if (existing.isEmpty()) {
				this.items.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
			} else {
				existing.grow(reachedLimit ? limit : stack.getCount());
			}
			onContentsChanged(slot);
		}

		return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (amount == 0)
			return ItemStack.EMPTY;

		validateSlotIndex(slot);

		ItemStack existing = this.items.get(slot);

		if (existing.isEmpty())
			return ItemStack.EMPTY;

		int toExtract = Math.min(amount, existing.getMaxStackSize());

		if (existing.getCount() <= toExtract) {
			if (!simulate) {
				this.items.set(slot, ItemStack.EMPTY);
				onContentsChanged(slot);
				return existing;
			} else {
				return existing.copy();
			}
		} else {
			if (!simulate) {
				this.items.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
				onContentsChanged(slot);
			}

			return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
		}
	}

	@Override
	public int getSlotLimit(int slot) {
		return 4;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return true;
	}

	protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }
	
	public void setSize(int size) {
        items = NonNullList.withSize(size, ItemStack.EMPTY);
    }
	
	@Override
	public CompoundTag serializeNBT() {
		ListTag nbtTagList = new ListTag();
		for (int i = 0; i < items.size(); i++) {
			if (!items.get(i).isEmpty()) {
				CompoundTag itemTag = new CompoundTag();
				itemTag.putInt("Slot", i);
				items.get(i).save(itemTag);
				nbtTagList.add(itemTag);
			}
		}
		CompoundTag nbt = new CompoundTag();
		nbt.put("Items", nbtTagList);
		nbt.putInt("Size", items.size());
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		setSize(nbt.contains("Size", Tag.TAG_INT) ? nbt.getInt("Size") : items.size());
		ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
		for (int i = 0; i < tagList.size(); i++) {
			CompoundTag itemTags = tagList.getCompound(i);
			int slot = itemTags.getInt("Slot");

			if (slot >= 0 && slot < items.size()) {
				items.set(slot, ItemStack.of(itemTags));
			}
		}
		onLoad();
	}

	protected void validateSlotIndex(int slot) {
		if (slot < 0 || slot >= items.size())
			throw new RuntimeException("Slot " + slot + " not in valid range - [0," + items.size() + ")");
	}

	protected void onLoad() {

	}

	protected void onContentsChanged(int slot) {

	}

}
