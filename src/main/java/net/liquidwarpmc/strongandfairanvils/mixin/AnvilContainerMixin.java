package net.liquidwarpmc.strongandfairanvils.mixin;

import net.liquidwarpmc.strongandfairanvils.SetEmptyItemStackInterface;
import net.liquidwarpmc.strongandfairanvils.StrongAndFairAnvils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.container.AnvilContainer;
import net.minecraft.container.BlockContext;
import net.minecraft.container.Property;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(AnvilContainer.class)
public abstract class AnvilContainerMixin {

    @Shadow
    @Final
    private BlockContext context;

    @Shadow
    @Final
    private Property levelCost;

    @Shadow
    @Final
    private Inventory inventory;

    /**
     * Disable the default prior work penalty increase.
     */
    @Redirect(
            method = "updateResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/container/AnvilContainer;getNextCost(I)I",
                    ordinal = 0
            ),
            require = 1
    )
    private int preventDefaultPriorWorkPenaltyIncrease(int cost) {
//        StrongAndFairAnvils.LOGGER.info("Preventing default prior work penalty increase");
        return cost;
    }


    /**
     * Don't increase level cost for repair between unenchanted items.
     */
    @ModifyVariable(
            method = "updateResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;setDamage(I)V",
                    ordinal = 0
            ),
            ordinal = 0,
            require = 1
    )
    private int freeLevelRepairCostBetweenUnenchantedItem(int val) {
        ItemStack itemStack = this.inventory.getInvStack(0);
        ItemStack itemStack3 = this.inventory.getInvStack(1);

        if(itemStack.hasEnchantments() || itemStack3.hasEnchantments()) {
//            StrongAndFairAnvils.LOGGER.info("Enchanted items: costly repair");
            return val;
        } else {
//            StrongAndFairAnvils.LOGGER.info("Unenchanted items: free repair");
            return val - 1;
        }
    }

    /**
     * Don't increase level cost for repair with materials for unenchanted item.
     */
    @ModifyVariable(
            method = "updateResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;setDamage(I)V",
                    ordinal = 1
            ),
            ordinal = 0,
            require = 1
    )
    private int freeLevelRepairCostWithMaterialOfUnenchantedItem(int val) {
        ItemStack itemStack = this.inventory.getInvStack(0);

        if(itemStack.hasEnchantments()) {
//            StrongAndFairAnvils.LOGGER.info("Enchanted item: costly repair with material");
            return val;
        } else {
//            StrongAndFairAnvils.LOGGER.info("Unenchanted item: free repair with material");
            return val - 2;
        }
   }

    /**
     * Don't increase level cost for name operations.
     */
    @ModifyVariable(
            method = "updateResult" ,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;removeCustomName()V",
                    ordinal = 0,
                    shift = At.Shift.BEFORE,
                    by = 1
            ),
            ordinal = 0,
            require = 1
    )
    private int freeNameResetCost(int val) {
//        StrongAndFairAnvils.LOGGER.info("Free name reset");
        return val - 1;
    }


    /**
     * Don't increase level cost for name operations.
     */
    @ModifyVariable(
            method = "updateResult" ,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;setCustomName(Lnet/minecraft/text/Text;)Lnet/minecraft/item/ItemStack;",
                    ordinal = 0,
                    shift = At.Shift.BEFORE,
                    by = 1
            ),
            ordinal = 0,
            require = 1
    )
    private int freeNameChangeCost(int val) {
//        StrongAndFairAnvils.LOGGER.info("Free name change");
        return val - 1;
    }

    /**
     * Allow operations without level cost.
     */
    @ModifyConstant(
            method = "updateResult",
            constant = @Constant(expandZeroConditions = Constant.Condition.GREATER_THAN_ZERO, ordinal = 0),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setCustomName(Lnet/minecraft/text/Text;)Lnet/minecraft/item/ItemStack;", ordinal = 0)
            ),
            require = 1
    )
    private int allowZeroLevelCost(int value) { return -1; }

    /**
     */
    @Inject(
            method = "updateResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/container/Property;set(I)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setCustomName(Lnet/minecraft/text/Text;)Lnet/minecraft/item/ItemStack;", ordinal = 0)
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            require = 1
    )
    private void finalManipulation(CallbackInfo ci,  ItemStack itemStack, int i, int j, int k, ItemStack itemStack2, ItemStack itemStack3, Map map) {
        // itemStack if the first slot (original item)
        // itemStack3 is the second slot (second item, material, enchanted book, etc)
        // itemStack2 is the result

        if(itemStack3 == ItemStack.EMPTY && ItemStack.areItemsEqual(itemStack, itemStack2)) {
            if(ItemStack.areTagsEqual(itemStack, itemStack2)) {
                // Clear result slot if no changes to the item are made.
//                StrongAndFairAnvils.LOGGER.info("No change");
                ((SetEmptyItemStackInterface) (Object) itemStack2).strongandfairanvils_setEmpty();
            } else {
                // Set the cost to 0 if no other changes than name have been done.
//                StrongAndFairAnvils.LOGGER.info("Name change only");
                this.levelCost.set(0);
            }
        } else {
            // Increase prior work penalty if item enchantments have been manipulated.
            Map<Enchantment, Integer> map_original = EnchantmentHelper.getEnchantments(itemStack);
            if(!map.equals(map_original)) {
//                StrongAndFairAnvils.LOGGER.info("Enchantments manipulated: increasing prior work penalty");
                itemStack2.setRepairCost(AnvilContainer.getNextCost(itemStack2.getRepairCost()));
            }
        }
    }

    /**
     * Only allow costless operations to be performed on the stone anvil.
     */
    @Inject(
            method = "updateResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/container/Property;set(I)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER,
                    by = 1
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setCustomName(Lnet/minecraft/text/Text;)Lnet/minecraft/item/ItemStack;", ordinal = 0)
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            require = 1
    )
    private void stoneAnvilNoLevelUseOperationsOnly(CallbackInfo ci,  ItemStack itemStack, int i, int j, int k, ItemStack itemStack2, ItemStack itemStack3, Map map) {
        boolean isStoneAnvil = this.context.run((world, blockPos) -> {
            BlockState blockState = world.getBlockState(blockPos);
            Block block = blockState.getBlock();

            return (block == StrongAndFairAnvils.STONE_ANVIL);
        }, true);

        if(isStoneAnvil && this.levelCost.get() > 0) {
            ((SetEmptyItemStackInterface)(Object) itemStack2).strongandfairanvils_setEmpty();
        }
    }

    /**
     * Whitelist the stone anvil for AnvilInventory screen to show properly.
     */
    @Inject(
            method = "method_17366",
            at = @At("RETURN"),
            require = 1,
            cancellable = true
    )
    private static void addStoneAnvilToEligibleBlocks(PlayerEntity player, World world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        boolean playerInRange = player.squaredDistanceTo((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;

        if(block == StrongAndFairAnvils.STONE_ANVIL && playerInRange) {
            cir.setReturnValue(true);
        }
    }
}
