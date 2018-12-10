package data.utils.cmc;

import com.fs.starfarer.api.Global;

public class I18nUtil {
    private static final String CATE_SHIP_SYSTEM = "shipSystem";
    private static final String CATE_STAR_SYSTEMS = "starSystems";
    private static final String CATE_HULL_MOD = "hullMod";

    public static String getString(String category, String id) {
        return Global.getSettings().getString(category, id);
    }

    public static String getShipSystemString(String id) {
        return getString(CATE_SHIP_SYSTEM, id);
    }

    public static String getStarSystemsString(String id) {
        return getString(CATE_STAR_SYSTEMS, id);
    }

    public static String getHullModString(String id) {
        return getString(CATE_HULL_MOD, id);
    }
}
