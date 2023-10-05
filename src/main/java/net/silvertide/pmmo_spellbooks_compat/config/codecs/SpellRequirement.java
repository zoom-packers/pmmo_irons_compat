package net.silvertide.pmmo_spellbooks_compat.config.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public class SpellRequirement {

    public SpellRequirement(final boolean replace, final List<String> sources) {
        this.replace = replace;
        this.sources = sources;
    }

    public static final Codec<SpellRequirement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.optionalFieldOf("replace", false).forGetter(SpellRequirement::getReplace),
            Codec.list(Codec.STRING).optionalFieldOf("sources", new ArrayList<>()).forGetter(SpellRequirement::getSources))
            .apply(instance, SpellRequirement::new)
    );
    private final boolean replace;
    public boolean getReplace() { return this.replace; }

    private final List<String> sources;
    public List<String> getSources() { return this.sources; }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("replace: ").append(getReplace()).append(" /n");
        for(String source : getSources()) {
            result.append(" ").append(source);
        }
        return result.append(" /n").toString();

    }
}
