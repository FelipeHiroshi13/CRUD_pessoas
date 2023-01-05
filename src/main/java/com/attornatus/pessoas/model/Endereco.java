package com.attornatus.pessoas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "enderecos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String logradouro;

    @Pattern(regexp = "\\d{8}")
    private String cep;

    private String numero;

    private String cidade;

    private Boolean enderecoPrincipal;

    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoaId;
}
