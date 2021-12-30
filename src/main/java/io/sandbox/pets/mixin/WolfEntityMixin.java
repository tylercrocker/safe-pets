package io.sandbox.pets.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.world.World;

@Mixin(WolfEntity.class)
public abstract class WolfEntityMixin extends TameableEntity implements Angerable {
  public WolfEntityMixin(EntityType<? extends WolfEntity> entityType, World world) {
    super(entityType, world);
    throw new IllegalStateException("WolfEntityMixin's dummy constructor called!");
  }

  @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cbir) {
    // If the wolf isn't tamed we can just leave.
    if (!this.isTamed()) { return; }

    // If it was the wolf's owner then prevent the damage.
    if (this.isTeammate(source.getAttacker())) {
      cbir.setReturnValue(false);
      cbir.cancel();
    }
  }
}
