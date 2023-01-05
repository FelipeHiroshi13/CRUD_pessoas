package com.attornatus.pessoas.service;

import com.attornatus.pessoas.dto.*;
import com.attornatus.pessoas.model.Endereco;
import com.attornatus.pessoas.model.Pessoa;
import com.attornatus.pessoas.repository.EnderecoRepository;
import com.attornatus.pessoas.repository.PessoaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

        if(pessoa.getEnderecos() != null){
            for(Endereco endereco : pessoa.getEnderecos()){
                endereco.setPessoaId(pessoa);
            }

            if(pessoa.semEnderecoPrincipal()){
                pessoa.getEnderecos().get(0).setEnderecoPrincipal(true);
            }
        }

        pessoaRepository.save(pessoa);

        return modelMapper.map(pessoa, PessoaRegistroDTO.class);
    }

    public List<PessoaListaDTO> listarPessoas() {
        return pessoaRepository
                .findAll()
                .stream()
                .map(p -> modelMapper.map(p, PessoaListaDTO.class)).collect(Collectors.toList());
    }


    public PessoaRegistroDTO pessoaDetalhes(Long id) {
        Pessoa pessoa = pessoaRepository.getReferenceById(id);

        return modelMapper.map(pessoa, PessoaRegistroDTO.class);
    }


    public EnderecosPessoaDTO listarEnderecosPessoa(Long id) {
        Pessoa pessoa = pessoaRepository.getReferenceById(id);

        return modelMapper.map(pessoa, EnderecosPessoaDTO.class);
    }

    public EnderecoDTO listarEnderecoPrincipalPessoa(Long id){
        Pessoa pessoa = pessoaRepository.getReferenceById(id);

        Endereco endereco = enderecoRepository.findByPessoaIdAndEnderecoPrincipalTrue(pessoa);
        return modelMapper.map(endereco, EnderecoDTO.class);
    }


    public PessoaAtualizaDTO atualizarPessoa(PessoaAtualizaDTO pessoaDados) {
        Pessoa pessoa = pessoaRepository.getReferenceById(pessoaDados.getId());
        modelMapper.map(pessoaDados, pessoa);
        pessoaRepository.save(pessoa);


        return modelMapper.map(pessoa, PessoaAtualizaDTO.class);
    }

    public EnderecoDTO registrarEnderecoPessoa(Long id, EnderecoDTO enderecoDto) {
        Pessoa pessoa = pessoaRepository.getReferenceById(id);
        Endereco novoEndereco = modelMapper.map(enderecoDto, Endereco.class);
        novoEndereco.setPessoaId(pessoa);

        if(pessoa.getEnderecos().size() == 0){
            novoEndereco.setEnderecoPrincipal(true);
        }

        if(pessoa.getEnderecos().size() > 0 && novoEndereco.getEnderecoPrincipal()){
            Endereco enderecoPrincipalAntigo = modelMapper
                    .map(listarEnderecoPrincipalPessoa(id), Endereco.class);
            enderecoPrincipalAntigo.setEnderecoPrincipal(false);
            enderecoPrincipalAntigo.setPessoaId(pessoa);

            enderecoRepository.save(enderecoPrincipalAntigo);
        }

        enderecoRepository.save(novoEndereco);

        return modelMapper.map(novoEndereco, EnderecoDTO.class);
    }
}
