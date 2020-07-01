package net.liquidwarpmc.strongandfairanvils.mixin;

import net.liquidwarpmc.strongandfairanvils.SetEmptyItemStackInterface;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements SetEmptyItemStackInterface {
    @Shadow
    @Final
    @Mutable
    private Item item;

    @Shadow
    protected abstract void updateEmptyState();

    @Shadow
    public abstract void setCount(int count);

    @Override
    public void strongandfairanvils_setEmpty() {
        this.item = null;
        this.setCount(1);
        this.updateEmptyState();
    }
}
