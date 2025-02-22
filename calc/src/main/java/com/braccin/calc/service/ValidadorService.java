package com.braccin.calc.service;

import com.braccin.calc.model.Estado;
import com.braccin.calc.model.TipoDocumento;
import org.springframework.stereotype.Service;

@Service
public class ValidadorService {

    public boolean validarDocumento(String documento, TipoDocumento tipo) {
        Estado estado = Estado.INICIAL;
        
        for (char c : documento.toCharArray()) {
            if (!Character.isDigit(c) && !(tipo == TipoDocumento.RG && c == 'X')) {
                return false;
            }
            
            switch (estado) {
                case INICIAL:
                    estado = Character.isDigit(c) ? Estado.DIGITO : Estado.ERRO;
                    break;
                case DIGITO:
                    estado = Character.isDigit(c) ? Estado.DIGITO : Estado.ERRO;
                    break;
                default:
                    estado = Estado.ERRO;
                    break;
            }

            if (estado == Estado.ERRO) return false;
        }

        return (tipo == TipoDocumento.CPF && documento.length() == 11) ||
               (tipo == TipoDocumento.RG && documento.length() >= 7 && documento.length() <= 9);
    }
}