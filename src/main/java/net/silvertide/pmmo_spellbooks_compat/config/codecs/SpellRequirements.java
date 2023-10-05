package net.silvertide.pmmo_spellbooks_compat.config.codecs;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.silvertide.pmmo_spellbooks_compat.PMMOSpellBooksCompat;

import java.util.*;

public class SpellRequirements {
    public static final MergeableCodecDataManager<SpellRequirement, SpellRequirement> DATA_LOADER = new MergeableCodecDataManager<>(
            "spells",
            SpellRequirement.CODEC,
            SpellRequirements::processSpellRequirements);

    public static SpellRequirement processSpellRequirements(final List<SpellRequirement> raws) {
        return raws.get(0);
    }
}
