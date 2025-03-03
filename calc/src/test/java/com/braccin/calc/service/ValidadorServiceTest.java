package com.braccin.calc.service;

import com.braccin.calc.model.TipoDocumento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidadorServiceTest {

    private final ValidadorService service = new ValidadorService();

    @Test
    public void testValidCPFWithoutSeparators() {
        String cpf = "43654732801";
        Assertions.assertTrue(service.validarDocumento(cpf, TipoDocumento.CPF));
    }

    @Test
    public void testValidCPFFull() {
        String cpf = "436.547.328-01";
        Assertions.assertTrue(service.validarDocumento(cpf, TipoDocumento.CPF));
    }

    @Test
    public void testInvalidCPF_MissingSeparators() {
        String cpf = "436.54732801";
        Assertions.assertFalse(service.validarDocumento(cpf, TipoDocumento.CPF));
    }

    @Test
    public void testInvalidCPF_AllDigitsEqual() {
        String cpf = "111.111.111-11";
        Assertions.assertFalse(service.validarDocumento(cpf, TipoDocumento.CPF));
        cpf = "11111111111";
        Assertions.assertFalse(service.validarDocumento(cpf, TipoDocumento.CPF));
    }

    @Test
    public void testValidRGFull() {
        String rg = "12.345.678-9";
        Assertions.assertTrue(service.validarDocumento(rg, TipoDocumento.RG));
        rg = "12.345.678-X";
        Assertions.assertTrue(service.validarDocumento(rg, TipoDocumento.RG));
    }

    @Test
    public void testValidRGPartial() {
        String rg = "12345678-9";
        Assertions.assertTrue(service.validarDocumento(rg, TipoDocumento.RG));
        rg = "12345678-X";
        Assertions.assertTrue(service.validarDocumento(rg, TipoDocumento.RG));
    }

    @Test
    public void testValidRGNoSeparators() {
        String rg = "1234567";
        Assertions.assertTrue(service.validarDocumento(rg, TipoDocumento.RG));
        rg = "12345678";
        Assertions.assertTrue(service.validarDocumento(rg, TipoDocumento.RG));
        rg = "123456789";
        Assertions.assertTrue(service.validarDocumento(rg, TipoDocumento.RG));
        rg = "12345678X";
        Assertions.assertTrue(service.validarDocumento(rg, TipoDocumento.RG));
    }

    @Test
    public void testInvalidRG() {
        String rg = "12.34567-89";
        Assertions.assertFalse(service.validarDocumento(rg, TipoDocumento.RG));
        rg = "1234.5678-9";
        Assertions.assertFalse(service.validarDocumento(rg, TipoDocumento.RG));
        rg = "123456";
        Assertions.assertFalse(service.validarDocumento(rg, TipoDocumento.RG));
        rg = "1234567890";
        Assertions.assertFalse(service.validarDocumento(rg, TipoDocumento.RG));
    }
}
