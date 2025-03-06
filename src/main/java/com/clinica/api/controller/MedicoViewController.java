package com.clinica.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;

import com.clinica.api.medico.DadosCadastroMedico;
import com.clinica.api.medico.DadosAtualizacaoMedico;
import com.clinica.api.medico.Medico;
import com.clinica.api.medico.MedicoRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import jakarta.validation.Valid;


@Controller
public class MedicoViewController {
    
    private final MedicoRepository repository;
    
    @Autowired
    public MedicoViewController(MedicoRepository repository) {
        this.repository = repository;
    }
    
    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("medico", new DadosCadastroMedico(null, "", "", ""));
        return "cadastro";
    }
    
    @PostMapping("/cadastro")
    public String cadastrar(@Valid DadosCadastroMedico dados, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "cadastro";
        }
        Medico medico = new Medico(dados);
        repository.save(medico);
        return "redirect:/listagem";
    }
    
    @GetMapping("/listagem")
    public String listar(Model model, @PageableDefault(size = 8, sort = "nome") Pageable pageable) {
        Page<Medico> pagina = repository.findAll(pageable);
        model.addAttribute("pagina", pagina);
        return "listagem";
    }
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Medico medico = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado"));
        model.addAttribute("medico", medico);
        return "editar";
    }

    // **Novo método para processar a edição**
    @PostMapping("/editar/{id}")
    public String atualizar(@PathVariable Long id, @Valid DadosAtualizacaoMedico dados, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editar";
        }
    
        Medico medico = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Médico não encontrado"));
        medico.atualizarInformacoes(new DadosAtualizacaoMedico(id, dados.nome(), dados.telefone()));
        repository.save(medico);
    
        return "redirect:/listagem";
    }}

    
