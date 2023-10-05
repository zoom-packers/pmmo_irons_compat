//package net.silvertide.pmmo_spellbooks_compat.config.codecs;
//
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import harmonised.pmmo.api.enums.ReqType;
//import harmonised.pmmo.config.codecs.CodecTypes;
//import harmonised.pmmo.util.Functions;
//import net.minecraft.util.StringRepresentable;
//
//import java.util.*;
//import java.util.function.BiConsumer;
//
//public record SpellData(boolean override, List<String> sources, Map<Integer, Map<String, Integer>> reqs) implements SpellDataSource {
//    public SpellData() {
//        this(false, new ArrayList<>(), new HashMap<>());
//    }
//
//    @Override
//    public void setReqs(int skillLevel, Map<String, Integer> skillReqs) {
//        reqs().put(skillLevel, skillReqs);
//    }
//    @Override
//    public Map<String, Integer> getReqs(int skillLevel) {
//        return reqs().get(skillLevel);
//    }
//    @Override
//    public List<String> getSources() { return sources(); }
//    public static final Codec<SpellData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
//            Codec.BOOL.fieldOf("override").forGetter(SpellData::override),
//            Codec.STRING.listOf().optionalFieldOf("sources", new ArrayList<>()).forGetter(SpellData::sources),
//            Codec.optionalField("requirements",
//                            Codec.simpleMap(Codec.INT, CodecTypes.INTEGER_CODEC, StringRepresentable.keys(ReqType.values())).codec())
//                    .forGetter(SpellData::getReqs))
//
//    ));
//    @Override
//    public SpellData combine(SpellData two) {
//        Set<String> sources = new HashSet<>();
//        Map<Integer, Map<String, Integer>> reqs = new HashMap<>();
//
//        BiConsumer<SpellData, SpellData> bothOrNeither = (o, t) -> {
//            sources.addAll(o.sources());
//            t.sources.forEach((s) -> {
//                if (!sources.contains(s))
//                    sources.add(s);
//            });
//
//            reqs.putAll(o.reqs());
//            t.reqs().forEach((event, map) -> {
//                reqs.merge(event, map, (oMap, nMap) -> {
//                    Map<String, Integer> mergedMap = new HashMap<>(oMap);
//                    nMap.forEach((k, v) -> mergedMap.merge(k, v, (o1, n1) -> o1 > n1 ? o1 : n1));
//                    return mergedMap;
//                });
//            });
//        };
//
//        Functions.biPermutation(this, two, this.override(), two.override(), (o, t) -> {
//                    sources.addAll(o.sources().isEmpty() ? t.sources() : o.sources());
//                    reqs.putAll(o.reqs().isEmpty() ? t.reqs() : o.reqs());
//                },
//                bothOrNeither,
//                bothOrNeither);
//
//        return new SpellData(this.override() || two.override(), sources.stream().toList(), reqs);
//    };
//
//    @Override
//    public boolean isUnconfigured() {
//        return reqs().values().stream().allMatch(map -> map.isEmpty());
//    }
//
//}
