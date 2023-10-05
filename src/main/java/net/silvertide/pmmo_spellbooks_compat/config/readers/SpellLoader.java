//package net.silvertide.pmmo_spellbooks_compat.config.readers;
//
//import com.google.common.collect.Sets;
//import harmonised.pmmo.config.readers.MergeableCodecDataManager;
//import net.minecraft.resources.ResourceLocation;
//import net.silvertide.pmmo_spellbooks_compat.config.codecs.SpellData;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public class SpellLoader {
//    public static final MergeableCodecDataManager<SpellData, Set<ResourceLocation>> DATA_LOADER = new MergeableCodecDataManager<>(
//            "flavors",
//            SpellData.CODEC,
//            raws -> processSpells(raws));
//
//    public static Set<String> processSpells(final List<SpellData> raws)
//    {
//        return raws.stream().reduce(new HashSet<String>(), SpellLoader::processSpell, Sets::union);
//    }
//
//    public static Set<ResourceLocation> processSpell(final Set<String> set, final SpellData raw)
//    {
//        return processFlavors(raw.override()? new HashSet<>() : set, raw.getSources());
//    }
//
//    public static Set<ResourceLocation> processFlavors(final Set<ResourceLocation> set, final List<ResourceLocation> flavors)
//    {
//        set.addAll(flavors);
//        return set;
//    }
//}
