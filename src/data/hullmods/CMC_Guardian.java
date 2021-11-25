package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;

public class CMC_Guardian extends BaseHullMod {
    private static final float HITPOINTS_BONUS = 10f;
    private static final float PROFILE_REDUCTION = 25f;

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        //Percent 10f -> 10%
        stats.getHullBonus().modifyPercent(id, HITPOINTS_BONUS);
        stats.getArmorBonus().modifyPercent(id, HITPOINTS_BONUS);
        stats.getSensorProfile().modifyPercent(id, -PROFILE_REDUCTION);
    }

    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0) return (int) HITPOINTS_BONUS + "%";
        if (index == 1) return (int) PROFILE_REDUCTION + "%";
        return null;
    }
}
