package com.app.tradogt.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {

    private final String directorioUploads = "./uploads/resenasUsuarios/";

    public String guardarArchivo(MultipartFile file, String nuevoNombre) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("El archivo está vacío");
        }

        String extension = getFileExtension(file.getOriginalFilename());
        String nombreArchivo = nuevoNombre + "." + extension;
        Path rutaArchivo = Paths.get(directorioUploads + nombreArchivo);

        Files.copy(file.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
        return nombreArchivo;
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}

