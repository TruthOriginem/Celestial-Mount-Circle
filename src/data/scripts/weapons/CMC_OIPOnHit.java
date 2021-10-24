package data.scripts.weapons;

import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;

import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class CMC_OIPOnHit implements OnHitEffectPlugin {
    private static final float MIN_DISTANCE = 100f;

    @Override
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit,
            ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        if (shieldHit) return;
        if (target.getHitpoints() <= 0) {
            engine.spawnExplosion(point, new Vector2f(), Color.white, 100f, 0.2f);
            CombatEntityAPI empTarget = null;
            float minDis = MIN_DISTANCE;
            float dis;
            List<CombatEntityAPI> entities = new ArrayList<>();
            entities.addAll(engine.getShips());
            entities.addAll(engine.getMissiles());
            for (CombatEntityAPI entity : entities) {
                if (entity.getOwner() == projectile.getOwner()) continue;
                if (entity == target) continue;
                dis = MathUtils.getDistance(entity, point);
                if (dis < minDis) {
                    minDis = dis;
                    empTarget = entity;
                }
            }
            if (empTarget != null) {
                engine.spawnEmpArcPierceShields(
                        projectile.getSource(),
                        point,
                        null,
                        empTarget,
                        DamageType.ENERGY,
                        projectile.getDamageAmount(),
                        projectile.getDamageAmount() / 2f,
                        10000f,
                        "tachyon_lance_emp_impact",
                        3f,
                        Color.white, Color.white);
            }
        }
    }
}
