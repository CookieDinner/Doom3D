package Entities;

import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector4f;

@Getter
@Setter
public class Entity {
    private float posX;
    private float posZ;
    private Model model;
//    private float minX;
//    private float maxX;
//    private float minZ;
//    private float maxZ;

//    @Getter(AccessLevel.NONE)
    private Vector4f defaultMinCollisionBox;
//    @Getter(AccessLevel.NONE)
    private Vector4f defaultMaxCollisionBox;
    private Vector4f currentMinCollisionBox;
    private Vector4f currentMaxCollisionBox;


    public Entity(float posX, float posZ, Model model) {
        this.posX = posX;
        this.posZ = posZ;
        this.model = model;

        if (model == null) {
            defaultMinCollisionBox = new Vector4f(-2,1,-2,1);
            defaultMaxCollisionBox = new Vector4f(2,1,2,1);
        }
        else{
            //todo moze być bład jesli te wierzchołki moga byc w losowej kolejnosci
            // (bo normalnie tylko 2 i 3 wektor maja wszystkie rozniace sie punkty X i Z)
            float x1 = model.getCollisionBox().get(1).x;
            float x2 = model.getCollisionBox().get(2).x;
            float z1 = model.getCollisionBox().get(1).z;
            float z2 = model.getCollisionBox().get(2).z;

            defaultMinCollisionBox = new Vector4f(Math.min(x1,x2),1,Math.min(z1,z2),1);
            defaultMaxCollisionBox = new Vector4f(Math.max(x1,x2),1,Math.max(z1,z2),1);
        }
        currentMinCollisionBox = new Vector4f(defaultMinCollisionBox.x, defaultMinCollisionBox.y, defaultMinCollisionBox.z, defaultMinCollisionBox.w);
        currentMaxCollisionBox = new Vector4f(defaultMaxCollisionBox.x, defaultMaxCollisionBox.y, defaultMaxCollisionBox.z, defaultMaxCollisionBox.w);

    }

    public Vector4f getDefaultMinCollisionBox() {
        return new Vector4f(defaultMinCollisionBox.x, defaultMinCollisionBox.y, defaultMinCollisionBox.z, defaultMinCollisionBox.w);
    }

    public Vector4f getDefaultMaxCollisionBox() {
        return new Vector4f(defaultMaxCollisionBox.x, defaultMaxCollisionBox.y, defaultMaxCollisionBox.z, defaultMaxCollisionBox.w);
    }

    public void updateCurrentVectors(Matrix4f matrix4f){
        currentMinCollisionBox = getDefaultMinCollisionBox().mul(matrix4f);
        currentMaxCollisionBox = getDefaultMaxCollisionBox().mul(matrix4f);
    }

    public float getMinX() {
        return currentMinCollisionBox.x;
    }

    public float getMaxX() {
        return currentMaxCollisionBox.x;
    }

    public float getMinZ() {
        return currentMinCollisionBox.z;
    }

    public float getMaxZ() {
        return currentMaxCollisionBox.z;
    }

    public void debugVectors(){
        System.out.println(currentMinCollisionBox);
        System.out.println(currentMaxCollisionBox);
        System.out.println("-----------------------------");
    }
}
