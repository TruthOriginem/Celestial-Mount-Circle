package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import data.utils.cmc.Constants;
import org.lazywizard.lazylib.MathUtils;

import java.awt.*;

public class CMC_HPShieldGenerator extends BaseHullMod {
    private static final float FIRE_RATE_PENALTY_FACTOR = 0.5f;
    private static final String ID = "cmc_hpshieldgen";
    public static final float SMOD_SHIELD_UNFOLD_BONUS = 100f;

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        // make sure the ship has a shield
        if (ship.getShield() == null) return;
        ShieldAPI shield = ship.getShield();
        // calculate the arc
        float arcFactor = shield.getActiveArc() / shield.getArc();
        float upkeepFactor = MathUtils.clamp(1f - arcFactor, 0f, 1f);

        MutableShipStatsAPI stats = ship.getMutableStats();
        stats.getEnergyRoFMult().modifyMult(ID, 1f - FIRE_RATE_PENALTY_FACTOR * arcFactor);
        stats.getBallisticRoFMult().modifyPercent(ID, 1f - FIRE_RATE_PENALTY_FACTOR * arcFactor);
        stats.getShieldUpkeepMult().modifyMult(ID, upkeepFactor);
    }

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        if (isSMod(stats)) {
            stats.getShieldUnfoldRateMult().modifyPercent(id, SMOD_SHIELD_UNFOLD_BONUS);
        }
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        Color gray = Misc.getGrayColor();
        tooltip.addPara(Constants.i18n_hullmod.get("hpsg_tips"), gray, 10f);
    }


    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0) return (int) (FIRE_RATE_PENALTY_FACTOR * 100) + "%";
        if (index == 1) return "0";
        return null;
    }

    @Override
    public String getSModDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0) return (int) (SMOD_SHIELD_UNFOLD_BONUS) + "%";
        return null;
    }
}
