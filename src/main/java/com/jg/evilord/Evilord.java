package com.jg.evilord;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jg.evilord.client.handler.ClientEventHandler;
import com.jg.evilord.client.render.spellIcon.SpellIconRenderer;
import com.jg.evilord.config.Config;
import com.jg.evilord.event.common.RegisterSpellEvent;
import com.jg.evilord.network.client.DeathEntityMessage;
import com.jg.evilord.network.common.SyncBlockEntityMessage;
import com.jg.evilord.network.server.DeathEntityResolutionMessage;
import com.jg.evilord.network.server.ProcessConnectionManipulatorWandMessage;
import com.jg.evilord.network.server.RunSpellMessage;
import com.jg.evilord.network.server.SpawnSkeletonMessage;
import com.jg.evilord.proxy.ClientProxy;
import com.jg.evilord.proxy.IProxy;
import com.jg.evilord.proxy.ServerProxy;
import com.jg.evilord.registries.BlockEntityRegistries;
import com.jg.evilord.registries.BlockRegistries;
import com.jg.evilord.registries.ContainerRegistries;
import com.jg.evilord.registries.EntityRegistries;
import com.jg.evilord.registries.ItemRegistries;
import com.jg.evilord.spell.Spell;
import com.jg.evilord.spell.Spell.FireRate;
import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Evilord.MODID)
public class Evilord {
	public static final String MODID = "evilord";
	public static IProxy proxy;
	public static SimpleChannel channel;
    private static int packetsRegistered = 0;
    public static final Logger LOGGER = LogManager.getLogger();
	public static final String PROTOCOL_VERSION = "1";
	private static final CreativeModeTab tab = new CreativeModeTab("Evilord") {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Items.GUNPOWDER);
		}
	};

    public Evilord() {
    	IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
	
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.server_config);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.client_config);
	
		// Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    	
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerEntityAttributes);
        
        Evilord.proxy.registerModEventListeners(bus);
        
        /*
        //Register mod stuff (Items, Entities, Containers, etc.)
        SoundRegistries.SOUNDS.register(bus);
        ItemRegistries.ITEMS.register(bus);
        EntityRegistries.ENTITIES.register(bus);
        ContainerRegistries.CONTAINERS.register(bus);
        BlockRegistries.BLOCKS.register(bus);
        */
        EntityRegistries.ENTITIES.register(bus);
        BlockEntityRegistries.BLOCKENTITIES.register(bus);
        ItemRegistries.ITEMS.register(bus);
        ContainerRegistries.CONTAINERS.register(bus);
        BlockRegistries.BLOCKS.register(bus);
        
        bus = MinecraftForge.EVENT_BUS;
        bus.addListener(this::registerSpells);
        Evilord.proxy.registerForgeEventListeners(bus);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			MinecraftForge.EVENT_BUS.start();
			MinecraftForge.EVENT_BUS.post(new RegisterSpellEvent());
			
			Evilord.channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main"), 
	        		() -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
		
			channel.registerMessage(packetsRegistered++, SpawnSkeletonMessage.class, 
					SpawnSkeletonMessage::encode, SpawnSkeletonMessage::decode, 
					SpawnSkeletonMessage::handle);
			channel.registerMessage(packetsRegistered++, DeathEntityMessage.class, 
					DeathEntityMessage::encode, DeathEntityMessage::decode, 
					DeathEntityMessage::handle);
			channel.registerMessage(packetsRegistered++, DeathEntityResolutionMessage.class, 
					DeathEntityResolutionMessage::encode, DeathEntityResolutionMessage::decode, 
					DeathEntityResolutionMessage::handle);
			channel.registerMessage(packetsRegistered++, RunSpellMessage.class, 
					RunSpellMessage::encode, RunSpellMessage::decode, 
					RunSpellMessage::handle);
			channel.registerMessage(packetsRegistered++, ProcessConnectionManipulatorWandMessage.class, 
					ProcessConnectionManipulatorWandMessage::encode, ProcessConnectionManipulatorWandMessage::decode, 
					ProcessConnectionManipulatorWandMessage::handle);
			channel.registerMessage(packetsRegistered++, SyncBlockEntityMessage.class, 
					SyncBlockEntityMessage::encode, SyncBlockEntityMessage::decode, 
					SyncBlockEntityMessage::handle);
		});
	}
	
	private void doClientStuff(final FMLClientSetupEvent event) {
		ClientEventHandler.setup();
	}
	
	private void registerEntityAttributes(EntityAttributeCreationEvent e) {
		e.put(EntityRegistries.BASICSKELETONMINION.get(), 
				Monster.createMonsterAttributes().build());
		e.put(EntityRegistries.DeadMass.get(), 
				Monster.createMonsterAttributes().build());
	}
	
	private void registerSpells(RegisterSpellEvent e) {
		e.push(Evilord.MODID);
		e.registerSpell("test", new Spell("Test", FireRate.SEMI, 
				new SpellIconRenderer("evilord:textures/spell/spell_atlas.png", 0, 0), 
				(stack, player) -> {
					
				}, 
				(stack, player) -> {
					return true;
				}));
		for(int i = 0; i < 30; i++) {
			if(i == 0) {
				e.registerSpell("fire", new Spell("Fire", FireRate.SEMI, 
						new SpellIconRenderer("evilord:textures/spell/spell_atlas.png", 16, 0)));
			} else {
				e.registerSpell("test" + i, new Spell("Test" + i, FireRate.SEMI, 
						new SpellIconRenderer("evilord:textures/spell/spell_atlas.png", 0, 0)));
			}
			
		}
		e.pop();
		
		LogUtils.getLogger().info("Registering spells");
	}
	
	public static CreativeModeTab getTab() {
		return tab;
	}
	
    /*
    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            // Register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }*/
}
