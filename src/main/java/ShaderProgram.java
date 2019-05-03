import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private int program;
    private int vertID;
    private int fragID;

    public ShaderProgram(){
    }


    public static String loadAsString(String location){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(location));
            String buffer = "";
            while((buffer = reader.readLine()) != null){
                result.append(buffer);
                result.append("\n");
            }
            reader.close();
        } catch(IOException e){
            System.err.println(e);
        }
        return result.toString();
    }


    public void load(){

       String vert = loadAsString("/home/dejmian/repos/Doom3D/src/main/java/vertex.glsl");
        String frag = loadAsString("/home/dejmian/repos/Doom3D/src/main/java/fragment.glsl");

        program = glCreateProgram();

        vertID = glCreateShader(GL_VERTEX_SHADER);
        fragID = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(vertID, vert);
        glShaderSource(fragID, frag);

        glCompileShader(vertID);
        if(glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE){
            System.err.println("Failed to compile vertex shader!");
            System.err.println(glGetShaderInfoLog(vertID));
        }
        glCompileShader(fragID);
        if(glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE){
            System.err.println("Failed to compile fragment shader!");
            System.err.println(glGetShaderInfoLog(fragID));
        }
        glAttachShader(program, vertID);
        glAttachShader(program, fragID);

        glLinkProgram(program);
        glValidateProgram(program);
    }

    public void use() {
        glUseProgram(program);
    }

    public void stop() {
        glUseProgram(0);
    }
    public void clear() {
        stop();
        glDetachShader(program, vertID);
        glDetachShader(program, fragID);
        glDeleteShader(vertID);
        glDeleteShader(fragID);
        glDeleteProgram(program);
    }
    public int u(String varName){
        return glGetUniformLocation(program, varName);
    }
    public int a(String varName){ return glGetAttribLocation(program, varName); }
}