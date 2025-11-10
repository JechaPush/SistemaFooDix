package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.model.Categoria;
import com.example.SistemaDePromociones.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador para el menú de administrador
 */
@Controller
@RequestMapping("/menuAdministrador")
public class AdminController {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Mostrar el menú de administrador
     */
    @GetMapping
    public String mostrarMenuAdmin(Model model) {
        System.out.println("🔧 [ADMIN] Cargando menú de administrador");
        
        // Cargar todas las categorías para el modal
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("categories", categorias);
        
        System.out.println("🔧 [ADMIN] Categorías cargadas: " + categorias.size());
        
        // TODO: Cargar usuarios, restaurantes, delivery, etc.
        // model.addAttribute("users", usuarioRepository.findByRol("CUSTOMER"));
        // model.addAttribute("restaurantUsers", usuarioRepository.findByRol("RESTAURANT"));
        // model.addAttribute("deliveryUsers", usuarioRepository.findByRol("DELIVERY"));
        // model.addAttribute("adminUsers", usuarioRepository.findByRol("ADMIN"));
        
        return "menuAdministrador";
    }
    
    /**
     * Crear nueva categoría
     * POST /menuAdministrador/create-category
     */
    @PostMapping("/create-category")
    public String crearCategoria(
            @RequestParam("nombre") String nombre,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "icono", required = false, defaultValue = "fa-utensils") String icono,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🏷️ [ADMIN] Creando categoría: " + nombre);
            
            Categoria categoria = new Categoria();
            categoria.setNombre(nombre);
            categoria.setDescripcion(descripcion);
            categoria.setIcono(icono);
            categoria.setEstado(true);
            
            categoriaRepository.save(categoria);
            
            redirectAttributes.addFlashAttribute("mensaje", "Categoría creada exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Categoría creada: " + categoria.getCodigo());
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al crear categoría: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al crear la categoría: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    /**
     * Cambiar estado de una categoría (activar/desactivar)
     * POST /menuAdministrador/category/{id}/toggle-status
     */
    @PostMapping("/category/{id}/toggle-status")
    public String cambiarEstadoCategoria(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🔄 [ADMIN] Cambiando estado de categoría: " + id);
            
            Categoria categoria = categoriaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            
            categoria.setEstado(!categoria.getEstado());
            categoriaRepository.save(categoria);
            
            String nuevoEstado = categoria.getEstado() ? "activada" : "desactivada";
            redirectAttributes.addFlashAttribute("mensaje", "Categoría " + nuevoEstado + " exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Categoría " + nuevoEstado + ": " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al cambiar estado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    /**
     * Eliminar una categoría
     * POST /menuAdministrador/category/{id}/delete
     */
    @PostMapping("/category/{id}/delete")
    public String eliminarCategoria(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            System.out.println("🗑️ [ADMIN] Eliminando categoría: " + id);
            
            categoriaRepository.deleteById(id);
            
            redirectAttributes.addFlashAttribute("mensaje", "Categoría eliminada exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            
            System.out.println("✅ [ADMIN] Categoría eliminada: " + id);
            
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Error al eliminar categoría: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }
        
        return "redirect:/menuAdministrador";
    }
    
    /**
     * ENDPOINT TEMPORAL PARA DEBUG - Verificar password BCrypt
     * GET /menuAdministrador/test-password
     */
    @GetMapping("/test-password")
    @ResponseBody
    public String testPassword(@RequestParam(defaultValue = "525224Da!") String password) {
        String existingHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        
        boolean matches = passwordEncoder.matches(password, existingHash);
        String newHash = passwordEncoder.encode(password);
        
        return """
                <h2>Test de Password BCrypt</h2>
                <p><strong>Password a verificar:</strong> %s</p>
                <p><strong>Hash existente en BD:</strong><br/>%s</p>
                <p><strong>¿Coincide?:</strong> %s</p>
                <hr/>
                <p><strong>Nuevo hash generado:</strong><br/>%s</p>
                <hr/>
                <p>Si coincide = true, el login debería funcionar</p>
                <p>Si coincide = false, necesitas actualizar la BD con el nuevo hash</p>
                """.formatted(password, existingHash, matches ? "✅ SÍ" : "❌ NO", newHash);
    }
}
