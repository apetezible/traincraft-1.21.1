package com.eternalblueflame.traincraft.client.effects;

import com.eternalblueflame.traincraft.entity.EntityLocomotive;
import com.eternalblueflame.traincraft.locomotive.effects.ParticleEmissionDefinition;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;

/**
 * Client-side equivalent of legacy Traincraft's renderer-driven smoke system.
 * Particle positions are model-relative and rotate with the locomotive.
 */
public final class LocomotiveParticleController {
    private LocomotiveParticleController() {
    }

    public static void emit(EntityLocomotive locomotive) {
        if (!locomotive.isEngineOn() || !locomotive.isFuelled()
                || Math.abs(locomotive.getXRot()) > 30.0F) {
            return;
        }

        Minecraft client = Minecraft.getInstance();
        if (client.level == null) {
            return;
        }

        RandomSource random = client.level.random;
        double speed = locomotive.getDeltaMovement().horizontalDistance();
        for (ParticleEmissionDefinition emission : locomotive.getDefinition().getParticleProfile().emissions()) {
            int density = emission.density();
            if (random.nextInt(10 * density) >= density * 4 + speed * 5.0D) {
                continue;
            }

            for (int count = 0; count < density; count++) {
                double[] position = rotate(emission.x(), emission.y(), emission.z(),
                        locomotive.getXRot(), locomotive.getYRot());
                if (emission.type() == ParticleEmissionDefinition.Type.LARGE_SMOKE) {
                    client.level.addParticle(ParticleTypes.LARGE_SMOKE,
                            locomotive.getX() + position[0],
                            locomotive.getY() + position[1],
                            locomotive.getZ() + position[2],
                            0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    private static double[] rotate(double x, double y, double z, float pitchDegrees, float yawDegrees) {
        double pitch = Math.toRadians(pitchDegrees);
        double pitchX = y * Math.sin(pitch) + x * Math.cos(pitch);
        double pitchY = y * Math.cos(pitch) - x * Math.sin(pitch);

        double yaw = Math.toRadians(yawDegrees);
        return new double[]{
                pitchX * Math.cos(yaw) - z * Math.sin(yaw),
                pitchY,
                pitchX * Math.sin(yaw) + z * Math.cos(yaw)
        };
    }
}
