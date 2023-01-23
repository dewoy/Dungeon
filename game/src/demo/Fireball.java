package demo;

import com.badlogic.gdx.Input;
import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.PositionComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.Skill;
import ecs.components.skill.SkillComponent;
import ecs.entities.Entity;
import graphic.Animation;
import mydungeon.ECS;
import tools.Point;

public class Fireball extends Entity{

    public Fireball(Point startPosition,int direction){
        ECS.entitiestoadd.add(this);
        new PositionComponent(this, startPosition);
        setupAnimationComponent(direction);
}

    private void setupAnimationComponent(int direction) {
        String path="";

        switch (direction){
            case 0: path="fireballRight"; break;
            case 1: path="fireballLeft"; break;
            case 2: path="fireballUp"; break;
            case 3: path="fireballDown"; break;
        }

        Animation idleRight = AnimationBuilder.buildAnimation(path);
        Animation moveRight = AnimationBuilder.buildAnimation(path);
        Animation moveLeft = AnimationBuilder.buildAnimation(path);

        new AnimationComponent(this, idleRight);

        switch (direction){
            case 0: new VelocityComponent(this, 0.5f, 0, 0.5f, 0.0f, moveLeft, moveRight); break;
            case 1: new VelocityComponent(this, -0.5f, 0, -0.5f, 0.0f, moveLeft, moveRight); break;
            case 2: new VelocityComponent(this, 0.0f, 0.5f, 0.0f, 0.5f, moveLeft, moveRight); break;
            case 3: new VelocityComponent(this, 0.0f, -0.5f, 0.0f, -0.5f, moveLeft, moveRight); break;
        }

    }

    public static void fbRight() {
        new Fireball(((PositionComponent)ECS.hero.getComponent(PositionComponent.name)).getPosition(),0);
    }
    public static void fbLeft() {
        new Fireball(((PositionComponent)ECS.hero.getComponent(PositionComponent.name)).getPosition(),1);
    }
    public static void fbUp() {
        new Fireball(((PositionComponent)ECS.hero.getComponent(PositionComponent.name)).getPosition(),2);
    }
    public static void fbDown() {
        new Fireball(((PositionComponent)ECS.hero.getComponent(PositionComponent.name)).getPosition(),3);
    }

    public static Skill FireballLEFT(Entity entity) {
        Animation animation = AnimationBuilder.buildAnimation("skill/fireballRight");
        try {
            return new Skill(Fireball.class.getMethod("fbLeft"), animation, Input.Keys.H);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Skill FireballRight(Entity entity) {
        Animation animation = AnimationBuilder.buildAnimation("skill/fireballRight");
        try {
            return new Skill(Fireball.class.getMethod("fbRight"), animation, Input.Keys.K);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Skill FireballDown(Entity entity) {
        Animation animation = AnimationBuilder.buildAnimation("skill/fireballRight");
        try {
            return new Skill(Fireball.class.getMethod("fbDown"), animation, Input.Keys.J);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Skill FireballUp(Entity entity) {
        Animation animation = AnimationBuilder.buildAnimation("skill/fireballRight");
        try {
            return new Skill(Fireball.class.getMethod("fbUp"), animation, Input.Keys.U);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
