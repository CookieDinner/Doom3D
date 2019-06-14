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
    private List<Vector3f> vertices = new ArrayList<Vector3f>();
    private List<Vector3f> normals = new ArrayList<Vector3f>();
    private List<Vector2f> texcoords = new ArrayList<Vector2f>();

    private List<Integer> face_vertices = new ArrayList<Integer>();
    private List<Integer> face_texcoords = new ArrayList<Integer>();
    private List<Integer> face_normals = new ArrayList<Integer>();

    public float[] ready_vertices;
    public float[] ready_texcoords;
    public float[] ready_normals;

    public FloatBuffer fbVertex;
    public FloatBuffer fbVertexNormals;
    public FloatBuffer fbTexCoords;
    public int VertexCount;

    boolean tex_flag = false;

    String cur[];

    public Model(String fileName){
        fileName = generateAbsolutePath("/src/main/models/",fileName);
        load(fileName);
    }

    public void load(String name){

        try{
            vertices.clear();
            normals.clear();
            texcoords.clear();

            face_vertices.clear();
            face_texcoords.clear();
            face_normals.clear();

            BufferedReader r = new BufferedReader(
                    new FileReader(new File(name)));

            while(true) {
                cur = r.readLine().split(" ");
                if (cur[0].equals("v")) {
                    vertices.add(new Vector3f(
                            Float.parseFloat(cur[1]),
                            Float.parseFloat(cur[2]),
                            Float.parseFloat(cur[3])));
                }
                if (cur[0].equals("vn")) {
                    normals.add(new Vector3f(
                            Float.parseFloat(cur[1]),
                            Float.parseFloat(cur[2]),
                            Float.parseFloat(cur[3])));
                }
                if (cur[0].equals("vt")) {
                    texcoords.add(new Vector2f(
                            Float.parseFloat(cur[1]),
                            Float.parseFloat(cur[2])));
                }
                if (cur[0].equals("s"))
                    break;
            }
                String line;
                int m = 0;
                int n = 0;
                while((line = r.readLine()) != null){
                    if(line.startsWith("f"))
                        for(int i = 1; i < 4; i++) {
                            cur = line.split(" ")[i].split("/");
                            help(cur);
                        }
                }
                VertexCount = face_vertices.size();
                finish();
            r.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        //fbVertex = makeFloatBuffer(ready_vertices);
        //fbVertexNormals = makeFloatBuffer(ready_normals);
        //fbTexCoords = makeFloatBuffer(ready_texcoords);
    }
    private void help(String [] cur){
        face_vertices.add(Integer.parseInt(cur[0]));
        face_normals.add(Integer.parseInt(cur[2]));
        if (!cur[1].equals("")) {
            face_texcoords.add(Integer.parseInt(cur[1]));
            tex_flag = true;
        }
    }
    private void finish(){
        ready_vertices = new float[4*VertexCount];
        ready_texcoords = new float[2*VertexCount];
        ready_normals = new float[4*VertexCount];

        for (int i = 0; i < VertexCount; i++){
            Vector3f cur_vert = vertices.get(face_vertices.get(i) - 1);
            ready_vertices[(4*i)] = cur_vert.x;
            ready_vertices[(4*i)+1] = cur_vert.y;
            ready_vertices[(4*i)+2] = cur_vert.z;
            ready_vertices[(4*i)+3] = 1.0f;

            if (tex_flag) {
                Vector2f cur_tex = texcoords.get(face_texcoords.get(i) - 1);
                ready_texcoords[(2 * i)] = cur_tex.x;
                ready_texcoords[(2 * i) + 1] = 1 - cur_tex.y;
            }

            Vector3f cur_norm = normals.get(face_normals.get(i) - 1);
            ready_normals[(4*i)] = cur_norm.x;
            ready_normals[(4*i)+1] = cur_norm.y;
            ready_normals[(4*i)+2] = cur_norm.z;
            ready_normals[(4*i)+3] = 0.0f;
        }
        fbVertex = makeFloatBuffer(ready_vertices);
        fbTexCoords = makeFloatBuffer(ready_texcoords);
        fbVertexNormals = makeFloatBuffer(ready_normals);

    }

    public static FloatBuffer makeFloatBuffer(float[] arr) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(4*arr.length);
        fb.put(arr).flip();
        return fb;
    }



}
