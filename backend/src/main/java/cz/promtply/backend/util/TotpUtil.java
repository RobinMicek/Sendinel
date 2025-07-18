package cz.promtply.backend.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Component
public class TotpUtil {

    private static final GoogleAuthenticator googleAuth = new GoogleAuthenticator();

    @Value("${app.app-name}")
    private String appName;

    @Value("${app.totp.qr.width}")
    private int qrWidth;

    @Value("${app.totp.qr.height}")
    private int qrHeight;

    public String generateKey() {
        return googleAuth.createCredentials().getKey();
    }

    private String getOtpAuthTotpURL(String accountName, String secret) {
        return String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                appName, accountName, secret, appName
        );
    }

    public String generateQRCodeBase64(String accountName, String secret) throws WriterException, IOException {
        String qrText = getOtpAuthTotpURL(accountName, secret);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, qrWidth, qrHeight);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        javax.imageio.ImageIO.write(image, "png", outputStream);

        byte[] pngData = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(pngData);
    }

    public static boolean verifyTotp(String secret, int code) {
        return googleAuth.authorize(secret, code);
    }
}
