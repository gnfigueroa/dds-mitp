package dds.servicios.helpers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CalcDistanciaHelper {

    private static CalcDistanciaHelper calcDistanciaHelper = new CalcDistanciaHelper();

    public static CalcDistanciaHelper getHelper() { return calcDistanciaHelper; }

    //calculo de distancia
    public  double distanciaCoord(double lat, double lng, double lat2, double lng2) {
        double lat1 = lat;
        double lng1 = lng;
        //double radioTierra = 6371000;//probar en metros tambien
        double radioTierra = 6371;//en kilómetros
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
        double distancia = radioTierra * va2;

        return distancia;
    }




}
