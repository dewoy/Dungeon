package mydungeon;

import basiselements.hud.FontBuilder;
import basiselements.hud.LabelStyleBuilder;
import basiselements.hud.ScreenText;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import controller.Game;
import controller.ScreenController;
import demo.DungenObject;
import demo.Fireball;
import dslToGame.QuestConfig;
import ecs.components.PositionComponent;
import ecs.entities.Entity;
import ecs.entities.Hero;
import ecs.entities.Monster;
import ecs.systems.*;
import interpreter.DSLInterpreter;
import java.util.*;
import level.LevelAPI;
import level.elements.ILevel;
import level.elements.tile.Tile;
import level.generator.postGeneration.WallGenerator;
import level.generator.randomwalk.RandomWalkGenerator;
import level.tools.LevelElement;
import starter.DesktopLauncher;
import tools.Point;

public class ECS extends Game {

    public static Set<Entity> entities = new HashSet<>();
    public static Set<Entity> entitiestoadd = new HashSet<>();
    public static Set<Entity> entitiestoRemove = new HashSet<>();


    /** List of all Systems in the ECS */
    public static SystemController systems;


    public static ILevel currentLevel;

    private PositionComponent heroPositionComponent;
    ScreenController screenController;
    public static Hero hero;

    @Override
    protected void setup() {
        controller.clear();
        systems = new SystemController();
        controller.add(systems);
        hero = new Hero(new Point(0, 0));
        heroPositionComponent = (PositionComponent) hero.getComponent(PositionComponent.name);
        levelAPI = new LevelAPI(batch, painter, new WallGenerator(new RandomWalkGenerator()), this);
        levelAPI.loadLevel();
        new VelocitySystem();
        new DrawSystem(painter);
        new KeyboardSystem();
        new AISystem();

        screenController = new ScreenController(batch);
        controller.add(screenController);
    }

    boolean gameover = false;

    @Override
    protected void frame() {
        fireballHit();
        camera.setFocusPoint(heroPositionComponent.getPosition());
        entities.addAll(entitiestoadd);
        entitiestoadd.clear();
        entities.removeAll(entitiestoRemove);
        entitiestoRemove.clear();

        if (isOnEndTile()) levelAPI.loadLevel();
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) togglePause();

