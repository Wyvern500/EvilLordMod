package com.jg.evilord.registries;

import com.jg.evilord.Evilord;
import com.jg.evilord.entities.block.ArtifactCrafterBlockEntity;
import com.jg.evilord.entities.block.SpellExplorerBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistries {
	public static final DeferredRegister<BlockEntityType<?>> BLOCKENTITIES = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITIES, Evilord.MODID);
	
	public static final RegistryObject<BlockEntityType<SpellExplorerBlockEntity>> spellExplorer = 
			BLOCKENTITIES.register("spell_explorer_block_entity", () -> 
			BlockEntityType.Builder.of(SpellExplorerBlockEntity::new, BlockRegistries
					.spellExplorer.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<ArtifactCrafterBlockEntity>> artifactCrafter = 
			BLOCKENTITIES.register("artifact_crafter_block_entity", () -> 
			BlockEntityType.Builder.of(ArtifactCrafterBlockEntity::new, BlockRegistries
					.artifactCrafter.get()).build(null));
}
