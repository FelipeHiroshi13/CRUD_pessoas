package com.attornatus.pessoas.dto;

import com.attornatus.pessoas.model.Endereco;
import com.attornatus.pessoas.model.Pessoa;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class EnderecosPessoaDTO {
    private List<EnderecoDTO> enderecos;
}
