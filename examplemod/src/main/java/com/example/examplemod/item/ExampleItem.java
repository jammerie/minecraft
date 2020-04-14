package com.example.examplemod.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExampleItem extends Item {
    private static final Logger LOGGER = LogManager.getLogger();

    public ExampleItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        LOGGER.info("Clicked by {}", player.getName().getString());
        return true;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        LOGGER.info("onUsingTick by {}", player.getName().getString());
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, PlayerEntity player) {
        LOGGER.info("onDroppedByPlayer by {}", player.getName().getString());
        return true;
    }
}
