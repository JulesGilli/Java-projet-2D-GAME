package io.github.maingame.items;

import io.github.maingame.entities.Entity;
import io.github.maingame.entities.Player;
import io.github.maingame.core.GameStat;

public class Weapon extends Gear {
    protected int attackIncrease;

    public Weapon(int lvl) {
        super((int) (200 * Math.pow(2, lvl - 1)),
            "icons/items/weapon/icon_weapon" + lvl + ".png",
            "icons/items/weapon/icon_weapon" + lvl + "_bought.png",
            "icons/items/weapon/icon_weapon" + lvl + "_lock.png",
            lvl);
        this.attackIncrease = 5;
        this.unlockFloor = 2 * lvl;
    }

    @Override
    public void applyItem(Entity targetEntity) {
        setIncreaseValue(attackIncrease + 10 * lvl);
        targetEntity.setAttackDamage(targetEntity.getAttack() + (this).getIncreaseValue());
        System.out.println("applying weapon gear, currentAttack : " + targetEntity.getAttack());
    }

    @Override
    public void resetItem(Entity targetEntity) {
        targetEntity.setAttackDamage(targetEntity.getAttack() - getIncreaseValue());
        System.out.println("reset weapon gear, current attack : " + targetEntity.getAttack());
    }

    @Override
    public boolean isUnlocked(GameStat stat) {

        return stat.getMaxFloors() >= unlockFloor;

    }
}
