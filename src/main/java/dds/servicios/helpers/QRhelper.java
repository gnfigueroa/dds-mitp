package dds.servicios.helpers;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.IOException;
import java.nio.file.Path;

public class QRhelper {
    private static QRhelper qrHelper = new QRhelper();

    public static QRhelper getHelper() {
        return qrHelper;
    }

    public static void creadorQR(String data, Path pathname) throws WriterException, IOException {
        int ancho = 400;
        int alto = 400;

        String formatoImagen = "png";
        QRCodeWriter qrWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrWriter.encode(data, BarcodeFormat.QR_CODE, ancho, alto);
        MatrixToImageWriter.writeToPath(bitMatrix, formatoImagen, pathname);

    }
}