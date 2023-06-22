package io.owenrbee.zapb.codegen.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

import io.owenrbee.zapb.codegen.model.spec.FileVO;

@Service
public class ZipService {

    public byte[] compile(FileVO... files) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(baos);

        for (FileVO file : files) {

            InputStream inputStream = new ByteArrayInputStream(file.getContent().getBytes());

            ZipEntry zipEntry = new ZipEntry(file.getFullPath());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            inputStream.close();

        }

        zipOut.finish();

        try {
            // Get the byte array containing the ZIP file data
            byte[] zipData = baos.toByteArray();
            return zipData;
        } finally {
            zipOut.close();
            baos.close();
        }

    }

}
