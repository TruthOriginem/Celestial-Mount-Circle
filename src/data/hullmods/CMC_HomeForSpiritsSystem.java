package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.WeaponRangeModifier;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.utils.cmc.Constants;
import data.utils.cmc.HullModUtil;

import java.awt.*;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.fs.starfarer.api.combat.ShipAPI.HullSize;
import static data.utils.cmc.Constants.FUNNEL_ID;

public class CMC_HomeForSpiritsSystem extends BaseHullMod {
    // Could use Hashmap here...But we could also use EnumMap for better performance!
    private static final Map<HullSize, Float> SIZE_REDUCTION_MAP = new EnumMap<>(HullSize.class);
    private static final float REDUCTION_THRESHOLD = 30f;
    private static final String ID = "cmc_homeforspirits_system";
    public static final float DEM_DAMAGE_BONUS = 20f;
    public static final float SMOD_CAP_FACTOR = 0.5f;

    static {
        SIZE_REDUCTION_MAP.put(HullSize.FRIGATE, 15f);
        SIZE_REDUCTION_MAP.put(HullSize.DESTROYER, 12f);
        SIZE_REDUCTION_MAP.put(HullSize.CRUISER, 8f);
        SIZE_REDUCTION_MAP.put(HullSize.CAPITAL_SHIP, 6f);
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        // increases dem damage
        List<ShipAPI> ships = Global.getCombatEngine().getShips();
        for (ShipAPI demDrone : ships) {
            if (!demDrone.isDrone()) continue;
            // look for dem drones launched by this ship
            if (demDrone.getAIFlags() != null && ship == demDrone.getAIFlags().getCustom(ShipwideAIFlags.AIFlags.DRONE_MOTHERSHIP)) {
                if (demDrone.getOwner() == ship.getOwner()) {
                    MutableShipStatsAPI demStats = demDrone.getMutableStats();
                    demStats.getEnergyWeaponDamageMult().modifyPercent(ID, DEM_DAMAGE_BONUS);
                    demStats.getMissileWeaponDamageMult().modifyPercent(ID, DEM_DAMAGE_BONUS);
                    demStats.getBallisticWeaponDamageMult().modifyPercent(ID, DEM_DAMAGE_BONUS);
                }
            }
        }
        if (!ship.hasLaunchBays()) return;
        if (ship.getAllWings().isEmpty()) return;
        float affectResult = 0f;
        //get all wings
        for (FighterWingAPI wing : ship.getAllWings()) {
            // if it's not the funnel, skip
            if (!wing.getWingId().startsWith(FUNNEL_ID)) continue;
            int alive = 0;
            for (ShipAPI fighter : wing.getWingMembers()) {
                if (fighter.isAlive()) {
                    alive++;
                }
                // call them back while hp is under 80%
                if (!wing.isReturning(fighter) && fighter.getHullLevel() <= 0.8f) {
                    wing.orderReturn(fighter);
                }
            }
            int lost = wing.getSpec().getNumFighters() - alive;
            lost = Math.max(0, lost);
            //lost means fighter lost
            affectResult += lost;
        }
        float reductionPercentage = Math.min(affectResult * SIZE_REDUCTION_MAP.get(ship.getHullSize()),
                                             REDUCTION_THRESHOLD * (isSMod(ship) ? SMOD_CAP_FACTOR : 1f));
        //Float is not always correct, sometimes <=1e-6 means ==0
        if (reductionPercentage <= 1e-6) return;
        MutableShipStatsAPI stats = ship.getMutableStats();
        stats.getFluxDissipation().modifyPercent(ID, -reductionPercentage);
        stats.getAcceleration().modifyPercent(ID, -reductionPercentage);
        stats.getDeceleration().modifyPercent(ID, -reductionPercentage);
        stats.getMaxTurnRate().modifyPercent(ID, -reductionPercentage);
        stats.getTurnAcceleration().modifyPercent(ID, -reductionPercentage);
        stats.getMaxSpeed().modifyPercent(ID, -reductionPercentage);

        // Shows the status thing if player's ship has this hullmod
        if (Global.getCombatEngine().getPlayerShip() == ship) {
            Global.getCombatEngine().maintainStatusForPlayerShip(ID + "_0", "graphics/icons/hullsys/drone_sensor.png",
                                                                 Constants.i18n_hullmod.get("hfsSystem_title"),
                                                                 Constants.i18n_hullmod.get("hfsSystem_0") + " - " + reductionPercentage + "%", true);
            Global.getCombatEngine().maintainStatusForPlayerShip(ID + "_1", "graphics/icons/hullsys/drone_sensor.png",
                                                                 Constants.i18n_hullmod.get("hfsSystem_title"),
                                                                 Constants.i18n_hullmod.get("hfsSystem_1") + " - " + reductionPercentage + "%", true);
        }
    }

