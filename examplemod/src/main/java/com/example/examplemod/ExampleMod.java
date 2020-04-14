package com.example.examplemod;

import com.example.examplemod.client.render.entity.WildBoarRenderer;
import com.example.examplemod.entity.WildBoarEntity;
import com.example.examplemod.init.ModBlocks;
import com.example.examplemod.init.ModEntityTypes;
import com.example.examplemod.init.ModItemGroups;
import com.example.examplemod.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("examplemod")
public class ExampleMod
{
    public static final String MODID = "examplemod";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public ExampleMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);

        LOGGER.debug("Registered TileEntity Renderers");
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.WILD_BOAR.get(), WildBoarRenderer::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void entityCreated(EntityEvent event) {
        Entity entity = event.getEntity();
        if (entity == null) {
            return;
        }

        if (entity.getEntityWorld().isRemote) {
            return;
        }
        if (entity instanceof WildBoarEntity) {
            if (!((event instanceof RenderNameplateEvent)
                    || (event instanceof LivingSpawnEvent)
                    || (event instanceof LivingEvent
            )
            )) {
                // EntityJoinWorldEvent
                if (event instanceof EntityJoinWorldEvent) {
                    EntityJoinWorldEvent ejwe = (EntityJoinWorldEvent) event;
                    LOGGER.info("Entity Joined World {}-{}", entity.getEntityId(), entity.getEntity().getName().getString());
                } else if (event instanceof EntityEvent.EnteringChunk) {
                    EntityEvent.EnteringChunk entityEvent = (EntityEvent.EnteringChunk) event;
                    LOGGER.info("Entity {}-{} EnteringChunk {}", entity.getEntityId(), entity.getEntity().getName().getString(),
                            entityEvent.getOldChunkX() + "/" + entityEvent.getOldChunkZ() + " => " + entityEvent.getNewChunkX() + "/" + entityEvent.getNewChunkZ()
                    );
                } else {
                    LOGGER.info("Entity Event {}-{} for event {}", entity.getEntityId(), entity.getEntity().getName().getString(), event);
                }
            }
        }
    }

    @SubscribeEvent
    public void entityDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity == null) {
            return;
        }

        if (entity.getEntityWorld().isRemote) {
            return;
        }


        LOGGER.info("EntityDeath {}-{} Killed by {} through {}",
                entity.getEntityId(), entity.getEntity().getName().getString(),
                event.getSource().getDamageType(),
                event.getSource().getTrueSource()
        );
    }

    @SubscribeEvent
    public void entityDamanged(LivingDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity.getEntityWorld().isRemote) {
            return;
        }

        if (entity instanceof WildBoarEntity) {
            //player.sendMessage(new StringTextComponent("Msg: Damage Entity " + entity.getName().getString()));
            Minecraft.getInstance().player.sendStatusMessage(
                    new StringTextComponent("Msg: Damage Entity " + entity.getName().getString()),
                    true
            );
        }

        LOGGER.info("EntityDamaged {}-{} Damanged by {} through {}",
                entity.getEntityId(), entity.getEntity().getName().getString(),
                event.getSource().getDamageType(),
                event.getSource().getTrueSource()
        );
    }


    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD, modid = ExampleMod.MODID)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }

        @SubscribeEvent
        public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
            final IForgeRegistry<Item> registry = event.getRegistry();
            // Automatically register BlockItems for all our Blocks
            ModBlocks.BLOCKS.getEntries().stream()
                    .map(RegistryObject::get)
                    // You can do extra filtering here if you don't want some blocks to have an BlockItem automatically registered for them
                    // .filter(block -> needsItemBlock(block))
                    // Register the BlockItem for the block
                    .forEach(block -> {
                        // Make the properties, and make it so that the item will be on our ItemGroup (CreativeTab)
                        final Item.Properties properties = new Item.Properties().group(ModItemGroups.MOD_ITEM_GROUP);
                        // Create the new BlockItem with the block and it's properties
                        final BlockItem blockItem = new BlockItem(block, properties);
                        // Set the new BlockItem's registry name to the block's registry name
                        blockItem.setRegistryName(block.getRegistryName());
                        // Register the BlockItem
                        registry.register(blockItem);
                    });
            LOGGER.debug("Registered BlockItems");
        }
    }

    @Mod.EventBusSubscriber(Dist.CLIENT)
    public static class DrawScreen {
        static int counter = 0;
        @SubscribeEvent
        public static void drawScreen(GuiScreenEvent.DrawScreenEvent event)
        {
            if (counter > 500) {

                LOGGER.info("Draw Screen {}", Minecraft.getInstance().currentScreen.getClass());
                ClientPlayerEntity player = Minecraft.getInstance().player;
                if (player != null) {
                    LOGGER.info("Draw Screen: Current Player {}", player);
                }

                counter = 0;
            }
            counter++;
        }

        static int counterPlayerTickEvent = 0;
        static int counterRenderTickEvent = 0;

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (counterPlayerTickEvent > 500) {
                counterPlayerTickEvent = 0;
                LOGGER.info("PlayerTickEvent {}", Minecraft.getInstance().currentScreen);
            }
            counterPlayerTickEvent++;
        }
        static List<String> textList = new ArrayList<String>();
        static {
            textList.add("HelloWorld");
        }

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.RenderTickEvent event) {
            if (counterRenderTickEvent > 500) {
                counterRenderTickEvent = 0;
                LOGGER.info("RenderTickEvent {}", Minecraft.getInstance().currentScreen);


            }

            // When there's no screen then we are in game
            if (Minecraft.getInstance().currentScreen == null) {
                GuiUtils.drawHoveringText(
                        textList,
                        200, 200, 200, 100, 100,
                        Minecraft.getInstance().fontRenderer
                );
            }
            counterRenderTickEvent++;
        }

        @SubscribeEvent
        public static void mouseClick(GuiScreenEvent.MouseInputEvent event) {
            LOGGER.info("Mouse Clicked");
        }
    }
}
