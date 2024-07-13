package net.silvertide.pmmo_spellbooks_compat.events;

import io.redspace.ironsspellbooks.api.events.InscribeSpellEvent;
import io.redspace.ironsspellbooks.api.events.SpellDamageEvent;
import io.redspace.ironsspellbooks.api.events.SpellHealEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silvertide.pmmo_spellbooks_compat.PMMOSpellBooksCompat;
import net.silvertide.pmmo_spellbooks_compat.config.Config;
import net.silvertide.pmmo_spellbooks_compat.config.codecs.SpellRequirement;
import net.silvertide.pmmo_spellbooks_compat.config.codecs.SpellRequirements;
import net.silvertide.pmmo_spellbooks_compat.util.CompatUtil;
import net.silvertide.pmmo_spellbooks_compat.util.SpellEventResult;

import java.util.Map;

@Mod.EventBusSubscriber(modid = PMMOSpellBooksCompat.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    @SubscribeEvent
    public static void entityHealedEvent(SpellHealEvent healEvent) {
        LivingEntity targetEntity = healEvent.getTargetEntity();
        // Check if the caster is a player.
        var caster = healEvent.getEntity();
        if (!(caster instanceof Player player)) return;
        if(targetEntity == null) return;

        // Only trigger xp for healing another entity.
        if(!player.level().isClientSide()) {
            float amountHealed = CompatUtil.getAmountHealed(targetEntity, healEvent.getHealAmount());
            if(amountHealed > 0) {
                if(targetEntity.getUUID() != player.getUUID()){
                    CompatUtil.addXp(Config.HEAL_OTHER_SKILL.get(), player, Math.round(amountHealed*Config.HEAL_OTHER_XP_REWARD.get()));
                } else {
                    CompatUtil.addXp(Config.HEAL_SELF_SKILL.get(), player, Math.round(amountHealed*Config.HEAL_SELF_XP_REWARD.get()));
                }
            }
        }
    }

    @SubscribeEvent
    public static void entityDamagedEvent(SpellDamageEvent damageEvent) {
        LivingEntity targetEntity = damageEvent.getEntity();
        var caster = damageEvent.getSpellDamageSource().get().getEntity();
        if (!(caster instanceof Player player)) return;
        if(targetEntity == null || player == null) return;

        // Only trigger xp for damaging another entity.
        if(!player.level().isClientSide()) {
            float amountDealt = damageEvent.getAmount();
            if(amountDealt > 0) {
                if(targetEntity.getUUID() != player.getUUID()){
                    CompatUtil.addXp(Config.DAMAGE_OTHER_SKILL.get(), player, Math.round(amountDealt*Config.DAMAGE_OTHER_XP_REWARD.get()));
                } else {
                    CompatUtil.addXp(Config.DAMAGE_SELF_SKILL.get(), player, Math.round(amountDealt*Config.DAMAGE_SELF_XP_REWARD.get()));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onSpellCast(SpellPreCastEvent spellPreCastEvent) {
        if (spellPreCastEvent.isCanceled()) return;

        Map<ResourceLocation, SpellRequirement> spellReqMap = SpellRequirements.DATA_LOADER.getData();
        if(!spellReqMap.isEmpty()) {
            ResourceLocation spellResourceLocation = CompatUtil.getCompatResourceLocation(spellPreCastEvent.getSpellId());
            if(spellResourceLocation != null) {
                SpellEventResult castResult = CompatUtil.canCastSpell(spellPreCastEvent, spellReqMap.get(spellResourceLocation));
                if(!castResult.wasSuccessful()) {
                    spellPreCastEvent.setCanceled(true);
                    ServerPlayer serverPlayer = (ServerPlayer) spellPreCastEvent.getEntity();
                    serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.literal("You must be level " + castResult.errorMessage() + " to cast this.").withStyle(ChatFormatting.RED)));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onSpellInscribe(InscribeSpellEvent inscribeEvent) {
        if (inscribeEvent.isCanceled()) return;

        Map<ResourceLocation, SpellRequirement> spellReqMap = SpellRequirements.DATA_LOADER.getData();
        if(!spellReqMap.isEmpty()) {
            AbstractSpell spell = inscribeEvent.getSpellData().getSpell();
            ResourceLocation spellResourceLocation = CompatUtil.getCompatResourceLocation(spell.getSpellId());
            if(spellResourceLocation != null) {
                SpellEventResult inscribeResult = CompatUtil.canInscribeSpell(inscribeEvent, spellReqMap.get(spellResourceLocation));
                if(!inscribeResult.wasSuccessful()) {
                    inscribeEvent.setCanceled(true);
                    ServerPlayer serverPlayer = (ServerPlayer) inscribeEvent.getEntity();
                    serverPlayer.sendSystemMessage(Component.literal("You must be level " + inscribeResult.errorMessage() + " to inscribe level " + inscribeEvent.getSpellData().getLevel() + " " + spell.getSpellName() + ".").withStyle(ChatFormatting.RED));
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
