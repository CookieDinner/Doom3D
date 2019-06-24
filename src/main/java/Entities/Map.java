package Entities;

import Engine.CollisionUnit;
import Engine.ShaderProgram;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class Map {
    public Map(){}

    private List<Entity> walls = new ArrayList<>();
    private List<Model> ceilings = new ArrayList<>();

    private int numOfWalls = 86;
    private int numOfCeilings = 4;

    public void load(CollisionUnit collisionUnit, ShaderProgram shader1, ShaderProgram shader2){
        loadWalls(collisionUnit, shader1, shader2);
        loadCeilings(shader1, shader2);
    }

    private void loadWalls(CollisionUnit collisionUnit,ShaderProgram shader1, ShaderProgram shader2){
        String wallname;
        for (int i = 1; i <= numOfWalls; i++) {
                wallname = "walls/" + i + ".obj";
                walls.add(new Entity(0.0f, 0.0f, new Model(shader1, shader2, wallname, "metal_plate_base.png", "metal_plate_diff.png", "black.png")));
                collisionUnit.addToList(walls.get(i - 1));
        }

    }

    private void loadCeilings(ShaderProgram shader1, ShaderProgram shader2){

        String ceilname;
        for (int i = 1; i <= numOfCeilings; i++){
            ceilname = "ceilings/" + i + ".obj";
            ceilings.add(new Model(shader1,shader2, ceilname, "metal_grill.png", "metal_grill_diff.png", "black.png"));
        }
    }
    public void draw(ShaderProgram shader1, ShaderProgram shader2, Matrix4f M, Matrix4f V, Matrix4f P){
        drawWalls(M,V,P);
        drawCeilings(M,V,P);
    }
    private void drawWalls(Matrix4f M, Matrix4f V, Matrix4f P){
        for (int i = 1; i <= numOfWalls; i++)
            walls.get(i-1).getModel().draw(M,V,P,1);
    }
    private void drawCeilings(Matrix4f M, Matrix4f V, Matrix4f P){
        for (int i = 1; i <= numOfCeilings; i++){
            ceilings.get(i-1).draw(M,V,P,1);
        }
    }

}
