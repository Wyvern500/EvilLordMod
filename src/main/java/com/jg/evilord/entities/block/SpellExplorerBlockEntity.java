package com.jg.evilord.entities.block;

import javax.annotation.Nonnull;

import com.jg.evilord.container.SpellExplorerContainer;
import com.jg.evilord.entities.block.inventory.SimpleBlockEntityInventory;
import com.jg.evilord.registries.BlockEntityRegistries;
import com.jg.evilord.soul.energy.ISoulEnergyStorage;
import com.jg.evilord.soul.energy.SoulEnergyStorage;
import com.jg.evilord.soul.energy.SoulStorage;
import com.jg.evilord.utils.EnergyUtils;
import com.mojang.datafixers.types.templates.Tag.TagType;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagTypes;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class SpellExplorerBlockEntity extends VinculatorBlockEntity implements IEnergyCapableBlockEntity {

	private static final Component CONTAINER_TITLE = new TranslatableComponent(
			"com.jg.container.spell_explorer_container");
	
	private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
	private SimpleBlockEntityInventory inv;
	private SoulEnergyStorage soulEnergyStorage;

	public SpellExplorerBlockEntity(BlockPos pos, BlockState state) {
		this(BlockEntityRegistries.spellExplorer.get(), pos, state);
		LogUtils.getLogger().info("Changes Test");
	}
	
	protected SpellExplorerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.vinculate = "";
		inv = new SimpleBlockEntityInventory(4);
		this.soulEnergyStorage = new SoulEnergyStorage(100);
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
		tag.put("soul_energy_storage", soulEnergyStorage.serializeNBT());
		tag.putString(VINCULATE, vinculate);
		super.saveAdditional(tag);
	}
	
	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		inv.deserializeNBT(nbt.getCompound("inventory"));
		soulEnergyStorage.deserializeNBT(nbt.getCompound("soul_energy_storage"));
		vinculate = nbt.getString(VINCULATE);
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		// Will get tag from #getUpdateTag
		return ClientboundBlockEntityDataPacket.create(this, be -> {
			CompoundTag nbt = new CompoundTag();
			//this.writeCustomNBT(nbttagcompound, true);
			nbt.putString("test", "Test Update packet");
			return nbt;
		});
	}
	
	@Override
	public void handleUpdateTag(CompoundTag tag) {
		super.handleUpdateTag(tag);
		LogUtils.getLogger().info("test: " + tag.getString("test"));
	}
	
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		super.onDataPacket(net, pkt);
		CompoundTag nonNullTag = pkt.getTag() != null ? pkt.getTag() : new CompoundTag();
		LogUtils.getLogger().info("test: " + nonNullTag.getString("test"));
	}
	
	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = new CompoundTag();
		tag.putString("test", "Test update tag");
		return tag;
	}
	
	@Override
	public CompoundTag serializeNBT() {
		return super.serializeNBT();
	}
	
	@Override
	public void deserializeNBT(CompoundTag nbt) {
		super.deserializeNBT(nbt);
		LogUtils.getLogger().info("Vinculate: " + nbt.getString(VINCULATE));
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
	public boolean stillValid(Player player) {
		if (this.isRemoved()) {
			return false;
		} else {
			return !(worldPosition.distSqr(player.blockPosition()) > 64.0D);
		}
	}
	
	@Override
	public void clearContent() {
		inv.clearContent();
	}

	@Override
	protected Component getDefaultName() {
		return CONTAINER_TITLE;
	}

	@Override
	public void drops() {
		Containers.dropContents(this.level, this.worldPosition, inv);
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory inv) {
		return new SpellExplorerContainer(id, inv, this);
	}

	@Override
	public void vinculationChanged(BlockState state, Level level, BlockPos pos) {
		String[] sData = serializeNBT().getString(VINCULATE).split(",");
		if(sData.length == 0) return;
		if(sData[0].equals("")) return;
		BlockPos bePos = new BlockPos(Integer.parseInt(sData[0]), Integer.parseInt(sData[1]),
				Integer.parseInt(sData[2]));
		BlockEntity be = level.getBlockEntity(bePos);
		if(be != null) {
			((VinculatorBlockEntity)be).vinculate = "";
		}
	}

	@Override
	public void receiveMessageFromClient(CompoundTag nbt) {
		LogUtils.getLogger().info("from Client");
	}

	@Override
	public void receiveMessageFromServer(CompoundTag nbt) {
		LogUtils.getLogger().info("from Server");
	}

	@Override
	public ISoulEnergyStorage getSoulEnergyStorage() {
		return soulEnergyStorage;
	}

}
