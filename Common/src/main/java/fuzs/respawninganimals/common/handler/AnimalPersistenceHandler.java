package fuzs.respawninganimals.common.handler;

import fuzs.puzzleslib.common.api.event.v1.core.EventResult;
import fuzs.respawninganimals.common.RespawningAnimals;
import fuzs.respawninganimals.common.config.CommonConfig;
import fuzs.respawninganimals.common.config.CommonConfig.PersistenceActionsConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.level.Level;

public class AnimalPersistenceHandler {

    public static void onEndEntityTick(Entity entity) {
        if (entity.level() instanceof ServerLevel serverLevel && entity.tickCount % 20 == 0 && entity instanceof Mob mob
                && !mob.isPersistenceRequired()) {
            // Only volatile animals are marked persistent here, ignored mobs like trader llamas must not be
            // touched as this would break their own despawning logic.
            if (AnimalSpawningHandler.isAllowedToDespawn(mob, serverLevel.getGameRules())
                    && hasPersistenceAction(mob)) {
                mob.setPersistenceRequired();
            }
        }
    }

    private static boolean hasPersistenceAction(Mob mob) {
        PersistenceActionsConfig config = RespawningAnimals.CONFIG.get(CommonConfig.class).persistenceActions;
        if (config.breeding && mob instanceof Animal animal && animal.isInLove()) {
            // Animals that are in love from having their breeding item used on them.
            return true;
        } else if (config.leashed && isPlayerLeashed(mob)) {
            // Mobs with a lead attached to them by a player or a fence knot, trader llamas leashed to
            // a wandering trader are ignored.
            return true;
        } else if (config.owned && mob instanceof OwnableEntity ownable && ownable.getOwnerReference() != null) {
            // Mobs that have an owner like horses or wolves.
            return true;
        } else {
            return false;
        }
    }

    private static boolean isPlayerLeashed(Mob mob) {
        if (!mob.isLeashed()) {
            return false;
        } else {
            Entity leashHolder = mob.getLeashHolder();
            return leashHolder instanceof Player || leashHolder instanceof LeashFenceKnotEntity;
        }
    }

    public static EventResult onAnimalTame(Animal animal, Player player) {
        // Enable persistence for animals that have been tamed (cats, ocelots, wolves, and all horse types including llamas).
        if (RespawningAnimals.CONFIG.get(CommonConfig.class).persistenceActions.tamed) {
            setPersistenceForAnimal(animal);
        }

        return EventResult.PASS;
    }

    public static EventResult onStartRiding(Level level, Entity rider, Entity vehicle) {
        PersistenceActionsConfig config = RespawningAnimals.CONFIG.get(CommonConfig.class).persistenceActions;
        if (config.ridden && rider instanceof Player) {
            // Make mobs the player has ridden persistent.
            setPersistenceForAnimal(vehicle);
        } else if (config.vehicles && vehicle instanceof VehicleEntity) {
            // Make mobs entering a vehicle like a boat or minecart persistent.
            setPersistenceForAnimal(rider);
        }

        return EventResult.PASS;
    }

    private static void setPersistenceForAnimal(Entity entity) {
        if (!entity.level().isClientSide()) {
            if (entity instanceof Mob mob && mob.getType().getCategory() == MobCategory.CREATURE) {
                if (!mob.isPersistenceRequired()) {
                    mob.setPersistenceRequired();
                }
            }
        }
    }
}
