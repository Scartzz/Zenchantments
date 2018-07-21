package zedly.zenchantments.enchantments;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import zedly.zenchantments.CustomEnchantment;
import zedly.zenchantments.arrows.EnchantedArrow;
import zedly.zenchantments.arrows.enchanted.SiphonArrow;
import zedly.zenchantments.enums.Hand;
import zedly.zenchantments.enums.Tool;

import static zedly.zenchantments.enums.Tool.BOW;
import static zedly.zenchantments.enums.Tool.SWORD;

public class Siphon extends CustomEnchantment {

    @Override
    public Builder<Siphon> defaults() {
        return new Builder<>(Siphon::new, 53)
            .maxLevel(4)
            .loreName("Siphon")
            .probability(0)
            .enchantable(new Tool[]{BOW, SWORD})
            .conflicting(new Class[]{})
            .description("Drains the health of the mob that you attack, giving it to you")
            .cooldown(0)
            .power(1.0)
            .handUse(Hand.BOTH);
    }

    @Override
    public boolean onEntityHit(EntityDamageByEntityEvent evt, int level, boolean usedHand) {
        if(evt.getEntity() instanceof LivingEntity &&
           ADAPTER.attackEntity((LivingEntity) evt.getEntity(), (Player) evt.getDamager(), 0)) {
            Player p = (Player) evt.getDamager();
            LivingEntity ent = (LivingEntity) evt.getEntity();
            int difference = (int) Math.round(.17 * level * power * evt.getDamage());
            while (difference > 0) {
                if (p.getHealth() <= 19) {
                    p.setHealth(p.getHealth() + 1);
                }
                difference--;
            }
        }
        return true;
    }

    @Override
    public boolean onEntityShootBow(EntityShootBowEvent evt, int level, boolean usedHand) {
        SiphonArrow arrow = new SiphonArrow((Arrow) evt.getProjectile(), level, power);
        EnchantedArrow.putArrow((Arrow) evt.getProjectile(), arrow, (Player) evt.getEntity());
        return true;
    }
}
