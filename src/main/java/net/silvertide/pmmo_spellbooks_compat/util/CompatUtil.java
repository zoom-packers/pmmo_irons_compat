package net.silvertide.pmmo_spellbooks_compat.util;

import harmonised.pmmo.api.APIUtils;
import io.redspace.ironsspellbooks.api.events.SpellCastEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.silvertide.pmmo_spellbooks_compat.PMMOSpellBooksCompat;
import net.silvertide.pmmo_spellbooks_compat.config.Config;
import net.silvertide.pmmo_spellbooks_compat.config.codecs.SpellRequirement;
import org.jetbrains.annotations.Nullable;

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

    public static SpellCastResult canCastSpell(SpellCastEvent spellCastEvent, SpellRequirement spellRequirement) {
        //TODO: Check source here once new version of Iron's is out.
        Map<String, Integer> requirementMap = spellRequirement.getRequirementMap(spellCastEvent.getSpellLevel());
        if(requirementMap != null){
            for(String skill : requirementMap.keySet()) {
                int requiredLevel = requirementMap.get(skill);
                if(requiredLevel > APIUtils.getLevel(skill, spellCastEvent.getEntity())){
                    return new SpellCastResult(false, requiredLevel + " " + skill);
                }
            }
        }
        return new SpellCastResult(true, "");
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
