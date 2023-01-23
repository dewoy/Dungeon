package ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ecs.components.PlayableComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.Skill;
import ecs.components.skill.SkillComponent;
import ecs.entities.Entity;
import java.lang.reflect.InvocationTargetException;
import mydungeon.ECS;

/** Used to controll the player */
public class KeyboardSystem extends ECS_System {

    @Override
    public void update() {
        for (Entity entity : ECS.entities) {
            if (entity.getComponent(PlayableComponent.name) != null) {
                VelocityComponent velocity =
                        (VelocityComponent) entity.getComponent(VelocityComponent.name);
                if (velocity != null) {
                    velocity.setX(0);
                    velocity.setY(0);

                    if (Gdx.input.isKeyPressed(Input.Keys.W))
                        velocity.setY(1 * velocity.getySpeed());
                    else if (Gdx.input.isKeyPressed(Input.Keys.S))
                        velocity.setY(-1 * velocity.getySpeed());
                    else if (Gdx.input.isKeyPressed(Input.Keys.D))
                        velocity.setX(1 * velocity.getxSpeed());
                    else if (Gdx.input.isKeyPressed(Input.Keys.A))
                        velocity.setX(-1 * velocity.getxSpeed());


                }
            }
        }
        SkillComponent sk = (SkillComponent) ECS.hero.getComponent(SkillComponent.name);
        for (Skill s : sk.getSkillSet()) {
            if (Gdx.input.isKeyJustPressed(s.key)) {
                try {
                    s.execute();
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
