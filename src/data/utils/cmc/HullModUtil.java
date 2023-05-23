package data.utils.cmc;

import com.fs.starfarer.api.combat.ShipAPI;

import java.util.Map;

public class HullModUtil {

    public static String getHullSizeFlatString(Map<ShipAPI.HullSize, Float> map) {
        return map.get(ShipAPI.HullSize.FRIGATE).intValue() + "/" +
                map.get(ShipAPI.HullSize.DESTROYER).intValue() + "/" +
                map.get(ShipAPI.HullSize.CRUISER).intValue() + "/" +
                map.get(ShipAPI.HullSize.CAPITAL_SHIP).intValue();
    }


    public static String getHullSizePercentString(Map<ShipAPI.HullSize, Float> map) {
        return CmcMisc.getDigitValue(map.get(ShipAPI.HullSize.FRIGATE)) + "%/" +
                CmcMisc.getDigitValue(map.get(ShipAPI.HullSize.DESTROYER)) + "%/" +
                CmcMisc.getDigitValue(map.get(ShipAPI.HullSize.CRUISER)) + "%/" +
                CmcMisc.getDigitValue(map.get(ShipAPI.HullSize.CAPITAL_SHIP)) + "%";
    }
}
