package net.silvertide.pmmo_spellbooks_compat.util;

import harmonised.pmmo.api.APIUtils;
import io.redspace.ironsspellbooks.api.events.SpellCastEvent;
import net.minecraft.resources.ResourceLocation;
import net.silvertide.pmmo_spellbooks_compat.PMMOSpellBooksCompat;
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
        String levelString = String.valueOf(spellCastEvent.getSpellLevel());
        //TODO: Check source here once new version of Iron's is out.
        if(spellRequirement.reqs().containsKey(levelString)) {
            Map<String, Integer> reqMap = spellRequirement.reqs().get(levelString);
            for(String skill : reqMap.keySet()) {
                int requiredLevel = reqMap.get(skill);
                if(requiredLevel > APIUtils.getLevel(skill, spellCastEvent.getEntity())){
                    return new SpellCastResult(false, requiredLevel + " " + skill);
                }
            }
        } else if(!spellRequirement.defaultReqs().isEmpty()) {
            for(String skill : spellRequirement.defaultReqs().keySet()) {
                int requiredLevel = spellRequirement.defaultReqs().get(skill);
                if(requiredLevel > APIUtils.getLevel(skill, spellCastEvent.getEntity())){
                    return new SpellCastResult(false, requiredLevel + " " + skill);
                }
            }
        }
        return new SpellCastResult(true, "");
    }


}
