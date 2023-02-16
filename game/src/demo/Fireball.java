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
import ecs.entities.Entity;
import graphic.Animation;
import mydungeon.ECS;
import tools.Point;

public class Fireball extends Entity{

    public Fireball(Point startPosition, Point endPosition){
        ECS.entitiestoadd.add(this);
        new PositionComponent(this, startPosition);
        Vector2 direction = getVelocityOfProjectile(startPosition,endPosition,0.5f);
        Animation move= AnimationBuilder.buildAnimation("skill/fireball");
        new AnimationComponent(this);
        new VelocityComponent(this, direction.x, direction.y, direction.x, direction.y, move);
}


    private static Vector2 getVelocityOfProjectile(Point start, Point goal, float speed){
        float x1= start.x;
        float y1= start.y;
        float x2 = goal.x;
        float y2= goal.y;

        float dx = x2 - x1;
        float dy = y2 - y1;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float velocityX = dx / distance * speed;
        float velocityY = dy / distance * speed;
        return new Vector2(velocityX,velocityY);

    }

    public static void fireball() {
        Vector3 mousePosition=ECS.dungeoncamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        new Fireball(((PositionComponent)ECS.hero.getComponent(PositionComponent.name)).getPosition(), new Point(mousePosition.x,mousePosition.y));
    }

    public static Skill Fireball(Entity entity) {
        Animation animation = AnimationBuilder.buildAnimation("skill/fireballDown");
        try {
            return new Skill(Fireball.class.getMethod("fireball"), animation, Input.Keys.Q);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }




}
