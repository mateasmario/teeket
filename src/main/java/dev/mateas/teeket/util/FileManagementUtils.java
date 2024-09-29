package dev.mateas.teeket.util;

import dev.mateas.teeket.entity.Ticket;

import java.nio.file.Paths;

public class FileManagementUtils {
    private static final String RESOURCES_DIR = Paths.get("src", "main", "resources").toString();

    public static String getBarcodeName(Ticket ticket) {
        return Paths.get(RESOURCES_DIR, "temp", "barcodes", "QR_" + ticket.getEvent() + "_" + ticket.getCode() + ".png").toString();
    }
}
