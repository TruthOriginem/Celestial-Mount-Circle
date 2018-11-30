package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.FighterWingAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import data.utils.cmc.HullModUtil;
import data.utils.cmc.I18nUtil;

import java.util.HashMap;
import java.util.Map;

import static com.fs.starfarer.api.combat.ShipAPI.HullSize;

public class CMC_HomeForSpiritsSystem extends BaseHullMod {
    private static final Map<HullSize, Float> SIZE_REDUCTION_MAP = new HashMap<>();
    private static final String ID = "cmc_homeforspirits_system";

    static {
        SIZE_REDUCTION_MAP.put(HullSize.FRIGATE, 15f);
        SIZE_REDUCTION_MAP.put(HullSize.DESTROYER, 12f);
        SIZE_REDUCTION_MAP.put(HullSize.CRUISER, 8f);
        SIZE_REDUCTION_MAP.put(HullSize.CAPITAL_SHIP, 6f);
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.hasLaunchBays()) return;
        if (ship.getAllWings().isEmpty()) return;
        float affectResult = 0f;
        for (FighterWingAPI wing : ship.getAllWings()) {
            int alive = 0;
            for (ShipAPI fighter : wing.getWingMembers()) {
                if (fighter.isAlive()) {
                    alive++;
                }
                if (fighter.getHullLevel() <= 0.8f) {
                    wing.orderReturn(fighter);
                }
            }
            int lost = wing.getSpec().getNumFighters() - alive;
            lost = Math.max(0, lost);
            //lost means fighter lost
            affectResult += lost;
        }
        float reductionPercent = affectResult * SIZE_REDUCTION_MAP.get(ship.getHullSize());
        if (reductionPercent <= 1e-6) return;
        MutableShipStatsAPI stats = ship.getMutableStats();
        stats.getFluxDissipation().modifyPercent(ID, -reductionPercent);
        stats.getAcceleration().modifyPercent(ID, -reductionPercent);
        stats.getDeceleration().modifyPercent(ID, -reductionPercent);
        stats.getMaxTurnRate().modifyPercent(ID, -reductionPercent);
        stats.getTurnAcceleration().modifyPercent(ID, -reductionPercent);
        stats.getMaxSpeed().modifyPercent(ID, -reductionPercent);

        if (Global.getCombatEngine().getPlayerShip() == ship) {
            Global.getCombatEngine().maintainStatusForPlayerShip(ID, "graphics/icons/hullsys/drone_sensor.png", "HFS System",
                    String.format(I18nUtil.getHullModString("hfsSystem"), reductionPercent + "%"), true);
        }
    }

    @Override
    public String getDescriptionParam(int index, HullSize hullSize) {
        if (index == 0) return HullModUtil.getHullSizePercentString(SIZE_REDUCTION_MAP);
        return null;
    }
}
