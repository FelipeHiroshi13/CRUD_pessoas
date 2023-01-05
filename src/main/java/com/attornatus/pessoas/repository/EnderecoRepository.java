package com.attornatus.pessoas.repository;

import com.attornatus.pessoas.model.Endereco;
import com.attornatus.pessoas.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    Endereco findByPessoaIdAndEnderecoPrincipalTrue(Pessoa pessoa);
}
