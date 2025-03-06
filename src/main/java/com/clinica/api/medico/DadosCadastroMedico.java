package com.clinica.api.medico;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroMedico(

    Long id,

    @NotBlank
    String nome, 
    
    @NotBlank
    String email,

    @NotBlank
    String telefone) {

}
