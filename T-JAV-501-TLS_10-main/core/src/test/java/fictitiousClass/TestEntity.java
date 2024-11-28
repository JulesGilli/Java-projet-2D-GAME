package fictitiousClass;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.maingame.entities.Entity;

public class TestEntity extends Entity {

    public TestEntity(Vector2 position, float health, int gold, float attack) {
        super(position, null, health, gold, attack);
    }

    public void render(SpriteBatch batch) {

        // Implémentation vide pour les tests
    }
}
