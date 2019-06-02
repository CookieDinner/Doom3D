package Entities;

import Engine.FileLoader;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Model implements FileLoader {
    private List<Vector3f> temp_vertices = new ArrayList<Vector3f>();
    public List<Vector3f> temp_normals = new ArrayList<Vector3f>();
    private List<Vector2f> temp_texcoords = new ArrayList<Vector2f>();
    private List<Integer> temp_indices = new ArrayList<Integer>();
    public float[] vertices = {};
    public float[] normals = {};
    public float[] texcoords = {};
    public int[] indices = {};
    public FloatBuffer fbVertex;
    public FloatBuffer fbVertexNormals;
    public FloatBuffer fbTexCoords;
    public int VertexCount;

    String cur[];

    public Model(String fileName){
        fileName = generateAbsolutePath("/src/main/models/",fileName);
        load(fileName);
    }

    public void load(String name){

        try{
            temp_vertices.clear();
            temp_normals.clear();
            temp_texcoords.clear();

            BufferedReader r = new BufferedReader(
                    new FileReader(new File(name)));

            while(true) {
                cur = r.readLine().split(" ");
                if (cur[0].equals("v")) {
                    temp_vertices.add(new Vector3f(
                            Float.parseFloat(cur[1]),
                            Float.parseFloat(cur[2]),
                            Float.parseFloat(cur[3])));
                }
                if (cur[0].equals("vn")) {
                    temp_normals.add(new Vector3f(
                            Float.parseFloat(cur[1]),
                            Float.parseFloat(cur[2]),
                            Float.parseFloat(cur[3])));
                }
                if (cur[0].equals("vt")) {
                    temp_texcoords.add(new Vector2f(
                            Float.parseFloat(cur[1]),
                            Float.parseFloat(cur[2])));
                }
                if (cur[0].equals("s")) {
                    vertices = new float[temp_vertices.size() * 4];
                    normals = new float[temp_vertices.size() * 4];
                    texcoords = new float[temp_vertices.size() * 2];
                    VertexCount = temp_vertices.size();
                    break;
                }
            }
                String line;
                int m = 0;
                int n = 0;
                while((line = r.readLine()) != null){
                    for(int i = 3; i > 0; i--) {        //change to for(int i=0;i<4;i++) if the face culling works in the wrong way
                        cur = line.split(" ")[i].split("/");
                        help(cur,m,n);
                        m+=4;
                        n+=2;
                    }
                }
            r.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        fbVertex = makeFloatBuffer(vertices);
        fbVertexNormals = makeFloatBuffer(normals);
        fbTexCoords = makeFloatBuffer(texcoords);
    }
    private void help(String [] cur, int pos, int texpos){
        Vector3f curVertex = temp_vertices.get(Integer.parseInt(cur[0])-1);
        vertices[pos]=curVertex.x;
        vertices[pos+1]=curVertex.y;
        vertices[pos+2]=curVertex.z;
        vertices[pos+3]=1.0f;
        Vector3f curNormal = temp_normals.get(Integer.parseInt(cur[2])-1);
        normals[pos]=curNormal.x;
        normals[pos+1]=curNormal.y;
        normals[pos+2]=curNormal.z;
        normals[pos+3]=0.0f;
        if (!cur[1].equals("")) {
            Vector2f curTexcoord = temp_texcoords.get(Integer.parseInt(cur[1]) - 1);
            texcoords[texpos] = curTexcoord.x;
            texcoords[texpos + 1] = curTexcoord.y;
        }
    }
    public static FloatBuffer makeFloatBuffer(float[] arr) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(4*arr.length);
        fb.put(arr).flip();
        return fb;
    }



}
