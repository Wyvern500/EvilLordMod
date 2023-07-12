package com.jg.evilord.entities.block;

import javax.annotation.Nonnull;

import com.jg.evilord.entities.block.inventory.SimpleBlockEntityInventory;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class InventoryBinderBlockEntity extends AbstractBinderBlockEntity<InventoryBinderBlockEntity> implements Container, MenuProvider {

	protected LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
	protected SimpleBlockEntityInventory inv;
	
	public InventoryBinderBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_,
			int capacity, int inventorySize, int inputSlots, int outputSlots) {
		super(p_155228_, p_155229_, p_155230_, capacity, inputSlots, outputSlots);
		inv = new SimpleBlockEntityInventory(inventorySize);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return lazyItemHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		lazyItemHandler = LazyOptional.of(() -> inv);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		lazyItemHandler.invalidate();
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		tag.put("inventory", inv.serializeNBT());
		super.saveAdditional(tag);
	}
	
	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		inv.deserializeNBT(nbt.getCompound("inventory"));
	}
	
	@Override
	public int getContainerSize() {
		return inv.getSize();
	}

	@Override
	public boolean isEmpty() {
		return inv.isEmpty();
	}

	@Override
	public ItemStack getItem(int p_18941_) {
		return inv.getItem(p_18941_);
	}

	@Override
	public ItemStack removeItem(int p_18942_, int p_18943_) {
		return inv.removeItem(p_18942_, p_18943_);
	}

	@Override
	public ItemStack removeItemNoUpdate(int p_18951_) {
		return inv.removeItemNoUpdate(p_18951_);
	}

	@Override
	public void setItem(int p_18944_, ItemStack p_18945_) {
		inv.setItem(p_18944_, p_18945_);
	}
	
	@Override
	public void clearContent() {
		inv.clearContent();
	}
	
	@Override
	public boolean stillValid(Player player) {
		if (this.isRemoved()) {
			return false;
		} else {
			return !(worldPosition.distSqr(player.blockPosition()) > 64.0D);
		}
	}

	@Override
	public void drops() {
		Containers.dropContents(this.level, this.worldPosition, inv);
	}

}
