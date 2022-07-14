package dds.servicios.helpers;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

public class PhotoUploaderHelper {
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    private static PhotoUploaderHelper dateHelper = new PhotoUploaderHelper();

    public static PhotoUploaderHelper getHelper() { return dateHelper; }

    public String uploadPhoto (InputStream ss) throws IOException {
      /*  String foto = null;
        File uploadDir = new File("src\\main\\resources\\public\\fotos");
        uploadDir.mkdir();
        Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");
        Files.copy(ss, tempFile, StandardCopyOption.REPLACE_EXISTING);
        foto = tempFile.toString();
        foto = foto.replace("src\\main\\resources\\public\\","");
*/

        byte[] bytes = Base64.getEncoder().encode(IOUtils.toByteArray(ss));
        return (new String(bytes));
    }

    public  String convertInputStreamToString(InputStream is) throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        // Java 1.1
        return result.toString(StandardCharsets.UTF_8.name());

    }




}
