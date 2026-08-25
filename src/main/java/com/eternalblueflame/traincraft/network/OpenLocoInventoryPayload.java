package com.eternalblueflame.traincraft.network;

import com.eternalblueflame.traincraft.Traincraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record OpenLocoInventoryPayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenLocoInventoryPayload> TYPE =
            new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath(Traincraft.MOD_ID, "open_loco_inventory"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenLocoInventoryPayload> STREAM_CODEC =
            StreamCodec.unit(new OpenLocoInventoryPayload());

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}