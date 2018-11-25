package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;

import static com.fs.starfarer.api.Global.getSettings;

public class CMCModPlugin extends BaseModPlugin {
    @Override
    public void onApplicationLoad() throws Exception {
        boolean hasLazyLib = getSettings().getModManager().isModEnabled("lw_lazylib");
        if (!hasLazyLib) {
            throw new RuntimeException("Celestial Mount Circle requires LazyLib!");
        }
    }
}
