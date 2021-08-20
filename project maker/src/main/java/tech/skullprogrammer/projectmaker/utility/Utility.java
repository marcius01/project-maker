package tech.skullprogrammer.projectmaker.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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
        ZipFile zipFile = new ZipFile(source.toFile());
        zipFile.extractAll(target.toString());
        String fileName = FilenameUtils.getBaseName(source.toString());
        Paths.get(target.toString(), fileName).toFile().renameTo(dir);
//        System.out.println("--> " + zipFile.getFile().getAbsolutePath());
    }

    public static Date checkUpdates(String nomeFile, Date lastDate) {
        try {
            File fileClassificazione = new File(nomeFile);
//            File fileClassificazione = new File(Utility.class.getResource(nomeFile).toURI());
            Date dataUltimaModifica = new Date(fileClassificazione.lastModified());
            if (logger.isDebugEnabled()) logger.debug("Verifico aggiornamenti nel file: " + nomeFile);
            if (logger.isDebugEnabled()) logger.debug("Data ultimo aggiornamento: " + dataUltimaModifica + " - Ultima consultazione: " + lastDate);
            if (dataUltimaModifica.after(lastDate)) {
                if (logger.isTraceEnabled()) logger.trace("Trovato aggiornamento in data: " + dataUltimaModifica);
                return dataUltimaModifica;
            }
            return null;
        } catch (Exception ex) {
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

    public static String createPackage(String... tokens) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (i == 0) {
                if (token.isEmpty()) throw new IllegalArgumentException("first part of the package can't be empty");
                result.append(token);
            } else if (!token.isEmpty()){
                result.append(".").append(token);
            }
        }
        return result.toString();
    }
    
    public static void unzipFolder(Path source, Path target, String rootFolderName, boolean override) throws IOException {
        Path path = Paths.get(target.toString(), rootFolderName);
        File dir = path.toFile();
        if (override) {
            if (dir.exists()) {
                FileUtils.deleteDirectory(dir);
            }
        }
        extract(source, target);
        String fileName = FilenameUtils.getBaseName(source.toString());
        Paths.get(target.toString(), fileName).toFile().renameTo(dir);
        

    }

    private static void extract(Path source, Path target) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source.toFile()))) {
            
            // list files in zip
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {

                boolean isDirectory = false;
                // example 1.1
                // some zip stored files and folders separately
                // e.g data/
                //     data/folder/
                //     data/folder/file.txt
                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }

                Path newPath = zipSlipProtect(zipEntry, target);

                if (isDirectory) {
                    Files.createDirectories(newPath);
                } else {

                    // example 1.2
                    // some zip stored file path only, need create parent directories
                    // e.g data/folder/file.txt
                    if (newPath.getParent() != null) {
                        if (Files.notExists(newPath.getParent())) {
                            Files.createDirectories(newPath.getParent());
                        }
                    }

                    // copy files, nio
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);

                    // copy files, classic
                    /*try (FileOutputStream fos = new FileOutputStream(newPath.toFile())) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                    }
                    }*/
                }

                zipEntry = zis.getNextEntry();

            }
            zis.closeEntry();

        }
    }

    // protect zip slip attack
    private static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir)
        throws IOException {

        // test zip slip vulnerability
        // Path targetDirResolved = targetDir.resolve("../../" + zipEntry.getName());

        Path targetDirResolved = targetDir.resolve(zipEntry.getName());

        // make sure normalized file still has targetDir as its prefix
        // else throws exception
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }

        return normalizePath;
    }
}
