package com.jg.evilord.registries;

import com.jg.evilord.Evilord;
import com.jg.evilord.block.blocks.SoulManipulatorBlock;
import com.jg.evilord.block.blocks.SpellExplorerBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistries {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
			ForgeRegistries.BLOCKS, Evilord.MODID);

	public static RegistryObject<Block> soulManipulator = BLOCKS.register("soul_manipulator",
			() -> new SoulManipulatorBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL).strength(2.0F, 3.0F)
					.strength(2).sound(SoundType.METAL)));
	
	public static RegistryObject<Block> spellExplorer = BLOCKS.register("spell_explorer",
			() -> new SpellExplorerBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL).strength(2.0F, 3.0F)
					.strength(2).sound(SoundType.METAL)));
	
}
