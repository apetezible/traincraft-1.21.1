package com.eternalblueflame.traincraft.item;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;

public class LocomotiveItem extends Item {
    private final EntityType<? extends AbstractMinecart> entityType;

    public LocomotiveItem(Properties properties, EntityType<? extends AbstractMinecart> entityType) {
        super(properties);
        this.entityType = entityType;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos railPos = context.getClickedPos();
        BlockState railState = level.getBlockState(railPos);
        if (!railState.is(BlockTags.RAILS)) {
            return InteractionResult.FAIL;
        }

        ItemStack stack = context.getItemInHand();
        if (!level.isClientSide()) {
            double yOffset = 0.0625D;
            if (railState.getBlock() instanceof BaseRailBlock railBlock
                    && railState.getValue(railBlock.getShapeProperty()).isAscending()) {
                yOffset += 0.5D;
            }

            AbstractMinecart locomotive = entityType.create(level);
            if (locomotive == null) {
                return InteractionResult.FAIL;
            }

            locomotive.setPos(railPos.getX() + 0.5D, railPos.getY() + yOffset, railPos.getZ() + 0.5D);
            level.addFreshEntity(locomotive);
        }

        stack.shrink(1);
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
