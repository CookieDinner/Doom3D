package Engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram implements FileLoader {

    private final int programId;

    private int vertexShaderId;

    private int fragmentShaderId;

    public String generateAbsolutePath(String fileName) {
        return generateAbsolutePath("/src/main/glsl/",fileName);
    }

    public ShaderProgram() throws Exception {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
    }


    public static String loadProgramAsString(String location){
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


    public void createVertexShader(String fileName) throws Exception{

        String shaderCode = loadProgramAsString(generateAbsolutePath("VertexShader/"+fileName));
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String fileName) throws Exception{

        String shaderCode = loadProgramAsString(generateAbsolutePath("FragmentShader/"+fileName));
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }


    protected int createShader(String shaderCode, int shaderType) throws Exception{
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        glDeleteShader(vertexShaderId);
        glDeleteShader(fragmentShaderId);
        if (programId != 0) {
            glDeleteProgram(programId);
        }

    }

    public void link() throws Exception {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId); //todo to moze byc blad
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

    }

    public int u(String varName){
        return glGetUniformLocation(programId, varName);
    }

    public int a(String varName){ return glGetAttribLocation(programId, varName); }
}