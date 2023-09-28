package net.silvertide.pmmo_spellbooks_compat.mixin;
import harmonised.pmmo.api.APIUtils;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.spells.holy.FortifySpell;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FortifySpell.class)
public abstract class FortifyMixin extends AbstractSpell {
    @Inject(method = "lambda$onCast$0(Lnet/minecraft/world/entity/LivingEntity;ILnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.addEffect (Lnet/minecraft/world/effect/MobEffectInstance;)Z", shift = At.Shift.AFTER), remap = false)
    public void injectAfterShielding(LivingEntity entity, int spellLevel, LivingEntity target, CallbackInfo ci) {
        float xpAmount = 20*super.getSpellPower(spellLevel, entity);
        APIUtils.addXp("wisdom", (ServerPlayer) entity, (long) xpAmount);
        entity.sendSystemMessage(Component.literal("You fortified " + target.getName()));
    }
}
