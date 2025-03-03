package com.braccin.calc.service;

import com.braccin.calc.model.TipoDocumento;
import org.springframework.stereotype.Service;

@Service
public class ValidadorService {

    // Estados para o CPF com pontos (full pattern: XXX.XXX.XXX-XX)
    private enum EstadoCPFFull { Q0, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14, ERRO }
    // Estados para o CPF sem pontos (aceita apenas dígitos ou dígitos com dash no lugar certo)
    private enum EstadoCPFNonDot { Q0, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, OK, ERRO }
    
    public boolean validarDocumento(String documento, TipoDocumento tipo) {
        if (tipo == TipoDocumento.CPF) {
            return validarCPF(documento);
        } else if (tipo == TipoDocumento.RG) {
            return validarRG(documento);
        }
        return false;
    }
    
    private boolean validarCPF(String cpf) {
        // Se houver separador de ponto, deve seguir o padrão completo
        boolean hasDot = cpf.contains(".");
        boolean valid;
        if (hasDot) {
            valid = validarCPFFull(cpf);
        } else {
            valid = validarCPFNonDot(cpf);
        }
        if (!valid) return false;
        
        // Verifica se, após remover os separadores, há exatamente 11 dígitos
        // e se não são todos iguais.
        String digits = cpf.replaceAll("\\D", "");
        if (digits.length() != 11) return false;
        char first = digits.charAt(0);
        for (int i = 1; i < digits.length(); i++) {
            if (digits.charAt(i) != first) {
                return true; // Encontrou ao menos um dígito diferente
            }
        }
        return false;
    }
    
    // AFD para CPF com pontos: formato obrigatório "XXX.XXX.XXX-XX"
    private boolean validarCPFFull(String cpf) {
        if (cpf.length() != 14) return false; // O formato completo tem 14 caracteres
        EstadoCPFFull estado = EstadoCPFFull.Q0;
        for (int i = 0; i < cpf.length(); i++) {
            char c = cpf.charAt(i);
            switch (estado) {
                case Q0:
                    estado = Character.isDigit(c) ? EstadoCPFFull.Q1 : EstadoCPFFull.ERRO;
                    break;
                case Q1:
                    estado = Character.isDigit(c) ? EstadoCPFFull.Q2 : EstadoCPFFull.ERRO;
                    break;
                case Q2:
                    estado = Character.isDigit(c) ? EstadoCPFFull.Q3 : EstadoCPFFull.ERRO;
                    break;
                case Q3:
                    estado = (c == '.') ? EstadoCPFFull.Q4 : EstadoCPFFull.ERRO;
                    break;
                case Q4:
                    estado = Character.isDigit(c) ? EstadoCPFFull.Q5 : EstadoCPFFull.ERRO;
                    break;
                case Q5:
                    estado = Character.isDigit(c) ? EstadoCPFFull.Q6 : EstadoCPFFull.ERRO;
                    break;
                case Q6:
                    estado = Character.isDigit(c) ? EstadoCPFFull.Q7 : EstadoCPFFull.ERRO;
                    break;
                case Q7:
                    estado = (c == '.') ? EstadoCPFFull.Q8 : EstadoCPFFull.ERRO;
                    break;
                case Q8:
                    estado = Character.isDigit(c) ? EstadoCPFFull.Q9 : EstadoCPFFull.ERRO;
                    break;
                case Q9:
                    estado = Character.isDigit(c) ? EstadoCPFFull.Q10 : EstadoCPFFull.ERRO;
                    break;
                case Q10:
                    estado = Character.isDigit(c) ? EstadoCPFFull.Q11 : EstadoCPFFull.ERRO;
                    break;
                case Q11:
                    estado = (c == '-') ? EstadoCPFFull.Q12 : EstadoCPFFull.ERRO;
                    break;
                case Q12:
                    estado = Character.isDigit(c) ? EstadoCPFFull.Q13 : EstadoCPFFull.ERRO;
                    break;
                case Q13:
                    estado = Character.isDigit(c) ? EstadoCPFFull.Q14 : EstadoCPFFull.ERRO;
                    break;
                default:
                    estado = EstadoCPFFull.ERRO;
                    break;
            }
            if (estado == EstadoCPFFull.ERRO) return false;
        }
        return estado == EstadoCPFFull.Q14;
    }
    
    // AFD para CPF sem pontos (aceita puro 11 dígitos ou 9 dígitos seguidos de dash e 2 dígitos)
    private boolean validarCPFNonDot(String cpf) {
        int len = cpf.length();
        // Formatos válidos: 11 (todos dígitos) ou 12 (9 dígitos, dash, 2 dígitos)
        if (len != 11 && len != 12) return false;
        EstadoCPFNonDot estado = EstadoCPFNonDot.Q0;
        for (int i = 0; i < len; i++) {
            char c = cpf.charAt(i);
            switch (estado) {
                // Para os primeiros 9 estados, esperamos dígitos
                case Q0: case Q1: case Q2: case Q3: case Q4: 
                case Q5: case Q6: case Q7: case Q8:
                    if (Character.isDigit(c)) {
                        // Avança para o próximo estado (incrementa ordinalmente)
                        estado = EstadoCPFNonDot.values()[estado.ordinal() + 1];
                    } else {
                        estado = EstadoCPFNonDot.ERRO;
                    }
                    break;
                case Q9:
                    if (len == 11) {
                        // Em formato puro, esperamos um dígito
                        estado = Character.isDigit(c) ? EstadoCPFNonDot.Q10 : EstadoCPFNonDot.ERRO;
                    } else { // len == 12: deve haver um dash na posição 9
                        estado = (c == '-') ? EstadoCPFNonDot.Q10 : EstadoCPFNonDot.ERRO;
                    }
                    break;
                case Q10:
                    if (Character.isDigit(c)) {
                        estado = EstadoCPFNonDot.Q11;
                    } else {
                        estado = EstadoCPFNonDot.ERRO;
                    }
                    break;
                case Q11:
                    if (len == 12) {
                        // No formato com dash, após Q11 esperamos mais um dígito e então aceitamos
                        estado = Character.isDigit(c) ? EstadoCPFNonDot.OK : EstadoCPFNonDot.ERRO;
                    } else {
                        estado = EstadoCPFNonDot.ERRO;
                    }
                    break;
                default:
                    estado = EstadoCPFNonDot.ERRO;
                    break;
            }
            if (estado == EstadoCPFNonDot.ERRO) return false;
        }
        if (len == 11) {
            return estado == EstadoCPFNonDot.Q11;
        } else { // len == 12
            return estado == EstadoCPFNonDot.OK;
        }
    }
    
    private boolean validarRG(String rg) {
        // Implementação do RG (não abordada nesta etapa)
        return true;
    }
}
