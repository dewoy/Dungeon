package ecs.entities;

import ecs.components.PositionComponent;

public class Goblin extends Entity{
    public Goblin(){
        super();
        new PositionComponent(this);

    }
}
