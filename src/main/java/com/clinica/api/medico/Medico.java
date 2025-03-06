package com.clinica.api.medico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "medicos")
@Entity(name = "Medico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Medico {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email; 
    private String telefone;
    private Boolean ativo;

    

//CONSTRUTOR
public Medico(DadosCadastroMedico dados){
  // this.id = dados.id();
    this.nome = dados.nome();
    this.email = dados.email();
    this.telefone = dados.telefone();
    this.ativo = true;
}    

public void atualizarInformacoes(DadosAtualizacaoMedico dados){
    if (dados.nome() != null){
        this.nome = dados.nome();
    }
    if (dados.telefone() != null){
        this.telefone = dados.telefone();
    }
  
}
  // Método para "desativar" o médico, criando um novo Medico com 'ativo = false'
  public Medico desativar(){
    return new Medico(this.id, this.nome, this.email, this.telefone, false);
}

}
