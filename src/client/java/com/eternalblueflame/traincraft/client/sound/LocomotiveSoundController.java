package com.eternalblueflame.traincraft.client.sound;

import com.eternalblueflame.traincraft.entity.EntityLocomotive;
import com.eternalblueflame.traincraft.locomotive.sound.LocomotiveSoundProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Maintains one positional idle or running sound for every audible locomotive. */
public final class LocomotiveSoundController {
    private final Map<Integer, EngineSoundInstance> activeSounds = new HashMap<>();

    public void tick(Minecraft client) {
        if (client.level == null) {
            stopAll();
            return;
        }

        Set<Integer> present = new HashSet<>();
        for (var entity : client.level.entitiesForRendering()) {
            if (!(entity instanceof EntityLocomotive locomotive)) {
                continue;
            }
            present.add(locomotive.getId());
            update(client, locomotive);
        }

        activeSounds.entrySet().removeIf(entry -> {
            if (present.contains(entry.getKey())) {
                return false;
            }
            entry.getValue().requestStop();
            return true;
        });
    }

    private void update(Minecraft client, EntityLocomotive locomotive) {
        boolean shouldPlay = locomotive.isAlive() && locomotive.isEngineOn() && locomotive.isFuelled();
        EngineSoundInstance current = activeSounds.get(locomotive.getId());
        if (!shouldPlay) {
            if (current != null) {
                current.requestStop();
                activeSounds.remove(locomotive.getId());
            }
            return;
        }

        boolean running = locomotive.getDeltaMovement().horizontalDistance() > 0.01D;
        if (current != null && current.running == running) {
            return;
        }
        if (current != null) {
            current.requestStop();
        }

        EngineSoundInstance next = new EngineSoundInstance(locomotive, locomotive.getDefinition().getSoundProfile(), running);
        activeSounds.put(locomotive.getId(), next);
        client.getSoundManager().play(next);
    }

    private void stopAll() {
        activeSounds.values().forEach(EngineSoundInstance::requestStop);
        activeSounds.clear();
    }

    private static final class EngineSoundInstance extends AbstractTickableSoundInstance {
        private final EntityLocomotive locomotive;
        private final boolean running;

        private EngineSoundInstance(EntityLocomotive locomotive, LocomotiveSoundProfile profile, boolean running) {
            super(SoundEvent.createVariableRangeEvent(running ? profile.running() : profile.idle()),
                    SoundSource.NEUTRAL, RandomSource.create());
            this.locomotive = locomotive;
            this.running = running;
            this.looping = true;
            this.delay = 0;
            this.volume = running ? profile.runningVolume() : profile.idleVolume();
            this.pitch = running ? profile.runningPitch() : profile.idlePitch();
            updatePosition();
        }

        @Override
        public void tick() {
            if (!locomotive.isAlive() || !locomotive.isEngineOn() || !locomotive.isFuelled()) {
                stop();
                return;
            }
            updatePosition();
        }

        private void updatePosition() {
            this.x = locomotive.getX();
            this.y = locomotive.getY();
            this.z = locomotive.getZ();
        }

        /** Allows the enclosing controller to stop this protected sound instance. */
        private void requestStop() {
            stop();
        }
    }
}
