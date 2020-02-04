package net.liquidwarpmc.strongandfairanvils.mixin;

import net.liquidwarpmc.strongandfairanvils.StrongAndFairAnvils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin {

    @Shadow private BlockState block;

    @ModifyVariable(
            method = "handleFallDamage",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/block/BlockState;matches(Lnet/minecraft/tag/Tag;)Z",
                    ordinal = 0
            ),
            index = 5,
            require = 1
    )
    private boolean destroyStoneAnvilOnFalling(boolean value) {
        return value || this.block.getBlock() == StrongAndFairAnvils.STONE_ANVIL;
    }
}
