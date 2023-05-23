package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import data.scripts.world.CMCWorldGen;
import exerelin.campaign.SectorManager;

public class CMCModPlugin extends BaseModPlugin {
    @Override
    public void onNewGame() {
        //Nex compatibility setting, if there is no nex or corvus mode(Nex), just generate the system
        boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
        if (!haveNexerelin || SectorManager.getManager().isCorvusMode()) {
            new CMCWorldGen().generate(Global.getSector());
        }
    }
}
