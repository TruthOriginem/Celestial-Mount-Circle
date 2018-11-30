package data.missions.mayasuratest;

import com.fs.starfarer.api.combat.BattleCreationContext;
import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.combat.EscapeRevealPlugin;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;

public class MissionDefinition implements MissionDefinitionPlugin {

	public void defineMission(MissionDefinitionAPI api) {

		// Set up the fleets so we can add ships and fighter wings to them.
		// In this scenario, the fleets are attacking each other, but
		// in other scenarios, a fleet may be defending or trying to escape
		api.initFleet(FleetSide.PLAYER, "MSS", FleetGoal.ATTACK, false, 10);
		api.initFleet(FleetSide.ENEMY, "HSS", FleetGoal.ATTACK, true, 10);

		// Set a small blurb for each fleet that shows up on the mission detail and
		// mission results screens to identify each side.
		api.setFleetTagline(FleetSide.PLAYER, "Mayasurian Fleet");
		api.setFleetTagline(FleetSide.ENEMY, "Hegemony Targets");
		
		// These show up as items in the bulleted list under 
		// "Tactical Objectives" on the mission detail screen
		api.addBriefingItem("Eliminate the Hegemony fleet.");
		
		// Friendly ships
		api.addToFleet(FleetSide.PLAYER, "MSS_starliner_strike", FleetMemberType.SHIP, true);
		api.addToFleet(FleetSide.PLAYER, "MSS_nebula_missile", FleetMemberType.SHIP, true);
		api.addToFleet(FleetSide.PLAYER, "MSS_mudskipper_missile", FleetMemberType.SHIP, true);	
		api.addToFleet(FleetSide.PLAYER, "MSS_starliner2_elite", FleetMemberType.SHIP, true);
		api.addToFleet(FleetSide.PLAYER, "MSS_Zygaena_Stealth", FleetMemberType.SHIP, "MSS Apex Predator", true);
		api.addToFleet(FleetSide.PLAYER, "MSS_Pilgrim_Standard", FleetMemberType.SHIP, "MSS Crossbow", true);
		

		api.addToFleet(FleetSide.ENEMY, "colossus_Standard", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "colossus_Standard", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "colossus_Standard", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "colossus_Standard", FleetMemberType.SHIP, false);
		api.addToFleet(FleetSide.ENEMY, "colossus_Standard", FleetMemberType.SHIP, false);
	

		
		// Set up the map.
		float width = 10000f;
		float height = 10000f;
		api.initMap((float)-width/2f, (float)width/2f, (float)-height/2f, (float)height/2f);
		
		float minX = -width/2;
		float minY = -height/2;
		
		// Add an asteroid field
		api.addAsteroidField(minX, minY + height / 2, 0, 8000f,
							 20f, 70f, 100);
		
		api.addPlanet(0, 0, 50f, "star_yellow", 250f, true);
		
	}

}
