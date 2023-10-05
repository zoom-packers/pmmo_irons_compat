package net.silvertide.pmmo_spellbooks_compat.util;

import net.minecraft.resources.ResourceLocation;
import net.silvertide.pmmo_spellbooks_compat.PMMOSpellBooksCompat;
import org.jetbrains.annotations.Nullable;

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
}
