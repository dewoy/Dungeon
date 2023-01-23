package demo;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.PositionComponent;
import ecs.entities.Entity;
import graphic.Animation;
import tools.Point;

public class DungenObject extends Entity {

    public DungenObject(Point startPosition, String path) {
        super();
        new PositionComponent(this, startPosition);
        setupAnimationComponent(path);
    }

    private void setupAnimationComponent(String path) {
        Animation idle = AnimationBuilder.buildAnimation(path);
        new AnimationComponent(this, idle);
    }
}
