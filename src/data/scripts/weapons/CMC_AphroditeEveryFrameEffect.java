package data.scripts.weapons;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.loading.MuzzleFlashSpec;
import org.lazywizard.lazylib.MathUtils;

public class CMC_AphroditeEveryFrameEffect implements EveryFrameWeaponEffectPlugin {
    private static final float MAX_INCREASE_FACTOR = 0.75f;
    private static final float CHARGE_UP_NEED_TIME = 2f;
    private static final float CHARGE_DOWN_NEED_TIME = 1.5f;

    //EveryFrameWeaponEffectPlugin would be generated for each weapon that implement it, so the local variable could be used correctly
    private float increaseFactor = 0;
    private boolean init = false;
    private MuzzleFlashSpec originMuzzleFlash;

    @Override
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        if (weapon.getShip().getOriginalOwner() == -1 || weapon.getShip().isHulk()) {
            return;
        }
        if (!init) {
            init = true;
            //record the muzzle flash spec
            originMuzzleFlash = weapon.getMuzzleFlashSpec().clone();
            //make sure weapon has its own spec(weapon spec is shared with each weapon if you don't ensure it is cloned)
            weapon.ensureClonedSpec();
        }
        float coolDownRemain = weapon.getCooldownRemaining();
        if (!weapon.isFiring() && weapon.getChargeLevel() <= 0) {
            increaseFactor -= amount / CHARGE_DOWN_NEED_TIME;
        } else {
            increaseFactor += amount / CHARGE_UP_NEED_TIME;
        }
        increaseFactor = MathUtils.clamp(increaseFactor, 0f, 1f);

        if (coolDownRemain > 0) {
            coolDownRemain -= increaseFactor * amount * MAX_INCREASE_FACTOR;
            coolDownRemain = MathUtils.clamp(coolDownRemain, 0f, 1f);
            weapon.setRemainingCooldownTo(coolDownRemain);
        }

        weapon.getMuzzleFlashSpec().setLength(originMuzzleFlash.getLength() * (1f + increaseFactor * MAX_INCREASE_FACTOR));
    }
}
