package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.AnimationComponent;
import ecs.components.PositionComponent;
import ecs.components.VelocityComponent;
import ecs.components.ai.AIComponent;
import graphic.Animation;
import tools.Point;

public class Monster extends Entity {

    /**
     * Entity with Components
     *
     * @param startPosition position at start
     */
    public Monster(Point startPosition, String pfad) {
        super();
        new PositionComponent(this, startPosition);
        new AIComponent(this);
        setupAnimationComponent(pfad);
    }

    private void setupAnimationComponent(String pfad) {
        Animation idleRight = AnimationBuilder.buildAnimation(pfad + "/idleRight");
        Animation idleLeft = AnimationBuilder.buildAnimation(pfad + "/idleLeft");
        Animation moveRight = AnimationBuilder.buildAnimation(pfad + "/runRight");
        Animation moveLeft = AnimationBuilder.buildAnimation(pfad + "/runLeft");
        new AnimationComponent(this, idleLeft, idleRight);
        new VelocityComponent(this, 0, 0, 0.1f, 0.1f, moveLeft, moveRight);
    }
}
