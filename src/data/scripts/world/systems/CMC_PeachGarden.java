package data.scripts.world.systems;

import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.impl.campaign.ids.Terrain;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.SalvageSpecialAssigner;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import data.utils.cmc.I18nUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin.DebrisFieldParams;
import static com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin.DebrisFieldSource;
import static data.scripts.world.CMCWorldGen.addMarketplace;

public class CMC_PeachGarden {


    public void generate(SectorAPI sector) {
        StarSystemAPI system = sector.createStarSystem("Peach Garden");
        system.getLocation().set(-14200f, -10000f);
        system.setBackgroundTextureFilename("graphics/backgrounds/background6.jpg");

        PlanetAPI pg_Star = system.initStar("TaoHuaYuan", "star_orange", 600f, 350f);
        system.setLightColor(new Color(255, 185, 50));

        system.addAsteroidBelt(pg_Star, 100, 2200f, 150f, 180, 360, Terrain.ASTEROID_BELT, "");

        PlanetAPI yuanming = system.addPlanet("cmc_planet_yuanming", pg_Star, I18nUtil.getStarSystemsString("planet_name_yuanming"), "terran", 215, 120f, 4500f, 365f);

        MarketAPI yuanmingMarket = addMarketplace("cmc", yuanming, null
                , yuanming.getName(), 6,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_7,
                                Conditions.HABITABLE,
                                Conditions.FARMLAND_BOUNTIFUL,
                                Conditions.MILD_CLIMATE,
                                Conditions.RUINS_WIDESPREAD
                        )),
                new ArrayList<>(
                        Arrays.asList(
                                Submarkets.GENERIC_MILITARY,
                                Submarkets.SUBMARKET_BLACK,
                                Submarkets.SUBMARKET_OPEN,
                                Submarkets.SUBMARKET_STORAGE
                        )),
                new ArrayList<>(
                        Arrays.asList(
                                Industries.POPULATION,
                                Industries.MEGAPORT,
                                Industries.STARFORTRESS_MID,
                                Industries.FARMING,
                                Industries.FUELPROD,
                                Industries.PATROLHQ,
                                Industries.ORBITALWORKS,
                                Industries.WAYSTATION,
                                Industries.TECHMINING,
                                Industries.HEAVYBATTERIES
                        )),
                0.3f,
                false,
                true);
        yuanming.setCustomDescriptionId("cmc_planet_yuanming");

        PlanetAPI mingyue = system.addPlanet("cmc_planet_mingyue", yuanming, I18nUtil.getStarSystemsString("planet_name_mingyue"), "barren", 90, 60, 1000f, 30);

        MarketAPI mingyueMarket = addMarketplace("cmc", mingyue, null
                , mingyue.getName(), 6,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_5,
                                Conditions.ORE_MODERATE,
                                Conditions.RARE_ORE_RICH,
                                Conditions.VOLATILES_PLENTIFUL
                        )),
                new ArrayList<>(
                        Arrays.asList(
                                Submarkets.SUBMARKET_BLACK,
                                Submarkets.SUBMARKET_OPEN,
                                Submarkets.SUBMARKET_STORAGE
                        )),
                new ArrayList<>(
                        Arrays.asList(
                                Industries.POPULATION,
                                Industries.SPACEPORT,
                                Industries.MINING,
                                Industries.HEAVYINDUSTRY,
                                Industries.LIGHTINDUSTRY,
                                Industries.REFINING
                        )),
                0.3f,
                true,
                true);
        yuanming.setCustomDescriptionId("cmc_planet_mingyue");

        // generates hyperspace destinations for in-system jump points
        system.autogenerateHyperspaceJumpPoints(true, true);

        // Debris for the Lost Astropolis
        DebrisFieldParams params = new DebrisFieldParams(
                150f, // field radius - should not go above 1000 for performance reasons
                1f, // density, visual - affects number of debris pieces
                10000000f, // duration in days
                0f); // days the field will keep generating glowing pieces
        params.source = DebrisFieldSource.MIXED;
        params.baseSalvageXP = 500; // base XP for scavenging in field
        SectorEntityToken debris = Misc.addDebrisField(system, params, StarSystemGenerator.random);
        SalvageSpecialAssigner.assignSpecialForDebrisField(debris);

        // makes the debris field always visible on map/sensors and not give any xp or notification on being discovered
        debris.setSensorProfile(null);
        debris.setDiscoverable(null);

        // makes it discoverable and give 200 xp on being found
        // sets the range at which it can be detected (as a sensor contact) to 2000 units
        // commented out.
        //debris.setDiscoverable(true);
        //debris.setDiscoveryXP(200f);
        //debris.setSensorProfile(1f);
        //debris.getDetectedRangeMod().modifyFlat("gen", 2000);

        debris.setCircularOrbit(pg_Star, 45 + 10, 1600, 250);
        //Finally cleans up hyperspace
        cleanup(system);

    }

    //Learning from Tart scripts
    private void cleanup(StarSystemAPI system) {
        HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin();
        NebulaEditor editor = new NebulaEditor(plugin);
        float minRadius = plugin.getTileSize() * 2f;

        float radius = system.getMaxRadiusInHyperspace();
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius * 0.5f, 0, 360f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0, 360f, 0.25f);
    }
}
