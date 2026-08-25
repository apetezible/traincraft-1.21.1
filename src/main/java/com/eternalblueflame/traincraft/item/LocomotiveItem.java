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
import net.minecraft.world.level.block.state.properties.RailShape;

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
            locomotive.setYRot(getRailYaw(railState, context));
            level.addFreshEntity(locomotive);
        }

        stack.shrink(1);
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    private static float getRailYaw(BlockState railState, UseOnContext context) {
        if (!(railState.getBlock() instanceof BaseRailBlock railBlock)) {
            return 0.0F;
        }

        RailShape shape = railState.getValue(railBlock.getShapeProperty());
        float railYaw = switch (shape) {
            case NORTH_SOUTH, ASCENDING_NORTH, ASCENDING_SOUTH -> 90.0F;
            case SOUTH_EAST -> 45.0F;
            case SOUTH_WEST -> 135.0F;
            case NORTH_WEST -> 225.0F;
            case NORTH_EAST -> 315.0F;
            default -> 0.0F;
        };

        if (context.getPlayer() == null) {
            return railYaw;
        }

        double targetX = context.getClickedPos().getX() + 0.5D - context.getPlayer().getX();
        double targetZ = context.getClickedPos().getZ() + 0.5D - context.getPlayer().getZ();
        double yawRadians = Math.toRadians(railYaw);
        double forwardX = Math.cos(yawRadians);
        double forwardZ = -Math.sin(yawRadians);
        double projection = targetX * forwardX + targetZ * forwardZ;

        return projection >= 0.0D ? railYaw : railYaw + 180.0F;
    }
}
