package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.model.DocumentoRestaurante;
import com.example.SistemaDePromociones.repository.DocumentoRestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Controller para servir documentos subidos (imágenes, PDFs, etc.)
 */
@Controller
@RequestMapping("/uploads")
public class DocumentoController {
    
    @Autowired
    private DocumentoRestauranteRepository documentoRepository;
    
    /**
     * Servir documento de restaurante
     * GET /uploads/restaurante/{idRestaurante}/{tipoDocumento}
     * 
     * @param idRestaurante ID del restaurante
     * @param tipoDocumento Tipo: CARTA_RESTAURANTE, CARNET_SANIDAD, LICENCIA_FUNCIONAMIENTO
     */
    @GetMapping("/restaurante/{idRestaurante}/{tipoDocumento}")
    public ResponseEntity<Resource> servirDocumentoRestaurante(
            @PathVariable Long idRestaurante,
            @PathVariable String tipoDocumento) {
        
        try {
            System.out.println("📄 [DOCUMENTO] Buscando documento: " + tipoDocumento + " para restaurante: " + idRestaurante);
            
            // Buscar el documento en la base de datos
            Optional<DocumentoRestaurante> documentoOpt = documentoRepository
                    .findByCodigoRestauranteAndTipoDocumento(idRestaurante, tipoDocumento);
            
            if (documentoOpt.isEmpty()) {
                System.err.println("❌ [DOCUMENTO] No se encontró el documento: " + tipoDocumento);
                return ResponseEntity.notFound().build();
            }
            
            DocumentoRestaurante documento = documentoOpt.get();
            
            // Obtener la ruta del archivo
            String rutaArchivo = documento.getRutaArchivo();
            if (rutaArchivo == null || rutaArchivo.isEmpty()) {
                System.err.println("❌ [DOCUMENTO] El documento no tiene ruta de archivo");
                return ResponseEntity.notFound().build();
            }
            
            // Cargar el archivo desde el sistema de archivos
            Path archivoPath = Paths.get(rutaArchivo);
            File archivo = archivoPath.toFile();
            
            if (!archivo.exists() || !archivo.canRead()) {
                System.err.println("❌ [DOCUMENTO] El archivo no existe o no se puede leer: " + rutaArchivo);
                return ResponseEntity.notFound().build();
            }
            
            // Crear el recurso
            Resource resource = new FileSystemResource(archivo);
            
            // Determinar el tipo MIME
            String contentType = Files.probeContentType(archivoPath);
            if (contentType == null) {
                contentType = determinarContentType(archivo.getName());
            }
            
            System.out.println("✅ [DOCUMENTO] Sirviendo: " + archivo.getName() + 
                             " (" + archivo.length() + " bytes)");
            
            // Preparar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDispositionFormData("inline", archivo.getName());
            headers.setContentLength(archivo.length());
            
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            
        } catch (IOException e) {
            System.err.println("❌ [DOCUMENTO] Error de I/O al servir documento: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            System.err.println("❌ [DOCUMENTO] Error al servir documento: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Determinar el Content-Type según la extensión del archivo
     */
    private String determinarContentType(String nombreArchivo) {
        if (nombreArchivo == null) {
            return "application/octet-stream";
        }
        
        String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1).toLowerCase();
        
        return switch (extension) {
            case "pdf" -> "application/pdf";
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "bmp" -> "image/bmp";
            case "webp" -> "image/webp";
            case "doc" -> "application/msword";
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            default -> "application/octet-stream";
        };
    }
}
