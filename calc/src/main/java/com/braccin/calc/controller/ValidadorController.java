package com.braccin.calc.controller;

import com.braccin.calc.model.TipoDocumento;
import com.braccin.calc.service.ValidadorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/braccin")
public class ValidadorController {

    private final ValidadorService validadorService;

    public ValidadorController(ValidadorService validadorService) {
        this.validadorService = validadorService;
    }

    @GetMapping("/home")
    public String showForm() {
        return "index";
    }

    @GetMapping("/validar/{tipo}/{documento}")
    public boolean validar(@PathVariable String tipo, @PathVariable String documento) {
        TipoDocumento tipoDoc;
        try {
            tipoDoc = TipoDocumento.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }

        return validadorService.validarDocumento(documento, tipoDoc);
    }
}