package com.braccin.calc.service;

import com.braccin.calc.model.TipoDocumento;
import org.springframework.stereotype.Service;

@Service
public class ValidadorService {

    private enum EstadoCPFFull { Q0, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12, Q13, Q14, ERRO }
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
        boolean hasDot = cpf.contains(".");
        boolean valid;
        if (hasDot) {
            valid = validarCPFFull(cpf);
        } else {
            valid = validarCPFNonDot(cpf);
        }
        if (!valid) return false;
        String digits = cpf.replaceAll("\\D", "");
        if (digits.length() != 11) return false;
        char first = digits.charAt(0);
        for (int i = 1; i < digits.length(); i++) {
            if (digits.charAt(i) != first) {
                return true;
            }
        }
        return false;
    }

    private boolean validarCPFFull(String cpf) {
        if (cpf.length() != 14) return false;
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

    private boolean validarCPFNonDot(String cpf) {
        int len = cpf.length();
        if (len != 11 && len != 12) return false;
        EstadoCPFNonDot estado = EstadoCPFNonDot.Q0;
        for (int i = 0; i < len; i++) {
            char c = cpf.charAt(i);
            switch (estado) {
                case Q0: case Q1: case Q2: case Q3: case Q4:
                case Q5: case Q6: case Q7: case Q8:
                    if (Character.isDigit(c)) {
                        estado = EstadoCPFNonDot.values()[estado.ordinal() + 1];
                    } else {
                        estado = EstadoCPFNonDot.ERRO;
                    }
                    break;
                case Q9:
                    if (len == 11) {
                        estado = Character.isDigit(c) ? EstadoCPFNonDot.Q10 : EstadoCPFNonDot.ERRO;
                    } else {
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
        } else {
            return estado == EstadoCPFNonDot.OK;
        }
    }

    private enum EstadoRGFull { Q0, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, OK, ERRO }

    private boolean validarRGFull(String rg) {
        if (rg.length() != 12) return false;
        EstadoRGFull estado = EstadoRGFull.Q0;
        for (int i = 0; i < rg.length(); i++) {
            char c = rg.charAt(i);
            switch (estado) {
                case Q0:
                    estado = Character.isDigit(c) ? EstadoRGFull.Q1 : EstadoRGFull.ERRO;
                    break;
                case Q1:
                    estado = Character.isDigit(c) ? EstadoRGFull.Q2 : EstadoRGFull.ERRO;
                    break;
                case Q2:
                    estado = (c == '.') ? EstadoRGFull.Q3 : EstadoRGFull.ERRO;
                    break;
                case Q3:
                    estado = Character.isDigit(c) ? EstadoRGFull.Q4 : EstadoRGFull.ERRO;
                    break;
                case Q4:
                    estado = Character.isDigit(c) ? EstadoRGFull.Q5 : EstadoRGFull.ERRO;
                    break;
                case Q5:
                    estado = Character.isDigit(c) ? EstadoRGFull.Q6 : EstadoRGFull.ERRO;
                    break;
                case Q6:
                    estado = (c == '.') ? EstadoRGFull.Q7 : EstadoRGFull.ERRO;
                    break;
                case Q7:
                    estado = Character.isDigit(c) ? EstadoRGFull.Q8 : EstadoRGFull.ERRO;
                    break;
                case Q8:
                    estado = Character.isDigit(c) ? EstadoRGFull.Q9 : EstadoRGFull.ERRO;
                    break;
                case Q9:
                    estado = Character.isDigit(c) ? EstadoRGFull.Q10 : EstadoRGFull.ERRO;
                    break;
                case Q10:
                    estado = (c == '-') ? EstadoRGFull.Q11 : EstadoRGFull.ERRO;
                    break;
                case Q11:
                    estado = (Character.isDigit(c) || c == 'X') ? EstadoRGFull.OK : EstadoRGFull.ERRO;
                    break;
                default:
                    estado = EstadoRGFull.ERRO;
                    break;
            }
            if (estado == EstadoRGFull.ERRO) return false;
        }
        return estado == EstadoRGFull.OK;
    }

    private enum EstadoRGPartial { Q0, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, OK, ERRO }

    private boolean validarRGPartial(String rg) {
        if (rg.length() != 10) return false;
        EstadoRGPartial estado = EstadoRGPartial.Q0;
        for (int i = 0; i < rg.length(); i++) {
            char c = rg.charAt(i);
            switch (estado) {
                case Q0:
                    estado = Character.isDigit(c) ? EstadoRGPartial.Q1 : EstadoRGPartial.ERRO;
                    break;
                case Q1:
                    estado = Character.isDigit(c) ? EstadoRGPartial.Q2 : EstadoRGPartial.ERRO;
                    break;
                case Q2:
                    estado = Character.isDigit(c) ? EstadoRGPartial.Q3 : EstadoRGPartial.ERRO;
                    break;
                case Q3:
                    estado = Character.isDigit(c) ? EstadoRGPartial.Q4 : EstadoRGPartial.ERRO;
                    break;
                case Q4:
                    estado = Character.isDigit(c) ? EstadoRGPartial.Q5 : EstadoRGPartial.ERRO;
                    break;
                case Q5:
                    estado = Character.isDigit(c) ? EstadoRGPartial.Q6 : EstadoRGPartial.ERRO;
                    break;
                case Q6:
                    estado = Character.isDigit(c) ? EstadoRGPartial.Q7 : EstadoRGPartial.ERRO;
                    break;
                case Q7:
                    estado = Character.isDigit(c) ? EstadoRGPartial.Q8 : EstadoRGPartial.ERRO;
                    break;
                case Q8:
                    estado = (c == '-') ? EstadoRGPartial.Q9 : EstadoRGPartial.ERRO;
                    break;
                case Q9:
                    estado = (Character.isDigit(c) || c == 'X') ? EstadoRGPartial.OK : EstadoRGPartial.ERRO;
                    break;
                default:
                    estado = EstadoRGPartial.ERRO;
                    break;
            }
            if (estado == EstadoRGPartial.ERRO) return false;
        }
        return estado == EstadoRGPartial.OK;
    }

    private boolean validarRGNoSep(String rg) {
        int len = rg.length();
        if (len < 7 || len > 9) return false;
        for (int i = 0; i < len; i++) {
            char c = rg.charAt(i);
            if (i == len - 1) {
                if (!(Character.isDigit(c) || c == 'X')) return false;
            } else {
                if (!Character.isDigit(c)) return false;
            }
        }
        return true;
    }

    public boolean validarRG(String rg) {
        if (rg.contains(".")) {
            return validarRGFull(rg);
        } else if (rg.contains("-")) {
            return validarRGPartial(rg);
        } else {
            return validarRGNoSep(rg);
        }
    }
}