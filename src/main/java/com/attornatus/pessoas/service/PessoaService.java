package com.attornatus.pessoas.service;

import com.attornatus.pessoas.dto.*;
import com.attornatus.pessoas.model.Endereco;
import com.attornatus.pessoas.model.Pessoa;
import com.attornatus.pessoas.repository.EnderecoRepository;
import com.attornatus.pessoas.repository.PessoaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PessoaRegistroDTO registrarPessoa(PessoaRegistroDTO pessoaRegistro) {
        Pessoa pessoa = modelMapper.map(pessoaRegistro, Pessoa.class);
        pessoa.setRegistradoEm(LocalDateTime.now());
        pessoa.setModificadoEm(LocalDateTime.now());

        for(Endereco endereco : pessoa.getEnderecos()){
            endereco.setPessoaId(pessoa);
        }

        pessoaRepository.save(pessoa);

        return modelMapper.map(pessoa, PessoaRegistroDTO.class);
    }

    public Page<PessoaListaDTO> listarPessoas(Pageable paginacao) {
        return pessoaRepository
                .findAll(paginacao)
                .map(p -> modelMapper.map(p, PessoaListaDTO.class));
    }


    public PessoaRegistroDTO pessoaDetalhes(Long id) {
        Pessoa pessoa = pessoaRepository.getReferenceById(id);

        return modelMapper.map(pessoa, PessoaRegistroDTO.class);
    }

    //TODO: DEIXAR MAIS SIMPLES
    public EnderecosPessoaDTO listarEnderecosPessoa(Long id) {
        Pessoa pessoa = pessoaRepository.getReferenceById(id);

        return modelMapper.map(pessoa, EnderecosPessoaDTO.class);
    }

    public EnderecoDTO listarEnderecoPrincipalPessoa(Long id){
        Pessoa pessoa = pessoaRepository.getReferenceById(id);

        Endereco endereco = enderecoRepository.findByPessoaIdAndEnderecoPrincipalTrue(pessoa);
        return modelMapper.map(endereco, EnderecoDTO.class);
    }


    public PessoaAtualizaDTO atualizar(PessoaAtualizaDTO pessoaDados) {
        Pessoa pessoa = pessoaRepository.getReferenceById(pessoaDados.getId());
        modelMapper.map(pessoaDados, pessoa);
        pessoaRepository.save(pessoa);


        return modelMapper.map(pessoa, PessoaAtualizaDTO.class);
    }

    public EnderecoDTO registrarEnderecoPessoa(Long id, EnderecoDTO enderecoDto) {
        Pessoa pessoa = pessoaRepository.getReferenceById(id);
        Endereco novoEndereco = modelMapper.map(enderecoDto, Endereco.class);
        novoEndereco.setPessoaId(pessoa);

        enderecoRepository.save(novoEndereco);

        return modelMapper.map(novoEndereco, EnderecoDTO.class);
    }
}
