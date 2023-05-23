package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;

/**
 * The actual code is in {@link CMC_HomeForSpiritsSystem#applyEffectsToFighterSpawnedByShip(ShipAPI, ShipAPI, String)}
 */
public class CMC_SpiritAlternatives extends BaseHullMod {

    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0) return "50%";
        if (index == 1) return Global.getSettings().getWeaponSpec("cmc_leic").getWeaponName();
        return null;
    }
}
