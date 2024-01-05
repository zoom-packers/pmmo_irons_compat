package net.silvertide.pmmo_spellbooks_compat.client.events;

import harmonised.pmmo.core.CoreUtils;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.spell.SpellData;
import io.redspace.ironsspellbooks.item.Scroll;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silvertide.pmmo_spellbooks_compat.PMMOSpellBooksCompat;
import net.silvertide.pmmo_spellbooks_compat.config.codecs.SpellRequirement;
import net.silvertide.pmmo_spellbooks_compat.network.SpellRequirementSyncPacket;
import net.silvertide.pmmo_spellbooks_compat.util.CompatUtil;

import java.util.Map;

@Mod.EventBusSubscriber(modid= PMMOSpellBooksCompat.MOD_ID, bus= Mod.EventBusSubscriber.Bus.FORGE, value= Dist.CLIENT)
public class TooltipHandler {

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        Player player = event.getEntity();
        if(player != null) {
            ItemStack stack = event.getItemStack();
            if(stack.getItem() instanceof Scroll) {
                Map<ResourceLocation, SpellRequirement> spellReqMap = SpellRequirementSyncPacket.SYNCED_DATA;
                if(spellReqMap.size() > 0) {
                    var spellData = SpellData.getSpellData(stack);
                    AbstractSpell spellCast = spellData.getSpell();
                    ResourceLocation spellResourceLocation = CompatUtil.getCompatResourceLocation(spellCast.getSpellId());
                    if(spellResourceLocation != null) {
                        SpellRequirement spellReq = spellReqMap.get(spellResourceLocation);
                        if(spellReq != null) {
                            Map<String, Integer> requirementMap = spellReq.getRequirementMap(spellData.getLevel());
                            if(requirementMap != null && !requirementMap.isEmpty()){
                                addCastRequirementTooltip(event, requirementMap);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void addCastRequirementTooltip(ItemTooltipEvent event, Map<String, Integer> reqs) {
        event.getToolTip().add(Component.literal("To Cast"));
        for (Map.Entry<String, Integer> req : reqs.entrySet()) {
            event.getToolTip().add(Component.translatable("pmmo."+req.getKey()).append(Component.literal(" "+ req.getValue())).setStyle(CoreUtils.getSkillStyle(req.getKey())));
        }
    }
}