    private static final String FUNNEL_SPAWN_KEY = "cmc_funnel_spawnTimes";

    @Override
    public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
        // check spirit-force
        if (isSpiritForceFunnel(fighter)) {
            // Add listener to fighter
            // check if the fighter has the listener
            if (!fighter.hasListenerOfClass(SpiritForceRangeModifier.class)) {
                fighter.addListener(new SpiritForceRangeModifier());
            }
            boolean removeIonCannon = true;
            // check if mother ship has spirit alternatives mod
            if (ship.getVariant().hasHullMod("cmc_spiritalter")) {
                Integer spawnTimes = (Integer) ship.getCustomData().get(FUNNEL_SPAWN_KEY);
                if (spawnTimes == null) {
                    spawnTimes = 0;
                }
                spawnTimes++;
                // weapon would be replaced every 2 fighter
                if (spawnTimes >= 2) {
                    spawnTimes = 0;
                    WeaponGroupAPI group = fighter.getWeaponGroupsCopy().get(0);
                    // remove phase lance
                    // 2 weapons are in one group, 0 for phase lance, 1 for ion cannon
                    group.removeWeapon(0);
                    removeIonCannon = false;
                }
                ship.setCustomData(FUNNEL_SPAWN_KEY, spawnTimes);
            }
            // remove ion cannon weapon
            if (removeIonCannon) {
                WeaponGroupAPI group = fighter.getWeaponGroupsCopy().get(0);
                group.removeWeapon(1);
            }
        }
    }

    public static boolean isSpiritForceFunnel(ShipAPI ship) {
        return ship.getHullSpec().getBaseHullId().equalsIgnoreCase(FUNNEL_ID);
    }

    /**
     * Make the funnel has the average range of the mother ship
     */
    private static class SpiritForceRangeModifier implements WeaponRangeModifier {
        private static final float BASE_RANGE = 600f;

        @Override
        public float getWeaponRangePercentMod(ShipAPI ship, WeaponAPI weapon) {
            return 0;
        }

        @Override
        public float getWeaponRangeMultMod(ShipAPI ship, WeaponAPI weapon) {
            return 1;
        }

        @Override
        public float getWeaponRangeFlatMod(ShipAPI ship, WeaponAPI weapon) {
            ShipAPI motherShip = ship.getWing().getSourceShip();
            if (motherShip != null) {
                float totalRange = 0f;
                float count = 0;
                for (WeaponAPI motherWeapon : motherShip.getAllWeapons()) {
                    // remember do not count these
                    if (motherWeapon.isDecorative()) continue;
                    if (motherWeapon.getType().equals(WeaponAPI.WeaponType.MISSILE)) continue;
                    if (motherWeapon.getSpec().getAIHints().contains(WeaponAPI.AIHints.PD)) continue;
                    totalRange += motherWeapon.getRange();
                    count++;
                }
                if (count > 0) {
                    float avgRange = totalRange / count + ship.getCollisionRadius();
                    return Math.max(avgRange, BASE_RANGE);
                }
            }
            // the original weapon (low energy phase lance) does not have range
            return BASE_RANGE;
        }
    }


    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return HullModUtil.getHullSizePercentString(SIZE_REDUCTION_MAP);
        if (index == 1) return (int) REDUCTION_THRESHOLD + "%";
        if (index == 2) return (int) DEM_DAMAGE_BONUS + "%";
        return null;
    }

    @Override
    public String getSModDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return SMOD_CAP_FACTOR + "x";
        return null;
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        Color gray = Misc.getGrayColor();
        tooltip.addPara(Constants.i18n_hullmod.get("hfs_tips"), gray, 10f);
    }
}
