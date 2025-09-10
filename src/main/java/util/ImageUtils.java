package util;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Utility class for image related helpers.
 */
public final class ImageUtils {

    private ImageUtils() {
        // utility class
    }

    /**
     * Converts a byte array into an {@link ImageIcon}. If the byte array cannot
     * be decoded into an image, {@code null} is returned.
     *
     * @param bytes raw image bytes from the database
     * @return ImageIcon or {@code null} when bytes are {@code null} or invalid
     */
    public static ImageIcon bytesToImageIcon(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
            return image != null ? new ImageIcon(image) : null;
        } catch (IOException ex) {
            return null;
        }
    }
}

