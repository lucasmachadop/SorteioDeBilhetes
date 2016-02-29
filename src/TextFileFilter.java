import java.io.File;

/**
 * Created by tales on 17/02/16.
 */
public class TextFileFilter extends javax.swing.filechooser.FileFilter {
    public boolean accept(File file) {
        //Converter para minúsculo antes de verificar a extensão
        return (file.getName().toLowerCase().endsWith(".txt")  || file.isDirectory());
    }

    public String getDescription() {
        return "Arquivos Texto (*.txt)";
    }
}
