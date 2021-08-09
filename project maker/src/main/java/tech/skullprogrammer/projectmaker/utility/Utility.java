package tech.skullprogrammer.projectmaker.utility;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utility {

    private static Logger logger = LoggerFactory.getLogger(Utility.class);

    public File getFileFromResource(String fileName) throws URISyntaxException {

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {

            // failed if files have whitespaces or special characters
            //return new File(resource.getFile());
            return new File(resource.toURI());
        }

    }

    public static void unzipFolderZip4j(Path source, Path target, String rootFolderName, boolean override) throws IOException {
        Path path = Paths.get(target.toString(), rootFolderName);
        File dir = path.toFile();
        if (override) {
            if (dir.exists()) {
                FileUtils.deleteDirectory(dir);
            }
        }
        String fileName = FilenameUtils.getBaseName(source.toString());
        ZipFile zipFile = new ZipFile(source.toFile());
        zipFile.extractAll(target.toString());
        Paths.get(target.toString(), fileName).toFile().renameTo(dir);
//        System.out.println("--> " + zipFile.getFile().getAbsolutePath());
    }

    public static Date checkUpdates(String nomeFile, Date lastDate) {
        try {
            File fileClassificazione = new File(Utility.class.getResource(nomeFile).toURI());
            Date dataUltimaModifica = new Date(fileClassificazione.lastModified());
            if (logger.isDebugEnabled()) logger.debug("Verifico aggiornamenti nel file: " + nomeFile);
            if (logger.isDebugEnabled()) logger.debug("Data ultimo aggiornamento: " + dataUltimaModifica + " - Ultima consultazione: " + lastDate);
            if (dataUltimaModifica.after(lastDate)) {
                if (logger.isTraceEnabled()) logger.trace("Trovato aggiornamento in data: " + dataUltimaModifica);
                return dataUltimaModifica;
            }
            return null;
        } catch (URISyntaxException ex) {
            logger.error(ex.getLocalizedMessage());
            throw new IllegalArgumentException("Impossibile caricare il file con la classificazione delle riviste... " + nomeFile);
        }
    }

    public static List<String> getListFromString(String originalString, String separator) {
        if (originalString == null || originalString.isEmpty()) {
            return new ArrayList<>();
        }
        String[] tokens = originalString.split(separator);
        return Arrays.asList(tokens);
    }

    public static boolean isEmptyString(String s) {
        return s == null || s.equals("");
    }

    public static String listToString(List elements) {
        StringBuilder result = new StringBuilder();
        for (Object element : elements) {
            result.append(element.toString()).append(", ");
        }
        if (result.length() > 0) {
            result.setLength(result.length());
        }
        return result.toString();
    }

    public static Path fromPackageToPath(String packageString) {
        String[] tokens = packageString.split("\\."); 
        Path path = Paths.get("");
        for (String token : tokens) {
            path = path.resolve(token);
        }
        return path;
    }    
    
}
