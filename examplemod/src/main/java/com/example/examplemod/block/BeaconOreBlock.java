package com.example.examplemod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IProperty;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Collection;

public class BeaconOreBlock extends Block {

    private static final Logger LOGGER = LogManager.getLogger();

    public BeaconOreBlock(Properties properties) {
        super(properties);
    }

    static int counte = 0;
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        LOGGER.info("onBlockActivated {}", player.getName().getString());
//        counte++;
//        counte = counte % 4;
//        switch (counte) {
//            case 0 : return ActionResultType.CONSUME;
//            case 1 : return ActionResultType.PASS;
//            case 2 : return ActionResultType.FAIL;
//            case 3 : return ActionResultType.SUCCESS;
//        }

        BlockPos pos0 = new BlockPos(pos.getX()+1, (pos.getY()+1) , pos.getZ());

        worldIn.setBlockState(pos0, state, 3);
        worldIn.removeBlock(pos, false);

        return ActionResultType.SUCCESS;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        LOGGER.info("Placed in the world by {}", placer.getName().getString());
        //worldIn.setDayTime(worldIn.getDayTime() + 1000);
        Collection<IProperty<?>> properties = state.getProperties();
        LOGGER.info("Prop Size {}", properties.size());
        properties.forEach(prop -> {
            LOGGER.info("Prop {}", prop);
        });
//        new Thread (
//                () -> {
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    worldIn.removeBlock(pos, false);
//                }).start();
    }

    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        LOGGER.info("Clicked in the world by {}", player.getName().getString());
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        LOGGER.info("onBlockHarvested by {}", player.getName().getString());
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        LOGGER.info("onEntityWalk by {}", entityIn.getName().getString());
    }


    @Override
    public void onLanded(IBlockReader worldIn, Entity entityIn) {
        LOGGER.info("onLanded by {}", entityIn.getName().getString());
    }
}
