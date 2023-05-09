package com.jg.evilord.container.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class SimpleInventory implements Container {

	private final NonNullList<ItemStack> items;
	private final AbstractContainerMenu menu;

	public SimpleInventory(AbstractContainerMenu p_39325_, int size) {
		this.items = NonNullList.withSize(size, ItemStack.EMPTY);
		this.menu = p_39325_;
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
		ItemStack itemstack = ContainerHelper.removeItem(this.items, p_39334_, p_39335_);
		if (!itemstack.isEmpty()) {
			this.menu.slotsChanged(this);
		}

		return itemstack;
	}

	public void setItem(int p_39337_, ItemStack p_39338_) {
		this.items.set(p_39337_, p_39338_);
		this.menu.slotsChanged(this);
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

}
