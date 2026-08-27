package com.eternalblueflame.traincraft.entity;

import com.eternalblueflame.traincraft.TraincraftItems;
import com.eternalblueflame.traincraft.wagon.WagonDefinition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.Item;

public class EntityPassengerCar1 extends EntityWagon {
    private static final WagonDefinition DEFINITION = new WagonDefinition(
            "passenger_small_black",
            1000.0F,
            0
    );

    public EntityPassengerCar1(EntityType<? extends EntityPassengerCar1> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public WagonDefinition getDefinition() {
        return DEFINITION;
    }

    @Override
    protected Item getDropItem() {
        return TraincraftItems.PASSENGER_CAR_1;
    }

    @Override
    public Type getMinecartType() {
        return Type.RIDEABLE;
    }

    @Override
    public Vec3 getPassengerRidingPosition(Entity passenger) {
        Vec3 seatOffset = new Vec3(0.0D, 0.5D, 0.0D);
        double yaw = Math.toRadians(getYRot());
        double offsetX = seatOffset.x * Math.cos(yaw) - seatOffset.z * Math.sin(yaw);
        double offsetZ = seatOffset.x * Math.sin(yaw) + seatOffset.z * Math.cos(yaw);
        return position().add(offsetX, seatOffset.y, offsetZ);
    }
}
