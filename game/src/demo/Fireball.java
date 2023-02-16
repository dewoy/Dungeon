package demo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.PositionComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.Skill;
import ecs.components.skill.SkillComponent;
import ecs.entities.Entity;
import ecs.entities.Hero;
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
            case 0: {
                Vector2 p = getVelcoity();
                new VelocityComponent(this, p.x, p.y, p.x, p.y, moveLeft, moveRight);

                break;
            }




            case 1: new VelocityComponent(this, -0.5f, 0, -0.5f, 0.0f, moveLeft, moveRight); break;
            case 2: new VelocityComponent(this, 0.0f, 0.5f, 0.0f, 0.5f, moveLeft, moveRight); break;
            case 3: new VelocityComponent(this, 0.0f, -0.5f, 0.0f, -0.5f, moveLeft, moveRight); break;
        }

    }
    private static Vector2 getVelcoity(){

        Vector3 mousePosition=ECS.dungeoncamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        Point pc = ((PositionComponent) ECS.hero.getComponent(PositionComponent.name)).getPosition();
        float x1= pc.x;
        float y1= pc.y;
        float x2 = mousePosition.x;
        float y2= mousePosition.y;

        // assume Point A is (x1, y1) and Point B is (x2, y2)
        float dx = x2 - x1;
        float dy = y2 - y1;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float speed = 0.1f;
        float velocityX = dx / distance * speed;
        float velocityY = dy / distance * speed;
        return new Vector2(velocityX,velocityY);

    }
    private static float getMouseY(){
        PositionComponent pc = (PositionComponent) ECS.hero.getComponent(PositionComponent.name);
        float mouseY = Gdx.input.getY();
        if(mouseY>pc.getPosition().y)
            return 0.5f;
        else return -0.5f;
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
