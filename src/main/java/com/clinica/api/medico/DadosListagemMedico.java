package com.clinica.api.medico;

public record DadosListagemMedico(Long id, String nome, String email, String telefone) {

public DadosListagemMedico(Medico medico){
    this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getTelefone());
}
}