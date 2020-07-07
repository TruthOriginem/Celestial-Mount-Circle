package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import data.scripts.world.CMCWorldGen;
import exerelin.campaign.SectorManager;

import static com.fs.starfarer.api.Global.getSettings;

public class CMCModPlugin extends BaseModPlugin {
    @Override
    public void onApplicationLoad() throws Exception {
        //check if lazylib exists
        boolean hasLazyLib = getSettings().getModManager().isModEnabled("lw_lazylib");
        if (!hasLazyLib) {
            throw new RuntimeException("Celestial Mount Circle requires LazyLib!");
        }
    }

    @Override
    public void onNewGame() {
        //Nex compatibility setting, if there is no nex or corvus mode(Nex), just generate the system
        boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
        if (!haveNexerelin || SectorManager.getCorvusMode()) {
            new CMCWorldGen().generate(Global.getSector());
        }
    }
}
