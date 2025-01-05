package com.hitachi.jwtqr.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class QrCodeService {

    public byte[] generateQrCode(String jwt) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 1); // Default margin is 4, setting to 1 for compact QR code
        BufferedImage image = toBufferedImage(qrCodeWriter.encode(jwt, BarcodeFormat.QR_CODE, 300, 300, hints));

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "PNG", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }

    private BufferedImage toBufferedImage(com.google.zxing.common.BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0x000000 : 0xFFFFFF); // Black and white QR code
            }
        }
        return image;
    }
}
