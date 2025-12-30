package com.github.rcubedev.riptidefix.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import com.github.rcubedev.riptidefix.RiptideFix;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Debug(export = true)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow protected int autoSpinAttackTicks;

    @Shadow protected abstract float getWaterSlowDown();

    @ModifyVariable(
            method = {
                    //? if >=1.21.10 {
                    /*"travelInFluid(Lnet/minecraft/world/phys/Vec3;)V",
                    // neoforge patches above to add a FluidState param and makes the old method wrap the new one
                    "travelInFluid(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/material/FluidState;)V"
                    *///?} else {
                    "travel(Lnet/minecraft/world/phys/Vec3;)V"
                    //?}
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getSpeed()F"),
            ordinal = 0
    )
    protected float undoRiptideDrag(float f, @Local(ordinal = 2) float h) {
        if (!RiptideFix.isEnabled()) return f;

        float fNew = f;
        // Workaround if h is 1, which would cause division by zero; shouldn't be (attribute caps at 1, but h is half of that).
        // Instead of just skipping, f is set back to the base speed (this will cause issues if a mixin modifies how f is
        // calculated as this is derived from the vanilla implementation)
        if (1 - h == 0) {
            fNew = isSprinting() ? 0.9F : getWaterSlowDown();
        }

        if (this.autoSpinAttackTicks != 0) { // h
            return (fNew - 0.54600006F * h) / (1 - h);
        }
        return fNew;
    }
}
