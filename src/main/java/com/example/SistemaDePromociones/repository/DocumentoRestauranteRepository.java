package com.example.SistemaDePromociones.repository;

import com.example.SistemaDePromociones.model.DocumentoRestaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para DocumentoRestaurante
 */
@Repository
public interface DocumentoRestauranteRepository extends JpaRepository<DocumentoRestaurante, Long> {
    
    /**
     * Buscar documento por restaurante y tipo de documento
     */
    Optional<DocumentoRestaurante> findByCodigoRestauranteAndTipoDocumento(Long codigoRestaurante, String tipoDocumento);
    
    /**
     * Buscar todos los documentos de un restaurante
     */
    List<DocumentoRestaurante> findByCodigoRestaurante(Long codigoRestaurante);
    
    /**
     * Verificar si existe un documento para un restaurante y tipo específico
     */
    boolean existsByCodigoRestauranteAndTipoDocumento(Long codigoRestaurante, String tipoDocumento);
}
