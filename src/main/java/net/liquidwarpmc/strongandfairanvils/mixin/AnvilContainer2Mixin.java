package net.liquidwarpmc.strongandfairanvils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant.Condition;

@Mixin(targets = "net/minecraft/container/AnvilContainer$2")
public abstract class AnvilContainer2Mixin {

    @ModifyConstant(
            method = "canTakeItems(Lnet/minecraft/entity/player/PlayerEntity;)Z",
            constant = @Constant(expandZeroConditions = Condition.GREATER_THAN_ZERO, ordinal = 0),
            require = 1
    )
    private int allowZeroLevelCost(int value) { return -1; }

    @ModifyConstant(
            method = "method_17370(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V",
            constant = @Constant(floatValue = 0.12F),
            require = 1)
    private static float preventRandomAnvilDegradationThroughUse(float value) { return -1.0F; }
}

