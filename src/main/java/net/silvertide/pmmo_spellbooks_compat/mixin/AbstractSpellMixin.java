package net.silvertide.pmmo_spellbooks_compat.mixin;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSpell.class)
public abstract class AbstractSpellMixin {
    @Inject(method = "attemptInitiateCast", at = @At("HEAD"), cancellable = true, remap = false)
    public void attemptInitiateCast(ItemStack stack, int spellLevel, Level level, Player player, CastSource castSource, boolean triggerCooldown, CallbackInfoReturnable<Boolean> cir) {
        if(!level.isClientSide) {
            player.sendSystemMessage(Component.literal("Failed the cast brah."));
            cir.setReturnValue(false);
        }
    }
}
