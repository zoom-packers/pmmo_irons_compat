package net.silvertide.pmmo_spellbooks_compat.mixin;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.spells.holy.BlessingOfLifeSpell;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlessingOfLifeSpell.class)
public abstract class BlessingOfLifeMixin extends AbstractSpell {
    @ModifyReceiver(method = "onCast", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;heal(F)V"), remap = true)
    public LivingEntity addXpBeforeHeal(LivingEntity targetEntity, float healAmount, Level world, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        entity.sendSystemMessage(Component.literal("You just healed " + targetEntity.getName() + " for " + healAmount));
        return targetEntity;
    }


//    public void injectAfterHealing(Level world, int spellLevel, LivingEntity entity, MagicData playerMagicData, CallbackInfo ci) {
//        float xpAmount = 20*super.getSpellPower(spellLevel, entity) * .25f;
//        APIUtils.addXp("wisdom", (ServerPlayer) entity, (long) xpAmount);
//    }
}
