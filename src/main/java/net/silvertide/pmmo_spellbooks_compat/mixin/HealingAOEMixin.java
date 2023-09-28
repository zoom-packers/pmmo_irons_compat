package net.silvertide.pmmo_spellbooks_compat.mixin;
import harmonised.pmmo.api.APIUtils;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.entity.spells.HealingAoe;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HealingAoe.class)
public abstract class HealingAOEMixin extends AoeEntity {

    public HealingAOEMixin(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "applyEffect", at = @At("HEAD"), remap = false)
    public void applyEffect(LivingEntity target, CallbackInfo ci) {
        boolean targetWillHeal = target.getHealth() < target.getMaxHealth();
        if (getOwner() instanceof LivingEntity owner && Utils.shouldHealEntity(owner, target) && targetWillHeal) {
            owner.sendSystemMessage(Component.literal("You healed " + target.getName() + " for " + getDamage()));
            APIUtils.addXp("wisdom", (ServerPlayer) owner, (long)(getDamage()*20));
        }
    }
}
