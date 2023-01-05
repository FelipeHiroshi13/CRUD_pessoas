package com.attornatus.pessoas.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnderecoDTO {
    private Long id;
    private String logradouro;
    @Pattern(regexp = "\\d{8}")
    private String cep;
    private String numero;
    private String cidade;
    private boolean enderecoPrincipal;

    public EnderecoDTO(String logradouro, String cep, String numero, String cidade, boolean enderecoPrincipal) {
        this.logradouro = logradouro;
        this.cep = cep;
        this.numero = numero;
        this.cidade = cidade;
        this.enderecoPrincipal = enderecoPrincipal;
    }
}
