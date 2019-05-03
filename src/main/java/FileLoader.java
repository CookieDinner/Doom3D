import java.io.File;

public interface FileLoader {

    default String generateAbsolutePath(String subfolder, String fileName){
        String filePath = new File("").getAbsolutePath();
        if (!System.getProperty("os.name").equals("Linux")){
            filePath = filePath.replaceAll("\\\\","\\\\\\\\");
            subfolder = subfolder.replaceAll("/","\\\\");
        }

        filePath += subfolder+fileName;
        System.out.println(filePath);//todo usunac

        return filePath;
    }
}
