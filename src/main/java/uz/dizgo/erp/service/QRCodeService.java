package uz.dizgo.erp.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import javax.imageio.ImageIO;

@Service
public class QRCodeService {

    // QR kodni generatsiya qilish
    public byte[] generateQRCode(String data) throws Exception {
        int width = 300;
        int height = 300;

        // QR kod parametrlari
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.MARGIN, 1);

        // QR kodni yaratish
        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height, hints);

        // QR kodni BufferedImage sifatida yaratish
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        // Har bir pikselni qora yoki oq rangda chizish
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                image.setRGB(i, j, matrix.get(i, j) ? 0x000000 : 0xFFFFFF);
            }
        }

        // BufferedImage ni byte arrayga aylantirish
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        return baos.toByteArray();
    }
}
