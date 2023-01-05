package com.attornatus.pessoas.dto;

import com.attornatus.pessoas.model.Endereco;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnderecoDTO {
    private Long id;
    private String logradouro;
    private String cep;
    private String numero;
    private String cidade;
    private boolean enderecoPrincipal;
}
