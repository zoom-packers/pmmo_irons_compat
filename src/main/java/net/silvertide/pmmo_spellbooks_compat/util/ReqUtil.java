package net.silvertide.pmmo_spellbooks_compat.util;

import harmonised.pmmo.core.Core;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;

public class ReqUtil {
    public boolean isSpellCastPermitted(String spell_name, Player player) {
        Core core = Core.get(player.level());
        if (player instanceof FakePlayer
                || core.getLoader().PLAYER_LOADER.getData(new ResourceLocation(player.getUUID().toString())).ignoreReq()) return true;
        return false;
//        return Core.doesPlayerMeetReq();
    }
}
