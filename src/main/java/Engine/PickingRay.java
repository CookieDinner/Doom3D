package Engine;

import org.joml.Vector3f;

public class PickingRay
{
    private Vector3f clickPosInWorld = new Vector3f();
    private Vector3f direction = new Vector3f();
    private Vector3f screenHorizontally = new Vector3f();
    private Vector3f screenVertically = new Vector3f();
    Vector3f view = new Vector3f();



    /**
     * Computes the intersection of this ray with the X-Y Plane (where Z = 0)
     * and writes it back to the provided vector.
     */
    public void intersectionWithXyPlane(float[] worldPos)
    {
        float s = -clickPosInWorld.z / direction.z;
        worldPos[0] = clickPosInWorld.x+direction.x*s;
        worldPos[1] = clickPosInWorld.y+direction.y*s;
        worldPos[2] = 0;
    }

    public boolean isIntersectingThePoint(float x, float y, float z){
        float first = direction.x * x + direction.z * z;
        float second = direction.y * y;

        if (Math.abs(first-second)<10)return true;
        return false;
    }

    public void calcuateScreenVerticallyAndHorizontally(Vector3f lookAt, Vector3f position, Vector3f lookup, float viewAngle,
                                                        float nearClippingPlaneDistance, float viewportAspectRatio){
        // look direction
//        view.subAndAssign(lookAt, position).normalize();
        lookAt.sub(position,view);

        // screenX
//        screenHorizontally.crossAndAssign(view, lookup).normalize();
        view.cross(lookup, screenHorizontally);


        // screenY
//        screenVertically.crossAndAssign(screenHorizontally, view).normalize();
        screenHorizontally.cross(view,screenVertically);

        float radians = (float) (viewAngle*Math.PI / 180f);
        float halfHeight = (float) (Math.tan(radians/2)*nearClippingPlaneDistance);
        float halfScaledAspectRatio = halfHeight*viewportAspectRatio;

//        screenVertically.scale(halfHeight);
//        screenHorizontally.scale(halfScaledAspectRatio);
        screenVertically.mul(halfHeight);
        screenHorizontally.mul(halfScaledAspectRatio);
    }

    public void picking(float screenX, float screenY, Vector3f position, float viewportWidth, float viewportHeight)
    {
        clickPosInWorld.set(position);
        clickPosInWorld.add(view);

        screenX -= viewportWidth/2f;
        screenY -= viewportHeight/2f;

        // normalize to 1
        screenX /= (viewportWidth/2f);
        screenY /= (viewportHeight/2f);

        clickPosInWorld.x += screenHorizontally.x*screenX + screenVertically.x*screenY;
        clickPosInWorld.y += screenHorizontally.y*screenX + screenVertically.y*screenY;
        clickPosInWorld.z += screenHorizontally.z*screenX + screenVertically.z*screenY;

        direction.set(clickPosInWorld);
        direction.sub(position);
    }



    public Vector3f getClickPosInWorld() {
        return clickPosInWorld;
    }
    public Vector3f getDirection() {
        return direction;
    }
}