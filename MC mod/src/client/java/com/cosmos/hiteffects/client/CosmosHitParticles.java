package com.cosmos.hiteffects.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

/** Client-only particle choreography for the hit and critical-hit visuals. */
public final class CosmosHitParticles {
	private static final int VIOLET = 0x6C35FF;
	private static final int MAGENTA = 0xFF3CCB;
	private static final int CYAN = 0x32D9FF;

	private CosmosHitParticles() {
	}

	public static int nebulaTint() {
		float phase = (System.currentTimeMillis() % 1600L) / 1600.0F;
		float wave = (float) (Math.sin(phase * Math.PI * 2.0F) * 0.5F + 0.5F);

		int first = blend(VIOLET, MAGENTA, wave);
		int second = blend(MAGENTA, CYAN, wave);
		return 0xFF000000 | blend(first, second, 0.35F);
	}

	public static void spawnNebulaBurst(ClientLevel level, Entity target, boolean critical) {
		if (!(target instanceof LivingEntity living)) {
			return;
		}

		double centerX = living.getX();
		double centerY = living.getY() + living.getBbHeight() * 0.55D;
		double centerZ = living.getZ();
		DustColorTransitionOptions nebula = new DustColorTransitionOptions(VIOLET, MAGENTA, 1.15F);
		DustColorTransitionOptions nebulaBlue = new DustColorTransitionOptions(MAGENTA, CYAN, 0.9F);

		int count = critical ? 16 : 10;
		for (int i = 0; i < count; i++) {
			double angle = Math.PI * 2.0D * i / count;
			double radius = 0.18D + (i % 3) * 0.08D;
			double velocityX = Math.cos(angle) * 0.045D;
			double velocityZ = Math.sin(angle) * 0.045D;
			level.addParticle(i % 2 == 0 ? nebula : nebulaBlue,
					centerX + Math.cos(angle) * radius,
					centerY + (i % 4) * 0.08D,
					centerZ + Math.sin(angle) * radius,
					velocityX, 0.025D + (i % 3) * 0.012D, velocityZ);
		}

		level.addParticle(ParticleTypes.ENCHANTED_HIT, centerX, centerY, centerZ, 0.0D, 0.08D, 0.0D);
	}

	public static void spawnCriticalStars(ClientLevel level, Entity target) {
		if (!(target instanceof LivingEntity living)) {
			return;
		}

		double centerX = living.getX();
		double centerY = living.getY() + living.getBbHeight() * 0.62D;
		double centerZ = living.getZ();
		int points = 12;
		for (int i = 0; i < points; i++) {
			double angle = Math.PI * 2.0D * i / points;
			double radius = 0.32D + (i % 2) * 0.12D;
			level.addParticle(ParticleTypes.END_ROD,
					centerX + Math.cos(angle) * radius,
					centerY + Math.sin(angle * 2.0D) * 0.18D,
					centerZ + Math.sin(angle) * radius,
					Math.cos(angle) * 0.06D, 0.04D, Math.sin(angle) * 0.06D);
		}

		for (int i = 0; i < 6; i++) {
			double angle = Math.PI * 2.0D * i / 6.0D + Math.PI / 6.0D;
			level.addParticle(ParticleTypes.END_ROD,
					centerX + Math.cos(angle) * 0.18D,
					centerY + 0.22D + (i % 2) * 0.08D,
					centerZ + Math.sin(angle) * 0.18D,
					Math.cos(angle) * 0.02D, 0.085D, Math.sin(angle) * 0.02D);
		}
	}

	private static int blend(int first, int second, float amount) {
		amount = Math.max(0.0F, Math.min(1.0F, amount));
		int red = lerp((first >> 16) & 0xFF, (second >> 16) & 0xFF, amount);
		int green = lerp((first >> 8) & 0xFF, (second >> 8) & 0xFF, amount);
		int blue = lerp(first & 0xFF, second & 0xFF, amount);
		return (red << 16) | (green << 8) | blue;
	}

	private static int lerp(int first, int second, float amount) {
		return Math.round(first + (second - first) * amount);
	}
}
