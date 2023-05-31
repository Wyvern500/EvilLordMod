package com.jg.evilord.entities.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jg.evilord.soul.link.SoulLink;
import com.jg.evilord.utils.EnergyUtils;
import com.jg.evilord.utils.Pos;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

public abstract class AbstractBinderBlockEntity<T extends BlockEntity> extends BlockEntity {

	protected String link;
	protected ContainerData data;
	protected SoulLink links;
	protected EnergyStorage energyStorage;
	private LazyOptional<EnergyStorage> energy;
	
	public static final String LINK = "link";
	
	public AbstractBinderBlockEntity(BlockEntityType<?> p_155228_, 
			BlockPos p_155229_, BlockState p_155230_, int capacity, 
			int inputSlots, int outputSlots) {
		super(p_155228_, p_155229_, p_155230_);
		energyStorage = new EnergyStorage(capacity);
		this.energy = LazyOptional.of(() -> this.energyStorage);
		links = new SoulLink(inputSlots, outputSlots);
		this.link = "";
		data = new ContainerData() {
			
			@Override
			public void set(int i, int v) {
				switch(i) {
				case 0:
					energyStorage.receiveEnergy(v, false);
					break;
				}
			}
			
			@Override
			public int getCount() {
				return 1;
			}
			
			@Override
			public int get(int i) {
				return energyStorage.getEnergyStored();
			}
		};
	}
	
	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		links.deserializeNBT(nbt.getCompound("links"));
		link = nbt.getString(LINK);
		EnergyUtils.deserializeFrom(energyStorage, nbt);
	}
	
	@Override
	protected void saveAdditional(CompoundTag tag) {
		tag.put("links", links.serializeNBT());
		tag.putString(LINK, link);
		EnergyUtils.serializeTo(energyStorage, tag);
		super.saveAdditional(tag);
	}

	@Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, 
    		final @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY)
            return energy.cast();

        return super.getCapability(cap, side);
    }
	
	public void linkWith(float x, float y, float z) {
		linkWith(new BlockPos(x, y, z));
	}
	
	public void linkWith(BlockPos pos) {
		/*if(link != null) {
			((AbstractBinderBlockEntity<?>)level.getBlockEntity(pos)).unbind();
		}
		link = String.valueOf(pos.getX()) + "," + String.valueOf(pos.getY()) + "," + 
				String.valueOf(pos.getZ());*/
		links.set(true, pos);
	}
	
	public void linkWith(AbstractBinderBlockEntity<?> be) {
		int remainingOutputSlots = links.getRemainingSlots(true);
		int remaingingInputSlots = be.getLinks().getRemainingSlots(false);
		LogUtils.getLogger().info(getBlockState().getBlock().getRegistryName()
				.toString() + remainingOutputSlots + " free output slosts " + 
				be.getBlockState().getBlock().getRegistryName()
				.toString() + remaingingInputSlots + " free input slots");
		if(remainingOutputSlots > 0 && 
				remaingingInputSlots > 0) {
			links.set(true, be.getBlockPos());
			be.getLinks().set(false, getBlockPos());
			linkChanged(be.getBlockState(), level, be.getBlockPos());
			be.linkChanged(getBlockState(), level, worldPosition);
		}
	}

	public void unbind(AbstractBinderBlockEntity<?> be) {
		//link = null;
		links.unbind(true, be.getBlockPos());
		be.getLinks().unbind(false, worldPosition);
		linkChanged(be.getBlockState(), level, be.getBlockPos());
		be.linkChanged(getBlockState(), level, worldPosition);
	}
	
	public void unbind() {
		for(Pos pos : links.getOutputs()) {
			if(pos.pos.length != 0) {
				BlockPos other = toBlockPos(pos.pos);
				if(level.isLoaded(other)) {
					BlockEntity be = level.getBlockEntity(other);
					if(be != null) {
						unbind(((AbstractBinderBlockEntity<?>)be));
						LogUtils.getLogger().info("outputs");
					}
				}
			}
		}
		for(Pos pos : links.getInputs()) {
			if(pos.pos.length != 0) {
				BlockPos other = toBlockPos(pos.pos);
				if(level.isLoaded(other)) {
					BlockEntity be = level.getBlockEntity(other);
					if(be != null) {
						((AbstractBinderBlockEntity<?>)be).unbind(this);
					}
				}
			}
		}
	}
	
	public BlockPos toBlockPos(int[] pos) {
		return new BlockPos(pos[0], pos[1], pos[2]);
	}
	
	public void notifyOutputs() {
		for(Pos pos : links.getOutputs()) {
			if(!pos.isEmpty()) {
				BlockPos bpos = pos.toBlockPos();
				if(level.isLoaded(bpos)) {
					((AbstractBinderBlockEntity<?>)level.getBlockEntity(bpos))
						.onInputChange(energyStorage, this);
				}
			}
		}
	}
	
	public void onInputChange(EnergyStorage storage, AbstractBinderBlockEntity<?> be) {
		
	}
	
	public abstract void onEnergyChanged(int energy, boolean extracted);
	
	public abstract void linkChanged(BlockState pState, Level pLevel, BlockPos pPos);
	
	public abstract void drops();
	
	public abstract void receiveMessageFromClient(CompoundTag nbt);
	
	public abstract void receiveMessageFromServer(CompoundTag nbt);
	
	public ContainerData getData() {
		return data;
	}
	
	public SoulLink getLinks() {
		return links;
	}
}
