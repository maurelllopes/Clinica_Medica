// Pacote que contém os controladores da API da clínica
package com.clinica.api.controller;

// Importação de bibliotecas e classes necessárias
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.clinica.api.medico.DadosCadastroMedico;
import com.clinica.api.medico.DadosListagemMedico;
import com.clinica.api.medico.DadosAtualizacaoMedico;
import com.clinica.api.medico.Medico;
import com.clinica.api.medico.MedicoRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

// Anotação que indica que esta é uma classe de controlador de API REST
@RestController
// Define o endpoint base para todas as requisições neste controlador
@RequestMapping("medicos")

public class MedicoController {
    // Injeção de dependência para o repositório de médicos (MedicoRepository)
    @Autowired
    private MedicoRepository repository;
    // Endpoint POST para cadastrar um novo médico
    @PostMapping 
    @Transactional // Transação para garantir que a operação de salvar seja atômica
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados){
    // Cria um novo médico com os dados fornecidos e o salva no banco de dados
        repository.save(new Medico(dados));
    }
    // Endpoint GET para listar todos os médicos
    @GetMapping
    public List<DadosListagemMedico> listar() {
        // Recupera todos os médicos e os converte para um formato de listagem adequado
        return repository.findAll().stream()
        .map(DadosListagemMedico::new)// Converte cada médico para um objeto de listagem
        .toList();// Retorna a lista

    }
    // Endpoint GET para editar as informações de um médico (formulário)
     @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
         // Procura o médico pelo ID, se não encontrar, lança uma exceção
        Medico medico = repository.findById(id).orElseThrow(() -> new RuntimeException("Médico não encontrado"));
        // Adiciona o médico ao modelo para ser exibido na página
        model.addAttribute("medico", medico);
        // Retorna a visão (página) de edição do médico
        return "editar"; // Página para editar médico
    }
    // Endpoint POST para atualizar as informações de um médico
    @PostMapping("/editar")
    @Transactional
    public String atualizar(@ModelAttribute DadosAtualizacaoMedico dados) {
        // Recupera o médico pela ID fornecida
        var medico = repository.getReferenceById(dados.id());
        // Atualiza as informações do médico com os novos dados
        medico.atualizarInformacoes(dados);
        // Redireciona para a lista de médicos após a atualização
        return "redirect:/medicos"; // Redireciona para a listagem
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id){
        repository.deleteById(id);

    }
    // Endpoint PATCH para desativar um médico
    @PatchMapping("/desativar/{id}")
@Transactional
public String desativar(@PathVariable Long id, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "8") int size, HttpServletResponse response) throws IOException {
    // Encontrar o médico pelo ID
    Medico medico = repository.findById(id).orElseThrow(() -> new RuntimeException("Médico não encontrado"));
    
    // Desativar o médico
    Medico medicoDesativado = medico.desativar();
    
    // Salvar o médico desativado
    repository.save(medicoDesativado);
    
    // Realiza o redirecionamento para a listagem de médicos com os parâmetros de paginação
    response.sendRedirect("/listagem?page=" + page + "&size=" + size);
    return null;  // Esse return pode ser nulo porque o redirecionamento é feito diretamente
}


}
