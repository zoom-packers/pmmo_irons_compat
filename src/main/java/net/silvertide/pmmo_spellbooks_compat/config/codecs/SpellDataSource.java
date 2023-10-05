//package net.silvertide.pmmo_spellbooks_compat.config.codecs;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Collection;
//
//// Adapted from Project MMO code
//public interface SpellDataSource {
//
//    public SpellData combine(SpellData two);
//
//    public boolean isUnconfigured();
//
//    public default List<String> getSources() { return new ArrayList<>(); }
//
//    public default Map<String, Integer> getReqs(int skillLevel) {
//        return new HashMap<>();
//    };
//    public default void setReqs(int skillLevel, Map<String, Integer> skillReqs) {}
//
//    public static <K, V> HashMap<K,V> clearEmptyValues(Map<K,V> map) {
//        HashMap<K, V> outMap = new HashMap<>();
//        map.forEach((key, value) -> {
//            boolean isEmpty = false;
//            if (value instanceof Collection)
//                isEmpty = ((Collection<?>)value).isEmpty();
//            else if (value instanceof Map)
//                isEmpty = ((Map<?,?>)value).isEmpty();
//
//            if (!isEmpty)
//                outMap.put(key, value);
//        });
//        return outMap;
//    }
//}
