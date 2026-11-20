package com.cosmos.hiteffects.mixin;

import com.cosmos.hiteffects.client.CosmosHitParticles;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	@Inject(method = "getMixColor", at = @At("HEAD"), cancellable = true)
	private void cosmos$replacePlayerHurtTint(LivingEntityRenderState state, CallbackInfoReturnable<Integer> cir) {
		if (state instanceof PlayerRenderState && state.hasRedOverlay) {
			cir.setReturnValue(CosmosHitParticles.nebulaTint());
		}
	}
}
