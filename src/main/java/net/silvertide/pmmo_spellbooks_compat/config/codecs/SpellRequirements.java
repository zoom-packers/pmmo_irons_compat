package net.silvertide.pmmo_spellbooks_compat.config.codecs;

import java.util.List;

public class SpellRequirements {
    public static final MergeableCodecDataManager<SpellRequirement, SpellRequirement> DATA_LOADER = new MergeableCodecDataManager<>(
            "spells",
            SpellRequirement.CODEC,
            SpellRequirements::processSpellRequirements);

    public static SpellRequirement processSpellRequirements(final List<SpellRequirement> raws) {
        // Simple merger function. Takes the latest SpellRequirement with replace = true;
        SpellRequirement result = raws.get(0);
        if(raws.size() > 1){
            for(int i = 1; i < raws.size(); i++) {
                SpellRequirement curr = raws.get(i);
                if(curr.replace()) result = curr;
            }
        }
        return result;
    }
}
