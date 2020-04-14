package com.example.examplemod.entity;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A Wild Boar entity.
 * Literally just a pig (with a different texture).
 * TODO: Will have more stuff added to it soon. (Will charge at player
 *
 * @author Cadiboo
 */
public class WildBoarEntity extends PigEntity {

	private static final Logger LOGGER = LogManager.getLogger();

	public WildBoarEntity(final EntityType<? extends WildBoarEntity> entityType, final World world) {
		super(entityType, world);
	}

	@Override
	protected void onChildSpawnFromEgg(PlayerEntity playerIn, AgeableEntity child) {
		LOGGER.info(
				"WildBoar spawned from {} with EntityID {}",
				playerIn.getName().getString(),
				child.getDisplayName().toString()
				);
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();

		final double baseSpeed = this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
		final double baseHealth = this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue();
		// Multiply base health and base speed by one and a half
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(baseSpeed * 1.5D);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(baseHealth * 1.5D);
	}

	@Override
	public WildBoarEntity createChild(final AgeableEntity parent) {
		// Use getType to support overrides in subclasses
		WildBoarEntity entity = (WildBoarEntity) getType().create(this.world);
		LOGGER.info("While Boar Entity Created {}", entity.getEntityId());
		return entity;
	}

	/**
	 * Called on the logical server to get a packet to send to the client containing data necessary to spawn your entity.
	 * Using Forge's method instead of the default vanilla one allows extra stuff to work such as sending extra data,
	 * using a non-default entity factory and having {@link IEntityAdditionalSpawnData} work.
	 *
	 * It is not actually necessary for our WildBoarEntity to use Forge's method as it doesn't need any of this extra
	 * functionality, however, this is an example mod and many modders are unaware that Forge's method exists.
	 *
	 * @return The packet with data about your entity
	 * @see FMLPlayMessages.SpawnEntity
	 */
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