        if (!gameover) {
            collisionMonsterHero();
            if (gameover) {
                togglePause();
                printGameOver();
            }
        }
    }

    @Override
    public void onLevelLoad() {
        currentLevel = levelAPI.getCurrentLevel();

        entities.clear();
        entities.add(hero);
        heroPositionComponent.setPosition(currentLevel.getStartTile().getCoordinate().toPoint());

        String a = "chort";
        String b = "imp";
        entities.add(new Monster(currentLevel.getRandomTilePoint(LevelElement.FLOOR), a));
        entities.add(new Monster(currentLevel.getRandomTilePoint(LevelElement.FLOOR), a));
        entities.add(new Monster(currentLevel.getRandomTilePoint(LevelElement.FLOOR), a));
        entities.add(new Monster(currentLevel.getRandomTilePoint(LevelElement.FLOOR), a));
        entities.add(new Monster(currentLevel.getRandomTilePoint(LevelElement.FLOOR), b));
        entities.add(new Monster(currentLevel.getRandomTilePoint(LevelElement.FLOOR), b));
        entities.add(new Monster(currentLevel.getRandomTilePoint(LevelElement.FLOOR), b));
        entities.add(new Monster(currentLevel.getRandomTilePoint(LevelElement.FLOOR), b));
        entities.add(new Monster(currentLevel.getRandomTilePoint(LevelElement.FLOOR), b));

        String torch = "torch/";
        String chest = "chest/chest";
        entities.add(new DungenObject(currentLevel.getRandomTilePoint(LevelElement.FLOOR), torch));
        entities.add(new DungenObject(currentLevel.getRandomTilePoint(LevelElement.FLOOR), torch));
        entities.add(new DungenObject(currentLevel.getRandomTilePoint(LevelElement.FLOOR), torch));
        entities.add(new DungenObject(currentLevel.getRandomTilePoint(LevelElement.FLOOR), torch));
        entities.add(new DungenObject(currentLevel.getRandomTilePoint(LevelElement.FLOOR), torch));
        entities.add(new DungenObject(currentLevel.getRandomTilePoint(LevelElement.FLOOR), torch));
        entities.add(new DungenObject(currentLevel.getRandomTilePoint(LevelElement.FLOOR), chest));
        entities.add(new DungenObject(currentLevel.getRandomTilePoint(LevelElement.FLOOR), chest));
        entities.add(new DungenObject(currentLevel.getRandomTilePoint(LevelElement.FLOOR), chest));
        entities.add(new DungenObject(currentLevel.getRandomTilePoint(LevelElement.FLOOR), chest));

        // TODO: when calling this before currentLevel is set, the default ctor of PositionComponent
        // triggers NullPointerException
    }

    /** Toggle between pause and run */
    public static void togglePause() {
        if (systems != null) {
            systems.forEach(s -> s.toggleRun());
        }
    }

    private void fireballHit(){
        for(Entity f: entities){
            if(f instanceof Fireball){
                for(Entity m: entities){
                    if(m instanceof Monster){
                        PositionComponent fp = (PositionComponent) f.getComponent(PositionComponent.name);
                        PositionComponent mp = (PositionComponent) m.getComponent(PositionComponent.name);
                        if(currentLevel.getTileAt(fp.getPosition().toCoordinate())==currentLevel.getTileAt(mp.getPosition().toCoordinate())){
                            entitiestoRemove.add(f);
                            entitiestoRemove.add(m);
                        }
                    }
                }
            }
        }
    }
    private boolean isOnEndTile() {
        Tile currentTile =
                currentLevel.getTileAt(heroPositionComponent.getPosition().toCoordinate());
        if (currentTile.equals(currentLevel.getEndTile())) return true;

        return false;
    }

    private void setupDSLInput() {
        String program =
                """
            game_object monster {
                position_component {
                },
                velocity_component {
                x_speed: 0.1,
                y_speed: 0.1,
                move_right_animation:"monster/imp/runRight",
                move_left_animation: "monster/imp/runLeft"
                },
                animation_component{
                    idle_left: "monster/imp/idleLeft",
                    idle_right: "monster/imp/idleRight",
                    current_animation: "monster/imp/idleLeft"
                },
                ai_component {
                }
            }

            quest_config config {
                entity: monster
            }
            """;
        DSLInterpreter interpreter = new DSLInterpreter();
        QuestConfig config = (QuestConfig) interpreter.getQuestConfig(program);
        // entities.add(config.entity());
    }

    private void collisionMonsterHero() {
        Tile heroTile =
                currentLevel.getTileAt(
                        ((PositionComponent) hero.getComponent(PositionComponent.name))
                                .getPosition()
                                .toCoordinate());
        for (Entity entity : entities) {
            if (entity instanceof Monster) {
                Tile monster =
                        currentLevel.getTileAt(
                                ((PositionComponent) entity.getComponent(PositionComponent.name))
                                        .getPosition()
                                        .toCoordinate());
                if (monster == heroTile) gameover = true;
            }
        }
    }

    private void printGameOver() {
        String text = "Game Over";
        FontBuilder fb = new FontBuilder("/Users/andre/Desktop/PM-Dungeon/KartoonMovie.ttf");
        fb.setSize(80);
        LabelStyleBuilder lb = new LabelStyleBuilder(fb.build());
        lb.setFontcolor(Color.RED);
        Label.LabelStyle ls = lb.build();
        ScreenText screenText = new ScreenText(text, new Point(100, 250), 100, ls);
        screenController.add(screenText);
    }

    public static void main(String[] args) {
        // start the game
        DesktopLauncher.run(new ECS());
    }
}
