package fr.vvlabs.tools.xls.importer;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class FileUtils {

    /**
     * Checks if is file content type.
     *
     * @param path the path
     * @param contentType the content type
     * @return true, if is file content type
     */
    public static boolean isFileContentType(final Path path, String expectedContentType) {
        try {
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                // Use alternative method by file name
                contentType = URLConnection.guessContentTypeFromName(path.toFile().getName());
                if (contentType == null) {
                    // Use alternative method by file content
                    contentType = URLConnection.guessContentTypeFromStream(Files.newInputStream(path));
                }
            }
            
            return contentType != null && contentType.equals(expectedContentType);
        } catch (IOException e) {
            log.error("isExcelFile: error={}", e.getMessage());
            return false;
        }
    }
}
