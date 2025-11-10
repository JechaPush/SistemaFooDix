package com.example.SistemaDePromociones.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad DocumentoRestaurante - Almacena los documentos legales del restaurante
 * (Carta del Restaurante, Licencia de Funcionamiento, Carnet de Sanidad, etc.)
 */
@Entity
@Table(name = "documento_restaurante")
@Data
public class DocumentoRestaurante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;
    
    @Column(name = "codigo_restaurante", nullable = false)
    private Long codigoRestaurante;
    
    @Column(name = "tipo_documento", nullable = false, length = 50)
    private String tipoDocumento;
    
    @Column(name = "ruta_archivo", nullable = false, length = 255)
    private String rutaArchivo;
    
    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;
    
    @Column(name = "fecha_subida", nullable = false, updatable = false)
    private LocalDateTime fechaSubida;
    
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_restaurante", insertable = false, updatable = false)
    private Restaurante restaurante;
    
    @PrePersist
    protected void onCreate() {
        fechaSubida = LocalDateTime.now();
        if (estado == null) {
            estado = true;
        }
    }
}
