package io.github.maingame.items;

import io.github.maingame.entities.Entity;
import io.github.maingame.entities.Player;
import io.github.maingame.core.GameStat;

public interface ItemSolution {
    void applyItem(Entity targetEntity);

    void resetItem(Entity targetEntity);

    boolean isUnlocked(GameStat stat);
}
