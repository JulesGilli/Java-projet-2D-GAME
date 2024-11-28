package fictitiousClass;

import io.github.maingame.core.GameStat;
import io.github.maingame.entities.Entity;
import io.github.maingame.items.Item;

public class TestItem extends Item {

    public TestItem(int value, String textureName, String textureDisabledName, String textureLockName) {
        super(value, textureName, textureDisabledName, textureLockName);
    }

    @Override
    public void applyItem(Entity targetEntity) {

    }

    @Override
    public void resetItem(Entity targetEntity) {

    }

    @Override
    public boolean isUnlocked(GameStat stat) {
        return false;
    }
}
