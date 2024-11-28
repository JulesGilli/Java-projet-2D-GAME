package io.github.maingame.items;

import io.github.maingame.entities.Entity;
import io.github.maingame.entities.Player;
import io.github.maingame.core.GameStat;

public class StrengthPotion extends Consumable {

    public StrengthPotion(GameStat stat) {
        super(160, 30,
            "icons/items/potion/icon_potionStrength.png",
            "icons/items/potion/icon_potionStrength_bought.png",
            "icons/items/potion/icon_potionStrength_lock.png",
            stat);

        this.unlockFloor = 3;
    }

    @Override
    public void applyItem(Entity targetEntity) {
        setIncreaseValue(10 * stat.getFloors());
        System.out.println("applying strength potion, current attack bonus: " + getIncreaseValue());
        System.out.println("time duration : " + timeDuration);
        targetEntity.setAttackBonus(getIncreaseValue());
        System.out.println("Attack with bonus : " + targetEntity.getAttack());
    }

    @Override
    public void resetItem(Entity targetEntity) {
        targetEntity.setAttackBonus(0);
        System.out.println("reset strength potion, reset bonus: " + targetEntity.getAttackBonus());
    }

    @Override
    public boolean isUnlocked(GameStat stat) {
        return stat.getMaxFloors() >= unlockFloor;
    }
}
