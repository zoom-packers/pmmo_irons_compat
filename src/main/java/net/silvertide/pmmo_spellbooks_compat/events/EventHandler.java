package net.silvertide.pmmo_spellbooks_compat.events;

import io.redspace.ironsspellbooks.api.events.SpellCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellHealEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silvertide.pmmo_spellbooks_compat.PMMOSpellBooksCompat;
import net.silvertide.pmmo_spellbooks_compat.config.codecs.SpellRequirement;
import net.silvertide.pmmo_spellbooks_compat.config.codecs.SpellRequirements;
import net.silvertide.pmmo_spellbooks_compat.util.CompatUtil;
import net.silvertide.pmmo_spellbooks_compat.util.SpellCastResult;

import java.util.Map;

@Mod.EventBusSubscriber(modid= PMMOSpellBooksCompat.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    @SubscribeEvent
    public static void playerHealedEvent(SpellHealEvent healEvent) {
        LivingEntity targetEntity = healEvent.getTargetEntity();
        if(targetEntity == null) return;

        if(targetEntity.getHealth() == targetEntity.getMaxHealth()) {
            healEvent.getEntity().sendSystemMessage(Component.literal(targetEntity.getName() + " is at max health. What a waste."));
        } else {
            healEvent.getEntity().sendSystemMessage(Component.literal("You healed " + targetEntity.getName() + " for " + healEvent.getHealAmount() + " with a level "));
        }
    }

    @SubscribeEvent
    public static void onSpellCast(SpellCastEvent spellCastEvent) {
        if (spellCastEvent.isCanceled()) return;
        Map<ResourceLocation, SpellRequirement> spellReqMap = SpellRequirements.DATA_LOADER.getData();
        if(!spellReqMap.isEmpty()) {
            ResourceLocation spellResourceLocation = CompatUtil.getCompatResourceLocation(spellCastEvent.getSpellId());
            if(spellResourceLocation != null) {
                SpellCastResult castResult = CompatUtil.canCastSpell(spellCastEvent, spellReqMap.get(spellResourceLocation));
                if(!castResult.wasSuccessful()) {
                    spellCastEvent.setCanceled(true);
                    ServerPlayer serverPlayer = (ServerPlayer) spellCastEvent.getEntity();
                    serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.literal("You must be level " + castResult.errorMessage() + " to cast this.").withStyle(ChatFormatting.RED)));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event)
    {
        event.addListener(SpellRequirements.DATA_LOADER);
    }
}
