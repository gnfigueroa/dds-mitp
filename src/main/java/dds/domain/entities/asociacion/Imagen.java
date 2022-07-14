package dds.domain.entities.asociacion;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


public class Imagen {

    public Imagen() {
    }

    public BufferedImage redimensionar(String archivo, Integer anchoSeteado, Integer altoSeteado){ //ruta de archivo y el porcentaje que se desea reducir colta
        BufferedImage bf = null;    // Acá se va a cargar la imagen
        try {
            bf = ImageIO.read(new File(archivo));
        } catch (IOException ex) {
            Logger.getLogger(Imagen.class.getName()).log(Level.SEVERE, null, ex);   // Manejo de error de archivo
        }
        int anchoFotoOriginal = bf.getWidth();
        int altoFotoOriginal = bf.getHeight();

        // Lógica del cambio de ancho/alto manteniendo la relación de aspecto seteada originalmente
        double ras = (anchoSeteado * 1.0) / altoSeteado;               // ras: relación de aspecto seteada (o configurada, o elegida...)
        double rafo = (anchoFotoOriginal * 1.0) / altoFotoOriginal;    // rafo: relación de aspecto de la foto original
        int anchoFotoFinal;     // El ancho de la foto final que se guardará
        int altoFotoFinal;      // El alto de la foto final que se guardará

        if (rafo > ras) {
            anchoFotoFinal = anchoSeteado;                  // Se fija el ancho máximo como el ancho seteado
            altoFotoFinal = (int) (anchoFotoFinal / rafo);  // Se calcula el alto partir del ancho y la relación de aspecto de la foto original
            if (altoFotoFinal == 0) altoFotoFinal = 1;      // Corrección para que la foto final tenga como mínimo 1 píxel de alto (nunca 0)
        }
        else {
            altoFotoFinal = altoSeteado;                    // Se fija el alto máximo como el alto seteado
            anchoFotoFinal = (int) (altoFotoFinal * rafo);  // Se calcula el ancho a partir del alto y la relación de aspecto de la foto original
            if (anchoFotoFinal == 0) anchoFotoFinal = 1;    // Corrección para que la foto final tenga como mínimo 1 píxel de ancho (nunca 0)
        }

        BufferedImage bufim = new BufferedImage(anchoFotoFinal, altoFotoFinal, bf.getType());     // Se guarda la nueva imagen creada
        Graphics2D g = bufim.createGraphics();  // Provee métodos para laburar con imágenes
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(bf, 0,0, anchoFotoFinal, altoFotoFinal, 0,0, anchoFotoOriginal, altoFotoOriginal, null); // Redibuja la imagen
        g.dispose();
        return bufim;
    }

}