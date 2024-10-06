package dev.mateas.teeket.util;

import dev.mateas.teeket.entity.Event;
import dev.mateas.teeket.entity.Ticket;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ImageGenerator {
    private static final String RESOURCES_DIR = Paths.get("src", "main", "resources").toString();

    public void generateTicketImages(Event event, Ticket ticket) throws IOException {
        BufferedImage templateImage = ImageIO.read(Paths.get(RESOURCES_DIR, "temp", "ticket-template", "template.png").toFile());
        BufferedImage outputImage = new BufferedImage(templateImage.getWidth(), templateImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = (Graphics2D) outputImage.getGraphics();
        graphics2D.drawImage(templateImage, 1, 2, null);

        double qrScaling = 0.45;
        addQRToTicket(graphics2D, templateImage, qrScaling, event.getId(), ticket.getCode());

        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        Rectangle2D rectangle2D = fontMetrics.getStringBounds(event.getName(), graphics2D);
        addTextToTicket(graphics2D, 5, templateImage.getHeight()/2, 40, event.getName());
        addTextToTicket(graphics2D, 5, templateImage.getHeight()/2 + 20, 15, "Event ID: " + event.getId());
        addTextToTicket(graphics2D, 5, templateImage.getHeight()/2 + 40, 15, "Generated at: " + ticket.getCreationDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy (HH:mm)")));

        File outputFile = new File(Paths.get(RESOURCES_DIR, "temp", "tickets", "TICKET_" + event.getId() + "_" + ticket.getCode() + ".png").toString());
        ImageIO.write(outputImage, "png", outputFile);

        graphics2D.dispose();
    }

    private void addQRToTicket(Graphics2D graphics2D, BufferedImage templateImage, double qrScaling, String eventId, String code) throws IOException {
        BufferedImage qr = ImageIO.read(Paths.get(RESOURCES_DIR, "temp", "barcodes", "QR_" + eventId + "_" + code + ".png").toFile());
        graphics2D.drawImage(qr, templateImage.getWidth() - (int)(qrScaling*qr.getWidth()), (templateImage.getHeight()-((int)(qrScaling*qr.getHeight())))/2,  (int)(qrScaling*qr.getWidth()), (int)(qrScaling*qr.getHeight()), null);
    }

    private void addTextToTicket(Graphics2D graphics2D, int posX, int posY, int size, String text) {
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f);
        graphics2D.setComposite(alphaComposite);
        graphics2D.setColor(Color.BLACK);
        graphics2D.setFont(new Font(Font.SANS_SERIF, Font.BOLD, size));
        graphics2D.drawString(text.toUpperCase(Locale.ROOT), posX, posY);
    }
}
