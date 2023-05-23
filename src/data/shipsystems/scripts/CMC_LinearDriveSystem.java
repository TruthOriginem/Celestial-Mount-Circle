package data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;

import static data.utils.cmc.Constants.i18n_shipSystem;

public class CMC_LinearDriveSystem extends BaseShipSystemScript {

    public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
        if (state == ShipSystemStatsScript.State.OUT) {
            stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
        } else {
            stats.getMaxSpeed().modifyPercent(id, 100f * effectLevel);
            stats.getMaxSpeed().modifyFlat(id, 50f * effectLevel);
            stats.getAcceleration().modifyFlat(id, 1200f * effectLevel);
        }
    }

    public void unapply(MutableShipStatsAPI stats, String id) {
        stats.getMaxSpeed().unmodify(id);
        stats.getAcceleration().unmodify(id);
    }

    public StatusData getStatusData(int index, State state, float effectLevel) {
        if (index == 0) {
            return new StatusData(i18n_shipSystem.get("linearDriveData0"), false);
        }
        return null;
    }

    public int getUsesOverride(ShipAPI ship) {
        if (ship.getHullSize() == HullSize.FRIGATE) {
            return 2;
        }
        if (ship.getHullSize() == HullSize.DESTROYER) {
            return 2;
        }
        if (ship.getHullSize() == HullSize.CRUISER) {
            return 2;
        }
        return -1;
    }
}