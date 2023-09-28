package net.silvertide.pmmo_spellbooks_compat.mixin;
import harmonised.pmmo.api.APIUtils;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.spells.holy.BlessingOfLifeSpell;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlessingOfLifeSpell.class)
public abstract class BlessingOfLifeMixin extends AbstractSpell {
    @Inject(method = "onCast", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;heal(F)V", shift = At.Shift.AFTER), remap = false)
    public void injectAfterHealing(Level world, int spellLevel, LivingEntity entity, MagicData playerMagicData, CallbackInfo ci) {
        float xpAmount = 20*super.getSpellPower(spellLevel, entity) * .25f;
        APIUtils.addXp("wisdom", (ServerPlayer) entity, (long) xpAmount);
    }
}
