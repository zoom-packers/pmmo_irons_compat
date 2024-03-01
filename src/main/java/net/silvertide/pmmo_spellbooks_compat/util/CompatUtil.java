package net.silvertide.pmmo_spellbooks_compat.util;

import harmonised.pmmo.api.APIUtils;
import io.redspace.ironsspellbooks.api.events.InscribeSpellEvent;
import io.redspace.ironsspellbooks.api.events.SpellCastEvent;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.silvertide.pmmo_spellbooks_compat.PMMOSpellBooksCompat;
import net.silvertide.pmmo_spellbooks_compat.config.Config;
import net.silvertide.pmmo_spellbooks_compat.config.codecs.SpellRequirement;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class CompatUtil {
    @Nullable
    public static ResourceLocation getCompatResourceLocation(String spellId){
        int colonIndex = spellId.indexOf(':');
        if(colonIndex != -1) {
            return new ResourceLocation(PMMOSpellBooksCompat.MOD_ID, spellId.substring(colonIndex+1));
        } else {
            return null;
        }
    }
    @Nullable
    public static String stringifyCastSource(CastSource castSource){
        return switch(castSource) {
            case SWORD -> "sword";
            case SCROLL -> "scroll";
            case SPELLBOOK -> "spellbook";
            default -> null;
        };
    }

    public static SpellEventResult canCastSpell(SpellCastEvent spellCastEvent, SpellRequirement spellRequirement) {
        List<String> sources = spellRequirement.sources();
        String sourceString = stringifyCastSource(spellCastEvent.getCastSource());
        if(sources.size() > 0 && sourceString != null && sources.contains(sourceString)){
            Map<String, Integer> requirementMap = spellRequirement.getRequirementMap(spellCastEvent.getSpellLevel());
            if(requirementMap != null) {
                for(String skill : requirementMap.keySet()) {
                    int requiredLevel = requirementMap.get(skill);
                    if(requiredLevel > APIUtils.getLevel(skill, spellCastEvent.getEntity())) {
                        return new SpellEventResult(false, requiredLevel + " " + skill);
                    }
                }
            }
        }

        return new SpellEventResult(true, "");
    }

    public static SpellEventResult canInscribeSpell(InscribeSpellEvent inscribeEvent, SpellRequirement spellRequirement) {
        if(spellRequirement.sources().contains("inscribe")) {
            Map<String, Integer> requirementMap = spellRequirement.getRequirementMap(inscribeEvent.getSpellData().getLevel());
            if(requirementMap != null) {
                for(String skill : requirementMap.keySet()) {
                    int requiredLevel = requirementMap.get(skill);
                    if(requiredLevel > APIUtils.getLevel(skill, inscribeEvent.getEntity())) {
                        return new SpellEventResult(false, requiredLevel + " " + skill);
                    }
                }
            }
        }

        return new SpellEventResult(true, "");
    }


    public static int getHealXPReward(LivingEntity targetEntity, float healAmount) {
        float missingLife = targetEntity.getMaxHealth() - targetEntity.getHealth();
        if(missingLife > 0){
            float xpReward = Math.min(healAmount, missingLife) * Config.HEAL_XP_REWARD.get();
            return Math.round(xpReward);
        } else {
            return 0;
        }
    }
}
