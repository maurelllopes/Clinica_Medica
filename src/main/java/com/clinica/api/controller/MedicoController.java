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

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados){
        repository.save(new Medico(dados));
    }
    @GetMapping
    public List<DadosListagemMedico> listar() {
        return repository.findAll().stream().map(DadosListagemMedico::new).toList();

    }

     @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Medico medico = repository.findById(id).orElseThrow(() -> new RuntimeException("Médico não encontrado"));
        model.addAttribute("medico", medico);
        return "editar"; // Página para editar médico
    }

    @PostMapping("/editar")
    @Transactional
    public String atualizar(@ModelAttribute DadosAtualizacaoMedico dados) {
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
        return "redirect:/medicos"; // Redireciona para a listagem
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id){
        repository.deleteById(id);

    }
    @PatchMapping("/desativar/{id}")
@Transactional
public String desativar(@PathVariable Long id, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "8") int size, HttpServletResponse response) throws IOException {
    // Encontrar o médico pelo ID
    Medico medico = repository.findById(id).orElseThrow(() -> new RuntimeException("Médico não encontrado"));
    
    // Desativar o médico
    Medico medicoDesativado = medico.desativar();
    
    // Salvar o médico desativado
    repository.save(medicoDesativado);
    
    // Realizar o redirecionamento com os parâmetros
    response.sendRedirect("/listagem?page=" + page + "&size=" + size);
    return null;  // Esse return pode ser nulo porque o redirecionamento é feito diretamente
}


}
