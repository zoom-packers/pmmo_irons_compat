package net.silvertide.pmmo_spellbooks_compat.config.writers;

import com.google.gson.*;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ModifierDataType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.config.codecs.CodecTypes;
import harmonised.pmmo.config.codecs.ObjectData;
import harmonised.pmmo.config.codecs.VeinData;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.core.nbt.LogicEntry;
import harmonised.pmmo.features.autovalues.AutoValues;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.fml.LogicalSide;
import net.silvertide.pmmo_spellbooks_compat.PMMOSpellBooksCompat;
import net.silvertide.pmmo_spellbooks_compat.enums.ObjectType;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class PackGenerator {
    public static final String PACKNAME = "pmmo_irons_compat_pack";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static boolean applyOverride = false, applyDefaults = false, applyDisabler = false, applySimple = false;
    public static List<String> namespaceFilter = new ArrayList<>();
    public static Set<ServerPlayer> players = new HashSet<>();

    public static int generatePack(MinecraftServer server) {
        //create the filepath for our datapack.  this will do nothing if already created
        Path filepath = server.getWorldPath(LevelResource.DATAPACK_DIR).resolve(PACKNAME);
        filepath.toFile().mkdirs();
        /* checks for existence of the pack.mcmeta.  This will:
         * 1. create a new file if not present, using the disabler setting
         * 2. overwrite the existing file if the disabler setting conflicts*/
        Path packPath = filepath.resolve("pack.mcmeta");
        try {
            Files.writeString(
                    packPath,
                    gson.toJson(getPackObject()),
                    Charset.defaultCharset(),
                    StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            System.out.println("Error While Generating pack.mcmeta for Spellbooks Compat Generated Data: "+e.toString());
        }

        for(AbstractSpell spell : SpellRegistry.getEnabledSpells()) {
            int index = spell.getSpellId().lastIndexOf(':');
            String spellname = spell.getSpellId().substring(index+1);
            Path finalPath = filepath.resolve("data/pmmo_spellbooks_compat/spells");
            finalPath.toFile().mkdirs();
            String defaultValue = "{\"requirements\":{\"default\":[]},\"sources\":[\"scroll\",\"spellbook\",\"sword\"]}";
            try {
                Files.writeString(
                        finalPath.resolve(spellname+".json"),
                        prettyPrintJSON(defaultValue),
                        Charset.defaultCharset(),
                        StandardOpenOption.CREATE_NEW,
                        StandardOpenOption.WRITE);
            } catch (IOException e) {System.out.println("Error While Generating Pack File For: "+spellname+" ("+e.toString()+")");}
        }
        return 0;
    }

    private static String prettyPrintJSON(String jsonString) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        return gson.toJson(jsonElement);
    }

    private static record Pack(String description, int format) {
        public static final Codec<Pack> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("description").forGetter(Pack::description),
                Codec.INT.fieldOf("pack_format").forGetter(Pack::format)
        ).apply(instance, Pack::new));
    }
    private static record BlockFilter(Optional<String> namespace, Optional<String> path) {
        public static final Codec<BlockFilter> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.optionalFieldOf("namespace").forGetter(BlockFilter::namespace),
                Codec.STRING.optionalFieldOf("path").forGetter(BlockFilter::path)
        ).apply(instance, BlockFilter::new));
    }
    private static record Filter(List<BlockFilter> block) {
        public static final Codec<Filter> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BlockFilter.CODEC.listOf().fieldOf("block").forGetter(Filter::block)
        ).apply(instance, Filter::new));
    }
    private static record McMeta(Pack pack, Optional<Filter> filter) {
        public static final Codec<McMeta> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Pack.CODEC.fieldOf("pack").forGetter(McMeta::pack),
                Filter.CODEC.optionalFieldOf("filter").forGetter(McMeta::filter)
        ).apply(instance, McMeta::new));
    }

    private static JsonElement getPackObject() {
        McMeta pack = new McMeta(
                new Pack("Generated Resources", 9),
                Optional.empty());

        return McMeta.CODEC.encodeStart(JsonOps.INSTANCE, pack).result().get();
    }
}
