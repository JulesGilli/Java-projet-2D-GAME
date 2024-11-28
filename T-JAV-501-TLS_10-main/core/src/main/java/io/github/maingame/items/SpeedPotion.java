package io.github.maingame.items;

import io.github.maingame.entities.Entity;
import io.github.maingame.entities.Player;
import io.github.maingame.core.GameStat;

public class SpeedPotion extends Consumable {
    public SpeedPotion(GameStat stat) {
        super(50, 30,
            "icons/items/potion/icon_potionSpeed.png",
            "icons/items/potion/icon_potionSpeed_bought.png",
            "icons/items/potion/icon_potionSpeed_lock.png",
            stat);

        stat.setSpeedPotionUse(stat.getSpeedPotionUse() + 1);

        this.unlockFloor = 5;
    }

    @Override
    public void applyItem(Entity targetEntity) {
        setIncreaseValue(targetEntity.getSpeed() * 0.6f);
        targetEntity.setSpeedBonus(getIncreaseValue());
        System.out.println("Speed Potion give you : " + targetEntity.getSpeedBonus() + " for " + timeDuration);
    }

    @Override
    public void resetItem(Entity targetEntity) {
        targetEntity.setSpeedBonus(0);
        System.out.println("Speed Potion reset, current speedBonus: " + targetEntity.getSpeedBonus());
    }

    @Override
    public boolean isUnlocked(GameStat stat) {
        return stat.getMaxFloors() >= 5;
    }
}
