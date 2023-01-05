package com.attornatus.pessoas.mocks;

import com.attornatus.pessoas.dto.PessoaAtualizaDTO;
import com.attornatus.pessoas.model.Endereco;
import com.attornatus.pessoas.model.Pessoa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MockPessoa {

    public PessoaAtualizaDTO pessoaDadosAtualizados(Integer numero){
        PessoaAtualizaDTO pessoa = new PessoaAtualizaDTO();
        pessoa.setId(numero.longValue());

        return pessoa;
    }

    public Pessoa pessoaEntity(Integer numero){
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(new Endereco("Rua 1", "12345678", "1234",
                "Cidade 1", true));

        Pessoa pessoa = new Pessoa();
        pessoa.setId(numero.longValue());
        pessoa.setNome("Pessoa " +  numero);
        pessoa.setDataNascimento(LocalDate.parse("2000-01-01"));
        pessoa.setEnderecos(enderecos);

        return pessoa;
    }

    public List<Pessoa> mockPessoaLista(){
        List<Pessoa> pessoas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            pessoas.add(pessoaEntity(i));
        }
        return pessoas;
    }

    public Endereco mockEndereco(Integer numero){
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Novo endereco "+ numero);
        endereco.setCep("12345678");
        endereco.setNumero(numero.toString());
        endereco.setCidade("Cidade " + numero);

        return endereco;
    }

}
