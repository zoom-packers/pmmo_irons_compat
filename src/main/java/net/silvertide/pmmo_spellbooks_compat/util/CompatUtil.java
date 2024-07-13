package net.silvertide.pmmo_spellbooks_compat.util;

import com.google.common.base.Preconditions;
import harmonised.pmmo.api.APIUtils;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.core.CoreUtils;
import harmonised.pmmo.core.IDataStorage;
import io.redspace.ironsspellbooks.api.events.InscribeSpellEvent;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.silvertide.pmmo_spellbooks_compat.PMMOSpellBooksCompat;
import net.silvertide.pmmo_spellbooks_compat.config.codecs.SpellRequirement;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
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

    public static SpellEventResult canCastSpell(SpellPreCastEvent spellPreCastEvent, SpellRequirement spellRequirement) {
        List<String> sources = spellRequirement.sources();
        String sourceString = stringifyCastSource(spellPreCastEvent.getCastSource());
        if(sources.size() > 0 && sourceString != null && sources.contains(sourceString)){
            Map<String, Integer> requirementMap = spellRequirement.getRequirementMap(spellPreCastEvent.getSpellLevel());
            if(requirementMap != null) {
                for(String skill : requirementMap.keySet()) {
                    int requiredLevel = requirementMap.get(skill);
                    if(requiredLevel > APIUtils.getLevel(skill, spellPreCastEvent.getEntity())) {
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

    public static float getAmountHealed(LivingEntity targetEntity, float healAmount) {
        float missingLife = targetEntity.getMaxHealth() - targetEntity.getHealth();
        return Math.min(healAmount, missingLife);
    }

    public static void addXp(String skill, Player player, long change) {
        Preconditions.checkNotNull(skill);
        Preconditions.checkNotNull(player);

        var skills = skill.split("\\|");
        for (String s : skills) {
            Map<String, Long> xpMap = new HashMap<>();
            xpMap.put(s, change);
            CoreUtils.processSkillGroupXP(xpMap);

            IDataStorage data = Core.get(player.level()).getData();
            xpMap.forEach((key, value) -> data.setXpDiff(player.getUUID(), key, value));
        }
    }
}
