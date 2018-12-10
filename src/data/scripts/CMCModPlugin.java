package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import data.scripts.world.CMCWorldGen;

import static com.fs.starfarer.api.Global.getSettings;

public class CMCModPlugin extends BaseModPlugin {
    @Override
    public void onApplicationLoad() throws Exception {
        boolean hasLazyLib = getSettings().getModManager().isModEnabled("lw_lazylib");
        if (!hasLazyLib) {
            throw new RuntimeException("Celestial Mount Circle requires LazyLib!");
        }
    }

    @Override
    public void onNewGame() {
//        boolean haveNexerelin = Global.getSettings().getModManager().isModEnabled("nexerelin");
//        if (!haveNexerelin || SectorManager.getCorvusMode()){
//            new DiableavionicsGen().generate(Global.getSector());
//        }
        new CMCWorldGen().generate(Global.getSector());
    }
}
