package faang.school.projectservice.util.validator;

import faang.school.projectservice.exception.ImageResizeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
@Slf4j
public class CoverHandler {
    @Value("${spring.image.cover.maxHeight}")
    private int maxHeight;
    @Value("${spring.image.cover.maxWidth}")
    private int maxWidth;

    public byte[] resizeCover(MultipartFile multipartFile) {
        try {
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());

            int width = getNewWidth(image.getWidth(), image.getHeight());
            int height = getNewHeight(image.getWidth(), image.getHeight());

            BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = resizedImage.createGraphics();
            graphics.drawImage(image, 0, 0, width, height, null);
            graphics.dispose();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ImageResizeException("Error while resizing image: " + e.getMessage());
        }
    }

    public int getNewWidth(int width, int height) {
        return (width == height && width > maxWidth) ? maxWidth : Math.min(width, maxWidth);
    }

    public int getNewHeight(int width, int height) {
        return (width == height && width > maxWidth) ? maxWidth : Math.min(height, maxHeight);
    }
}
