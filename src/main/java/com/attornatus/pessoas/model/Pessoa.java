package com.attornatus.pessoas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pessoas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @OneToMany(mappedBy = "pessoaId", cascade = CascadeType.ALL)
    private List<Endereco> enderecos;

    private LocalDateTime registradoEm;

    private LocalDateTime modificadoEm;

}
