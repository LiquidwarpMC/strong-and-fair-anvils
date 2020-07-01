package net.liquidwarpmc.strongandfairanvils.mixin;

import net.liquidwarpmc.strongandfairanvils.StrongAndFairAnvils;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilBlock.class)
public abstract class AnvilBlockMixin {

    @Inject(method="getLandingState(Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;", at = @At("HEAD"), cancellable = true, require = 1)
    private static void destroyStoneAnvilOnFalling(BlockState blockState, CallbackInfoReturnable<BlockState> cir) {
       if(blockState.isOf(StrongAndFairAnvils.STONE_ANVIL)) {
           cir.setReturnValue(null);
           cir.cancel();
       }
    }
}
