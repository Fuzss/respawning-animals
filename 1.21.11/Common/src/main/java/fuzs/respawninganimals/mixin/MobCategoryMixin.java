package fuzs.respawninganimals.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobCategory.class)
abstract class MobCategoryMixin {
    @Shadow
    public int noDespawnDistance;

    @ModifyReturnValue(method = "getNoDespawnDistance", at = @At("RETURN"))
    public int getNoDespawnDistance(int noDespawnDistance) {
        // This method returns a constant normally, and the corresponding field goes unused.
        return this.noDespawnDistance;
    }
}
