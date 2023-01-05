package com.attornatus.pessoas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PessoaAtualizaDTO {
    private Long id;
    private String nome;
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate dataNascimento;
    private List<EnderecoDTO> enderecos;
}
