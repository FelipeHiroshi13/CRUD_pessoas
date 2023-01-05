package com.attornatus.pessoas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PessoaRegistroDTO {
        private Long id;
        @NotBlank
        private String nome;
        @JsonFormat(pattern="dd/MM/yyyy")
        private LocalDate dataNascimento;
        @Valid
        private List<EnderecoDTO> enderecos;
}
