package net.silvertide.pmmo_spellbooks_compat.config.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import harmonised.pmmo.config.codecs.CodecTypes;

import java.util.*;

public record SpellRequirement(boolean replace, Map<String, Map<String, Integer>> reqs, Map<String, Integer> defaultReqs, List<String> sources) {
    public static final Codec<SpellRequirement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("replace", false).forGetter(SpellRequirement::replace),
            Codec.unboundedMap(Codec.STRING, CodecTypes.INTEGER_CODEC).fieldOf("requirements").forGetter(SpellRequirement::reqs),
            Codec.unboundedMap(Codec.STRING, Codec.INT).optionalFieldOf("default_requirements", new HashMap<>()).forGetter(SpellRequirement::defaultReqs),
            Codec.list(Codec.STRING).optionalFieldOf("sources", new ArrayList<>()).forGetter(SpellRequirement::sources))
            .apply(instance, SpellRequirement::new)
    );

    public Map<String, Integer> getRequirementMap(int spellLevel) {
        String level = String.valueOf(spellLevel);
        if(reqs().containsKey(level)) {
            return reqs().get(level);
        } else if (!defaultReqs().isEmpty()) {
            return defaultReqs();
        } else {
            return null;
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("replace: ").append(replace()).append("\n");
        for(String source : sources()) {
            result.append(" ").append(source);
        }
        result.append("\n");
        return result.toString();
    }
}
