package com.attornatus.pessoas.controller;

import com.attornatus.pessoas.dto.*;
import com.attornatus.pessoas.service.PessoaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("pessoas")
public class PessoaController {

    @Autowired
    private PessoaService service;

    @PostMapping
    @Transactional
    public ResponseEntity<PessoaRegistroDTO> registrar(@RequestBody @Valid PessoaRegistroDTO pessoaDTO,
                                                            UriComponentsBuilder uriBuilder){
        PessoaRegistroDTO pessoa =  service.registrarPessoa(pessoaDTO);
        URI uri = uriBuilder.path("pessoas/{id}").buildAndExpand(pessoa.getId()).toUri();

        return ResponseEntity.created(uri).body(pessoa);
    }


    @GetMapping
    public ResponseEntity<Page<PessoaListaDTO>> listaPessoas
            (@PageableDefault(size = 10, sort = {"nome"})Pageable paginacao){
        var page = service.listarPessoas(paginacao);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaRegistroDTO> detalhesPessoa (@PathVariable @NotNull Long id){
        PessoaRegistroDTO pessoaRegistroDTO = service.pessoaDetalhes(id);

        return ResponseEntity.ok(pessoaRegistroDTO);
    }

    @PostMapping("/{id}/enderecos")
    @Transactional
    public ResponseEntity<EnderecoDTO> registrarEndereco (@PathVariable @NotNull Long id,
                                                                 @RequestBody @Valid EnderecoDTO endereco){
        EnderecoDTO novoEndereco = service.registrarEnderecoPessoa(id, endereco);

        return ResponseEntity.ok(novoEndereco);
    }

    @GetMapping("/{id}/enderecos")
    public ResponseEntity<EnderecosPessoaDTO> enderecosPessoa (@PathVariable @NotNull Long id){
        EnderecosPessoaDTO enderecos = service.listarEnderecosPessoa(id);

        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/{id}/endereco-principal")
    public ResponseEntity<EnderecoDTO> enderecoPrincipal (@PathVariable @NotNull Long id){
        EnderecoDTO enderecoPrincipal = service.listarEnderecoPrincipalPessoa(id);

        return ResponseEntity.ok(enderecoPrincipal);
    }


    @PutMapping
    @Transactional
    public ResponseEntity atualizaDadosPessoa(@RequestBody @Valid PessoaAtualizaDTO dados){
        PessoaAtualizaDTO pessoaDadosAtualizados = service.atualizar(dados);

        return ResponseEntity.ok(pessoaDadosAtualizados);
    }




}
