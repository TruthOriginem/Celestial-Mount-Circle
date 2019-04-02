package data.missions.cmctest;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

import java.util.ArrayList;
import java.util.List;

public class MissionDefinition implements MissionDefinitionPlugin {

    public void defineMission(MissionDefinitionAPI api) {

        api.initFleet(FleetSide.PLAYER, "CMC", FleetGoal.ATTACK, false, 10);
        api.initFleet(FleetSide.ENEMY, "CMC2", FleetGoal.ATTACK, true, 10);

        api.setFleetTagline(FleetSide.PLAYER, "CMC Fleet");
        api.setFleetTagline(FleetSide.ENEMY, "Mirrors");

        api.addBriefingItem("Eliminate them.");
        List<String> ids = new ArrayList<>();
        List<String> capitalships = new ArrayList<>();
        List<String> cruiserships = new ArrayList<>();
        List<String> destoryerships = new ArrayList<>();
        List<String> frigateships = new ArrayList<>();
        //To show All CMC ships
        for (String variantID : Global.getSettings().getAllVariantIds()) {
            if (variantID.endsWith("_wing")) {
                continue;
            }
            //show all ship which id prefix is "cmc"
            if (!variantID.startsWith("cmc")) {
                continue;
            }
            FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, variantID);
            //such variant id means it's an empty variant
            String id = member.getHullId() + "_Hull";
            if (ids.contains(id)) {
                continue;
            }
            ids.add(id);
            switch (member.getHullSpec().getHullSize()) {
                case CAPITAL_SHIP:
                    capitalships.add(id);
                    break;
                case CRUISER:
                    cruiserships.add(id);
                    break;
                case DESTROYER:
                    destoryerships.add(id);
                    break;
                case FRIGATE:
                    frigateships.add(id);
                    break;
                default:
                    break;
            }
        }
        boolean isFlagShipset = false;

        //Add them...well, copy them without any thinking is okay
        for (String id : capitalships) {
            if (!isFlagShipset) {
                api.addToFleet(FleetSide.PLAYER, id, FleetMemberType.SHIP, true);
                isFlagShipset = true;
            } else {
                api.addToFleet(FleetSide.PLAYER, id, FleetMemberType.SHIP, false);
            }
            api.addToFleet(FleetSide.ENEMY, id, FleetMemberType.SHIP, false);
        }
        for (String id : cruiserships) {
            api.addToFleet(FleetSide.PLAYER, id, FleetMemberType.SHIP, false);
            api.addToFleet(FleetSide.ENEMY, id, FleetMemberType.SHIP, false);
        }
        for (String id : destoryerships) {
            api.addToFleet(FleetSide.PLAYER, id, FleetMemberType.SHIP, false);
            api.addToFleet(FleetSide.ENEMY, id, FleetMemberType.SHIP, false);
        }
        for (String id : frigateships) {
            api.addToFleet(FleetSide.PLAYER, id, FleetMemberType.SHIP, false);
            api.addToFleet(FleetSide.ENEMY, id, FleetMemberType.SHIP, false);
        }

        // Set up the map.
        float width = 10000f;
        float height = 10000f;
        api.initMap(-width / 2f, width / 2f, -height / 2f, height / 2f);

        float minX = -width / 2;
        float minY = -height / 2;

        // Add an asteroid field
        api.addAsteroidField(minX, minY + height / 2, 0, 8000f,
                20f, 70f, 100);

        api.addPlanet(0, 0, 50f, "star_yellow", 250f, true);

    }

}
