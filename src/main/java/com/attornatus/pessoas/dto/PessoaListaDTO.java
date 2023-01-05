package com.attornatus.pessoas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PessoaListaDTO {
    private Long id;
    private String nome;
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate dataNascimento;

}
