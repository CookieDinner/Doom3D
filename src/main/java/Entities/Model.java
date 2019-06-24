package Entities;

import Engine.FileLoader;
import Engine.ShaderProgram;
import de.matthiasmann.twl.utils.PNGDecoder;
import lombok.Getter;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL40;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

public class Model implements FileLoader {
    private List<Vector3f> vertices = new ArrayList<>();
    private List<Vector3f> normals = new ArrayList<>();
    private List<Vector2f> texcoords = new ArrayList<>();

    private List<Integer> face_vertices = new ArrayList<>();
    private List<Integer> face_texcoords = new ArrayList<>();
    private List<Integer> face_normals = new ArrayList<>();

    private FloatBuffer fbVertex;
    private FloatBuffer fbVertexNormals;
    private FloatBuffer fbTexCoords;
    private int VertexCount;

    private ShaderProgram shaderProgram;
    private ShaderProgram initialshader;

    private int tex0;
    private int tex1;
    private int tex2;
    private boolean tex_flag = false;

    @Getter
    private List<Vector4f> collisionBox = new ArrayList<>();

    private String cur[];

    public Model(ShaderProgram shader,ShaderProgram initshader, String fileName, String texName, String diffusionMap, String environmentalMap){
        fileName = generateAbsolutePath("/src/main/models/",fileName);
        load(fileName);
        shaderProgram = shader;
        initialshader = initshader;
        tex0 = readTexture(texName);
        tex1 = readTexture(diffusionMap);
        tex2 = readTexture(environmentalMap);
    }

    private int readTexture(String filename) {
        int tex = 0;

        try {
            FileInputStream in = new FileInputStream(generateAbsolutePath("/src/main/models/",filename));
            PNGDecoder dec = new PNGDecoder(in);
            ByteBuffer buf = ByteBuffer.allocateDirect(4*dec.getWidth()*dec.getHeight());
            dec.decode(buf, dec.getWidth()*4, PNGDecoder.Format.RGBA);
            buf.flip();
            tex = glGenTextures();
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D,tex);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, dec.getWidth(), dec.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
            GL40.glGenerateMipmap(GL_TEXTURE_2D);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.5f);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tex;
    }

    private void load(String name){

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
                while(!(line = r.readLine()).contains("Box")){
                    if(line.startsWith("f"))
                        for(int i = 1; i < 4; i++) {
                            cur = line.split(" ")[i].split("/");
                            help(cur);
                        }
                }
                for (int i = 0; i < 4; i++){

                    line = r.readLine();
                    if(line == null){
                        System.out.println("ERROR LOADING THE COLLISION BOX!");
                        collisionBox.clear();
                        break;
                    }
                    if (line.equals("none")){
                        break;
                    }

                    cur = line.split(" ");
                    collisionBox.add(new Vector4f(Float.parseFloat(cur[1]),Float.parseFloat(cur[2]),Float.parseFloat(cur[3]),1.0f));
                }
                VertexCount = face_vertices.size();
                finish();
            r.close();

        }catch(Exception e){
            e.printStackTrace();
        }

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
        float [] ready_vertices = new float[4*VertexCount];
        float [] ready_texcoords = new float[2*VertexCount];
        float [] ready_normals = new float[4*VertexCount];

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

    public void draw (Matrix4f M, Matrix4f V, Matrix4f P, int texNumber){

        shaderProgram.bind();

        GL20.glUniform1i(shaderProgram.u("textureMap0"),texNumber);
        glActiveTexture(GL_TEXTURE0+texNumber);
        glBindTexture(GL_TEXTURE_2D,tex0);

        GL20.glUniform1i(shaderProgram.u("textureMap1"),texNumber+1);
        glActiveTexture(GL_TEXTURE0+texNumber+1);
        glBindTexture(GL_TEXTURE_2D,tex1);

        GL20.glUniform1i(shaderProgram.u("textureMap2"),texNumber+2);
        glActiveTexture(GL_TEXTURE0+texNumber+2);
        glBindTexture(GL_TEXTURE_2D,tex2);

        GL20.glUniformMatrix4fv(shaderProgram.u("P"),false,P.get(BufferUtils.createFloatBuffer(16)));
        GL20.glUniformMatrix4fv(shaderProgram.u("V"),false,V.get(BufferUtils.createFloatBuffer(16)));
        GL20.glUniformMatrix4fv(shaderProgram.u("M"),false,M.get(BufferUtils.createFloatBuffer(16)));

        glEnableVertexAttribArray(shaderProgram.a("vertex"));
        GL20.glVertexAttribPointer(shaderProgram.a("vertex"),4,GL_FLOAT,false,0,fbVertex);
        glEnableVertexAttribArray(shaderProgram.a("normal"));
        GL20.glVertexAttribPointer(shaderProgram.a("normal"),4,GL_FLOAT,false,0,fbVertexNormals);
        glEnableVertexAttribArray(shaderProgram.a("texCoord0"));
        GL20.glVertexAttribPointer(shaderProgram.a("texCoord0"),2,GL_FLOAT,false,0,fbTexCoords);

        glDrawArrays(GL_TRIANGLES, 0, VertexCount);

        glDisableVertexAttribArray(shaderProgram.a("vertex"));
        glDisableVertexAttribArray(shaderProgram.a("normal"));
        glDisableVertexAttribArray(shaderProgram.a("texCoord0"));
    }

    private static FloatBuffer makeFloatBuffer(float[] arr) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(4*arr.length);
        fb.put(arr).flip();
        return fb;
    }



}









