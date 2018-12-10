package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import org.lazywizard.lazylib.MathUtils;

public class CMC_HPShieldGenerator extends BaseHullMod {
    private static final float FIRE_RATE_DECREASE = 50f;
    private static final String ID = "cmc_hpshieldgen";

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        if (ship.getShield() == null) return;
        ShieldAPI shield = ship.getShield();
        MutableShipStatsAPI stats = ship.getMutableStats();
        float arcFactor = shield.getActiveArc() / shield.getArc();
        float upkeepFactor = MathUtils.clamp(1f - arcFactor, 0f, 1f);

        stats.getEnergyRoFMult().modifyPercent(ID, -FIRE_RATE_DECREASE * arcFactor);
        stats.getBallisticRoFMult().modifyPercent(ID, -FIRE_RATE_DECREASE * arcFactor);
        stats.getMissileRoFMult().modifyPercent(ID, -FIRE_RATE_DECREASE * arcFactor);

        stats.getShieldUpkeepMult().modifyMult(ID, upkeepFactor);
    }

    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0) return (int) FIRE_RATE_DECREASE + "%";
        if (index == 1) return "0";
        return null;
    }

    /*@Override
    public boolean shouldAddDescriptionToTooltip(ShipAPI.HullSize hullSize, ShipAPI ship, boolean isForModSpec) {
        return !isForModSpec;
    }

    @Override
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float pad = 10f;
        tooltip.addSectionHeading("Testing Grid", Alignment.MID, pad);
        int col = 3;
        Color valueColor = new Color(0, 255, 100);
        tooltip.beginGrid((width - pad * 2f) / (col * col), col);
        tooltip.addToGrid(0, 0, "X0", "Y0", valueColor);
        tooltip.addToGrid(1, 0, "X1", "Y0", valueColor);
        tooltip.addToGrid(2, 0, "X2", "Y0", valueColor);
        tooltip.addToGrid(0, 1, "X0", "Y1", valueColor);
        tooltip.addToGrid(1, 1, "X1", "Y1", valueColor);
        tooltip.addToGrid(2, 1, "X2", "Y1", valueColor);
        tooltip.addToGrid(0, 2, "X0", "Y2", valueColor);
        tooltip.addToGrid(1, 2, "X1", "Y2", valueColor);
        tooltip.addToGrid(2, 2, "X2", "Y2", valueColor);
        tooltip.addGrid(pad);

        TooltipMakerAPI text = tooltip.beginImageWithText(Global.getSettings().getCommoditySpec(Commodities.ALPHA_CORE).getIconName(), 36);
        text.addPara("I think it's fu*king COOL!", 0);
        tooltip.addImageWithText(pad);

        text = tooltip.beginImageWithText(Global.getSettings().getCommoditySpec(Commodities.BETA_CORE).getIconName(), 36);
        text.addPara("I think so.", 0);
        tooltip.addImageWithText(pad);

        text = tooltip.beginImageWithText(Global.getSettings().getCommoditySpec(Commodities.GAMMA_CORE).getIconName(), 36);
        text.addPara("NO I HAVE THE OPPOSITE OPINION!!!", 0);
        tooltip.addImageWithText(pad);

        tooltip.addSectionHeading("Then Gamma core turns into...", Alignment.MID, pad);

        CargoAPI debris = Global.getFactory().createCargo(true);
        debris.addCommodity(Commodities.METALS, 40f);
        debris.addCommodity(Commodities.RARE_METALS, 20f);
        debris.addCommodity(Commodities.ORE, 10f);
        debris.addCommodity(Commodities.RARE_ORE, 5f);
        debris.addCommodity(Commodities.HEAVY_MACHINERY, 5f);

        tooltip.showCargo(debris, 6, true, pad);

        tooltip.addPara("...Poor Gamma Core.", pad);

    }*/
}
