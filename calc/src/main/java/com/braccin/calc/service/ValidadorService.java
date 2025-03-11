package com.braccin.calc.service;

import com.braccin.calc.model.TipoDocumento;
import org.springframework.stereotype.Service;

@Service
public class ValidadorService {

    public boolean validarDocumento(String documento, TipoDocumento tipo) {
        if (tipo == TipoDocumento.CPF) {
            return validarCPF(documento);
        } else if (tipo == TipoDocumento.RG) {
            return validarRG(documento);
        }
        return false;
    }

    private boolean validarCPF(String cpf) {
        String digits = cpf.replaceAll("\\D", "");
        if (digits.length() != 11) return false;

        // Verificando se todos os números são iguais (CPF inválido)
        char first = digits.charAt(0);
        for (int i = 1; i < digits.length(); i++) {
            if (digits.charAt(i) != first) {
                // Verificando os dígitos verificadores
                return verificarDigitosCPF(digits);
            }
        }
        return false;
    }

    private boolean verificarDigitosCPF(String cpf) {
        int soma1 = 0, soma2 = 0;

        // Cálculo do primeiro dígito verificador
        for (int i = 0; i < 9; i++) {
            soma1 += (cpf.charAt(i) - '0') * (10 - i);
        }
        int digito1 = 11 - (soma1 % 11);
        if (digito1 >= 10) digito1 = 0;

        // Cálculo do segundo dígito verificador
        for (int i = 0; i < 9; i++) {
            soma2 += (cpf.charAt(i) - '0') * (11 - i);
        }
        soma2 += digito1 * 2;
        int digito2 = 11 - (soma2 % 11);
        if (digito2 >= 10) digito2 = 0;

        // Verificando se os dígitos calculados são iguais aos dois últimos do CPF
        return cpf.charAt(9) - '0' == digito1 && cpf.charAt(10) - '0' == digito2;
    }

    private boolean validarRG(String rg) {
        // Remover pontos, hífens ou outros caracteres
        String digits = rg.replaceAll("[^0-9X]", "");
        if (digits.length() < 8) return false;

        // RG geralmente tem 8 ou 9 caracteres (com o último sendo o dígito verificador)
        char digitoVerificador = digits.charAt(digits.length() - 1);

        // Verificando se o dígito final é válido para o RG
        return verificarDigitoRG(digits, digitoVerificador);
    }

    private boolean verificarDigitoRG(String rg, char digitoVerificador) {
        if (rg.length() == 9) {
            // Se o RG tem 9 caracteres, o dígito verificador é calculado
            return calcularDigitoVerificadorRG(rg.substring(0, 8)) == digitoVerificador;
        }
        return true; // Se o RG tiver 8 caracteres, não há digito verificador a validar
    }

    private char calcularDigitoVerificadorRG(String rgBase) {
        int soma = 0;
        for (int i = 0; i < rgBase.length(); i++) {
            soma += (rgBase.charAt(i) - '0') * (9 - i);
        }
        int resto = soma % 11;
        if (resto == 10) {
            return 'X'; // Se o resto for 10, o dígito é 'X'
        } else {
            return (char) ('0' + resto); // Caso contrário, o dígito é o resto da divisão
        }
    }
}
