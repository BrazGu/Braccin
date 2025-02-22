package com.braccin.calc.controller;

import com.braccin.calc.model.TipoDocumento;
import com.braccin.calc.service.ValidadorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/validar")
public class ValidadorController {

    private final ValidadorService validadorService;

    public ValidadorController(ValidadorService validadorService) {
        this.validadorService = validadorService;
    }

    @GetMapping("/{tipo}/{documento}")
    public boolean validar(@PathVariable String tipo, @PathVariable String documento) {
        TipoDocumento tipoDoc;
        try {
            tipoDoc = TipoDocumento.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false; // Tipo inválido
        }

        return validadorService.validarDocumento(documento, tipoDoc);
    }
}
