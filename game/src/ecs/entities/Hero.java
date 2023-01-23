package ecs.entities;

import demo.Fireball;
import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.AnimationComponent;
import ecs.components.PositionComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.SkillComponent;
import graphic.Animation;
import tools.Point;

public class Hero extends Entity {

    /**
     * Entity with Components
     *
     * @param startPosition position at start
     */
    public Hero(Point startPosition) {
        super();
        new PositionComponent(this, startPosition);
        new PlayableComponent(this);
        setupAnimationComponent();

        SkillComponent sk = new SkillComponent(this);
        sk.addSkill(Fireball.FireballLEFT(this));
        sk.addSkill(Fireball.FireballRight(this));
        sk.addSkill(Fireball.FireballUp(this));
        sk.addSkill(Fireball.FireballDown(this));
    }

    private void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation("knight/idleRight");
        Animation idleLeft = AnimationBuilder.buildAnimation("knight/idleLeft");
        Animation moveRight = AnimationBuilder.buildAnimation("knight/runRight");
        Animation moveLeft = AnimationBuilder.buildAnimation("knight/runLeft");
        ;

        new AnimationComponent(this, idleLeft, idleRight);

        new VelocityComponent(this, 0, 0, 0.3f, 0.3f, moveLeft, moveRight);
    }
}
