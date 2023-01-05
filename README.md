# API REST - Spring Boot 

### CRUD Pessoas

![Spring Boot 3.0](https://img.shields.io/badge/Spring%20Boot-3.0-brightgreen)

API REST para cadastro de pessoas, com os campos de nome, data de nascimento e endereços.
Um usuário pode ter vários endereços, mas apenas um será o principal.

### Endpoints para chamada da API

#### Registro de Pessoa (POST)

```
POST http://localhost:8080/pessoas

{
    "nome": "Pessoa 1",
    "dataNascimento": "01/01/2000",
    "enderecos":[
        {
            "logradouro": "rua 1",
            "cep": "12345678",
            "numero": "132",
            "cidade": "Cidade 1",
            "enderecoPrincipal": true
        },
        {
            "logradouro": "rua 2",
            "cep": "79115123",
            "numero": "345",
            "cidade": "Cidade 2",
            "enderecoPrincipal": false
        }
    ]
}

RESPONSE: HTTP 201 (Created)
```

### Registro de um novo endereço (Post)
```
POST http://localhost:8080/pessoas/1/enderecos
{
    "logradouro": "Novo Endereco",
    "cep": "12378946",
    "numero": "777",
    "cidade": "Nova Cidade",
    "enderecoPrincipal": false
}
```

### Editar uma Pessoa (PUT)

```
PUT http://localhost:8080/pessoas
{
    "id": 1,
    "nome": "Pessoa Editada",
        "enderecos":[
        {
            "logradouro": "Rua 1 Editada",
            "cep": "12345678",
            "numero": "132",
            "cidade": "Cidade Editada",
            "enderecoPrincipal": true
        }
    ]
}
```

### Consultas 

```
--Listar pessoas
http://localhost:8080/pessoas

--Consulta Pessoa
http://localhost:8080/pessoas/1

--Consulta Lista de endereços da pessoa
http://localhost:8080/pessoas/1/enderecos

--Listar Endereco Principal
http://localhost:8080/pessoas/1/endereco-principal
```


## Implementação de Testes 

- Teste salvar e buscar pessoa;
- Teste atualizar dados da pessoa;
- Teste listar pessoas;
- Teste inserir endereço secundário;
- Teste inserir endereço principal;
- Teste retornar endereço principal;
- Teste listar endereços da pessoa.

## Tratamento de Erro

- Erro 404: EntityNotFoundException;
- Erro 400: MethodArgumentNotValidException.