package net.silvertide.pmmo_spellbooks_compat.network;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.silvertide.pmmo_spellbooks_compat.config.codecs.SpellRequirement;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SpellRequirementSyncPacket {
    private static final Codec<Map<ResourceLocation, SpellRequirement>> MAPPER =
            Codec.unboundedMap(ResourceLocation.CODEC, SpellRequirement.CODEC);
    public static Map<ResourceLocation, SpellRequirement> SYNCED_DATA = new HashMap<>();

    private final Map<ResourceLocation, SpellRequirement> map;

    public SpellRequirementSyncPacket(Map<ResourceLocation, SpellRequirement> map)
    {
        this.map = map;
    }

    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeNbt((CompoundTag)(MAPPER.encodeStart(NbtOps.INSTANCE, this.map).result().orElse(new CompoundTag())));
    }

    public static SpellRequirementSyncPacket decode(FriendlyByteBuf buffer)
    {
        return new SpellRequirementSyncPacket(MAPPER.parse(NbtOps.INSTANCE, buffer.readNbt()).result().orElse(new HashMap<>()));
    }

    public void onPacketReceived(Supplier<NetworkEvent.Context> contextGetter)
    {
        NetworkEvent.Context context = contextGetter.get();
        context.enqueueWork(this::handlePacketOnMainThread);
        context.setPacketHandled(true);
    }

    private void handlePacketOnMainThread()
    {
        SYNCED_DATA = this.map;
    }
}
