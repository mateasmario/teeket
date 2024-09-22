package dev.mateas.teeket.util;

import com.spire.barcode.BarCodeGenerator;
import com.spire.barcode.BarCodeType;
import com.spire.barcode.BarcodeSettings;
import com.spire.barcode.QRCodeECL;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QRCodeGenerator {
    public void generateQRCode(String eventId, String ticketId) throws IOException {
        BarcodeSettings settings = new BarcodeSettings();
        settings.setType(BarCodeType.QR_Code);
        settings.setData("http://192.168.1.4/validate/" + eventId + "/" + ticketId);
        settings.setData2D(ticketId);
        settings.setShowTextOnBottom(true);
        settings.hasBorder(false);
        settings.setX(2);
        settings.setQRCodeECL(QRCodeECL.M);

        BarCodeGenerator barCodeGenerator = new BarCodeGenerator(settings);
        BufferedImage bufferedImage = barCodeGenerator.generateImage();
        ImageIO.write(bufferedImage,"png",new File("QR_ " + eventId + "_" + ticketId + ".png"));
        System.out.println("Complete!");
    }
}
