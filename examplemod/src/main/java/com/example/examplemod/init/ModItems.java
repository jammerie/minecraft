package com.example.examplemod.init;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.item.ExampleItem;
import com.example.examplemod.item.ModdedSpawnEggItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, ExampleMod.MODID);

    public static final RegistryObject<Item> EXAMPLE_CRYSTAL = ITEMS.register(
            "example_crystal",
            () -> new ExampleItem(
                    new Item.Properties().group(ModItemGroups.MOD_ITEM_GROUP)
            )
    );

    public static final RegistryObject<ModdedSpawnEggItem> WILD_BOAR_SPAWN_EGG = ITEMS.register(
            "wild_boar_spawn_egg",
            () -> new ModdedSpawnEggItem(
                    ModEntityTypes.WILD_BOAR,
                    0xF0A5A2,
                    0xA9672B,
                    new Item.Properties().group(ModItemGroups.MOD_ITEM_GROUP
                    )
            )
    );

}
