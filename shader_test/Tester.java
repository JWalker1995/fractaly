import net.walker9.fractaly.shader.*;

import java.awt.Color;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Tester {
    private static final int size_x = 1000;
    private static final int size_y = 1000;

    public static void main(String[] args) {
        Shader s = new FractalGalaxy((float)size_x, (float)size_y);

        float frag[] = new float[4];

        BufferedImage bi = new BufferedImage(size_x, size_y, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < size_x; i++) {
            for (int j = 0; j < size_y; j++) {
                s.main(frag, (float)i + 0.5f, (float)j + 0.5f);
                Color color = new Color(clamp(frag[0]), clamp(frag[1]), clamp(frag[2]), 1.0f);
                bi.setRGB(i, j, color.getRGB());
            }
        }

        try {
            File file;
            int i = Integer.parseInt(args[0]);
            do {
                file = new File("out/img_" + i + ".png");
                i++;
            } while (file.exists());

            ImageIO.write(bi, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static float clamp(float val) {
        return Math.max(0.0f, Math.min(val, 1.0f));
    }
}
