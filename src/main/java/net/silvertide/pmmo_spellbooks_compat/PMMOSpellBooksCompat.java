package net.silvertide.pmmo_spellbooks_compat;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.silvertide.pmmo_spellbooks_compat.config.Config;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.silvertide.pmmo_spellbooks_compat.commands.CmdPmmoSpellBooksRoot;
import net.silvertide.pmmo_spellbooks_compat.config.codecs.SpellRequirements;
import net.silvertide.pmmo_spellbooks_compat.network.SpellRequirementSyncPacket;
import org.slf4j.Logger;

@Mod(PMMOSpellBooksCompat.MOD_ID)
public class PMMOSpellBooksCompat {
    public static final String MOD_ID = "pmmo_spellbooks_compat";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final String CHANNEL_PROTOCOL = "0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MOD_ID, "main"),
            () -> CHANNEL_PROTOCOL,
            CHANNEL_PROTOCOL::equals,
            CHANNEL_PROTOCOL::equals);

    public PMMOSpellBooksCompat()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        // Register ourselves for server and other game events we are interested in
        this.registerPackets();

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        SpellRequirements.DATA_LOADER.subscribeAsSyncable(CHANNEL, SpellRequirementSyncPacket::new);
    }
    void registerPackets()
    {
        int id = 0;
        CHANNEL.registerMessage(id++, SpellRequirementSyncPacket.class,
                SpellRequirementSyncPacket::encode,
                SpellRequirementSyncPacket::decode,
                SpellRequirementSyncPacket::onPacketReceived);
    }

    @Mod.EventBusSubscriber(modid=PMMOSpellBooksCompat.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
    public static class CommonSetup {
        @SubscribeEvent
        public static void onCommandRegister(RegisterCommandsEvent event) {
            CmdPmmoSpellBooksRoot.register(event.getDispatcher());
        }
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {}

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
    }
}
