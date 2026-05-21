package com.symptomchecker;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

public class ScreenshotGenerator {
    public static void main(String[] args) throws Exception {
        String outputPath = args.length == 0 ? "screenshots/user-interface.png" : args[0];

        SwingUtilities.invokeAndWait(() -> {
            try {
                SymptomCheckerFrame frame = new SymptomCheckerFrame();
                frame.setSize(new Dimension(980, 680));
                frame.addNotify();
                frame.validate();
                frame.getContentPane().validate();

                BufferedImage image = new BufferedImage(980, 680, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = image.createGraphics();
                graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                frame.getContentPane().printAll(graphics);
                graphics.dispose();
                frame.dispose();

                File output = new File(outputPath);
                File parent = output.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                ImageIO.write(image, "png", output);
                System.out.println("Saved UI screenshot to " + output.getAbsolutePath());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
