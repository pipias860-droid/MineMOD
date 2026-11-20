package com.cosmos.hiteffects.mixin;

import com.cosmos.hiteffects.client.CosmosHitParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {
	@Inject(method = "attackVisualEffects", at = @At("TAIL"))
	private void cosmos$spawnNebulaHit(Entity target, boolean criticalHit, boolean sweeping, boolean cooldownPassed,
			boolean pierce, float enchantDamage, CallbackInfo ci) {
		Player self = (Player) (Object) this;
		if (self.level() instanceof ClientLevel level && target instanceof Player) {
			CosmosHitParticles.spawnNebulaBurst(level, target, criticalHit);
		}
	}

	@Inject(method = "crit", at = @At("TAIL"))
	private void cosmos$spawnCriticalStars(Entity target, CallbackInfo ci) {
		Player self = (Player) (Object) this;
		if (self.level() instanceof ClientLevel level && target instanceof Player) {
			CosmosHitParticles.spawnCriticalStars(level, target);
		}
	}
}
