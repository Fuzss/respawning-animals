package fuzs.respawninganimals.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.Level;

public class AnimalPersistenceHandler {

    public static void onEndEntityTick(Entity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            if (entity instanceof Mob mob && !mob.isPersistenceRequired() && AnimalSpawningHandler.isAllowedToDespawn(
                    mob,
                    serverLevel.getGameRules())) {
                if (requiresCustomPersistence(mob)) {
                    mob.setPersistenceRequired();
                }
            }
        }
    }

    private static boolean requiresCustomPersistence(Mob mob) {
        // do not include saddled mobs, e.g. striders can spawn with saddles when ridden by zombified piglin
        if (mob instanceof Animal animal && animal.isInLove()) {
            // animals that are in love from using their breeding item on them
            return true;
        } else if (mob.isLeashed()) {
            // mobs with a lead attached to them
            return true;
        } else if (mob instanceof OwnableEntity ownable && ownable.getOwnerUUID() != null) {
            // mobs that have an owner like horses or wolves
            return true;
        } else {
            return false;
        }
    }

    public static EventResult onAnimalTame(Animal animal, Player player) {
        // enable persistence for animals that have been tamed (cats, ocelots, wolves, and all horse types including llamas)
        setPersistenceForVolatileAnimal(animal);
        return EventResult.PASS;
    }

    public static EventResult onStartRiding(Level level, Entity rider, Entity vehicle) {
        // make mobs the player has ridden persistent
        if (rider instanceof Player) {
            setPersistenceForVolatileAnimal(vehicle);
        } else if (vehicle instanceof VehicleEntity) {
            setPersistenceForVolatileAnimal(rider);
        }

        return EventResult.PASS;
    }

    private static void setPersistenceForVolatileAnimal(Entity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            if (entity instanceof Mob mob && mob.getType().getCategory() == MobCategory.CREATURE) {
                if (!mob.isPersistenceRequired() && AnimalSpawningHandler.isAllowedToDespawn(mob,
                        serverLevel.getGameRules())) {
                    mob.setPersistenceRequired();
                }
            }
        }
    }
}
