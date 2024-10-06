package dev.mateas.teeket.util;

import dev.mateas.teeket.entity.Ticket;
import dev.mateas.teeket.util.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileGenerator {
    private static final String RESOURCES_DIR = Paths.get("src", "main", "resources").toString();

    @Autowired
    private StringGenerator stringGenerator;

    public String generateZipFile(List<Ticket> ticketList) throws IOException {
        String zipId;
        File zipFile;

        do {
            zipId = stringGenerator.generateString(StringType.ALPHANUMERIC, 10);
            zipFile = new File(Paths.get(RESOURCES_DIR, "temp", "zip", zipId + ".zip").toString());
        } while (zipFile.exists());

        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

        for (Ticket ticket : ticketList) {
            writeTicketToZip(out, ticket);
        }

        out.close();

        return zipFile.getName();
    }

    private void writeTicketToZip(ZipOutputStream out, Ticket ticket) throws IOException {
        String barcodeName = FileManagementUtils.getTicketName(ticket);
        File barcode = new File(barcodeName);
        byte[] data = readData(barcode);

        ZipEntry e = new ZipEntry(barcodeName);
        out.putNextEntry(e);
        out.write(data, 0, data.length);
        out.closeEntry();
    }

    private byte[] readData(File barcode) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(barcode);
        byte[] data = new byte[(int) barcode.length()];
        fileInputStream.read(data);
        fileInputStream.close();
        return data;
    }
}
