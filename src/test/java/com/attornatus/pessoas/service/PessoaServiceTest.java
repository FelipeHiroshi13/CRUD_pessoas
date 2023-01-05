package com.attornatus.pessoas.service;

import com.attornatus.pessoas.AplicationConfigTest;
import com.attornatus.pessoas.dto.*;
import com.attornatus.pessoas.mocks.MockPessoa;
import com.attornatus.pessoas.model.Endereco;
import com.attornatus.pessoas.model.Pessoa;
import com.attornatus.pessoas.repository.EnderecoRepository;
import com.attornatus.pessoas.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pessoa Service")
class PessoaServiceTest extends AplicationConfigTest {

    MockPessoa input;

    @MockBean
    private PessoaRepository pessoaRepository;

    @MockBean
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PessoaService pessoaService;


    @BeforeEach
    void setUpMocks() throws Exception {
        input = new MockPessoa();
        MockitoAnnotations.openMocks(this);
    }

    public PessoaRegistroDTO salvarPessoa(Pessoa pessoa, PessoaRegistroDTO dto){
        Mockito.when(pessoaRepository.save(pessoa)).thenReturn(pessoa);
        return pessoaService.registrarPessoa(dto);
    }


    @Test
    @DisplayName("Deve salvar e buscar a Pessoa Registrada")
    public void deveRegistrarPessoa(){
        Pessoa pessoa = input.pessoaEntity(1);
        PessoaRegistroDTO pessoaRegistroDTO = modelMapper.map(pessoa, PessoaRegistroDTO.class);

        PessoaRegistroDTO pessoaSalva = salvarPessoa(pessoa, pessoaRegistroDTO);

        Mockito.when(pessoaRepository.getReferenceById(pessoaSalva.getId()))
                .thenReturn(pessoa);
        PessoaRegistroDTO pessoaBuscar = pessoaService.pessoaDetalhes(pessoaSalva.getId());

        assertNotNull(pessoaBuscar);
        assertEquals("Pessoa 1", pessoaBuscar.getNome());
        assertEquals(LocalDate.parse("2000-01-01"), pessoaBuscar.getDataNascimento());

        assertEquals(pessoa.getEnderecos().get(0).getLogradouro(), "Rua 1");
        assertEquals(pessoa.getEnderecos().get(0).getCep(), "12345678");
        assertEquals(pessoa.getEnderecos().get(0).getNumero(), "1234");
        assertEquals(pessoa.getEnderecos().get(0).getCidade(), "Cidade 1");
        assertEquals(pessoa.getEnderecos().get(0).getEnderecoPrincipal(), true);
    }

    @Test
    @DisplayName("Deve editar dados da pessoa")
    public void deveEditarPessoa(){
        Pessoa pessoa = input.pessoaEntity(1);
        PessoaRegistroDTO pessoaRegistroDTO = modelMapper.map(pessoa, PessoaRegistroDTO.class);

        PessoaRegistroDTO pessoaSalva = salvarPessoa(pessoa, pessoaRegistroDTO);

        PessoaAtualizaDTO pessoaEditada = input.pessoaDadosAtualizados(1);
        pessoaEditada.setNome("Pessoa Editada");
        pessoaEditada.setDataNascimento(LocalDate.now());

        Mockito.when(pessoaRepository.getReferenceById(pessoaEditada.getId()))
                .thenReturn(pessoa);

        PessoaAtualizaDTO pessoaBuscar = pessoaService.atualizarPessoa(pessoaEditada);

        assertNotNull(pessoaBuscar);
        assertEquals("Pessoa Editada", pessoaBuscar.getNome());
        assertEquals(LocalDate.now(), pessoaBuscar.getDataNascimento());

        Endereco enderecoPessoa = pessoa.getEnderecos().get(0);

        assertEquals("Rua 1", enderecoPessoa.getLogradouro());
        assertEquals("12345678", enderecoPessoa.getCep());
        assertEquals("1234", enderecoPessoa.getNumero());
        assertEquals("Cidade 1", enderecoPessoa.getCidade());
        assertTrue(enderecoPessoa.getEnderecoPrincipal());
    }


    @Test
    @DisplayName("Deve listar varias pessoas")
    public void deveListarPessoas(){
        Pageable pageable = PageRequest.of(0, 10);
        List<Pessoa> lista = input.mockPessoaLista();


        for(Pessoa pessoa : lista){
            salvarPessoa(pessoa, modelMapper.map(pessoa, PessoaRegistroDTO.class));
        }

        Mockito.when(pessoaRepository.findAll()).thenReturn(lista);

        var pessoas = pessoaService.listarPessoas();

        assertEquals(10, pessoas.size());

        for(PessoaListaDTO pessoa : pessoas){
            PessoaListaDTO pessoaSalva = pessoas.get(pessoa.getId().intValue());

            assertNotNull(pessoaSalva);
            assertEquals(pessoa.getNome(), pessoaSalva.getNome());
            assertEquals(pessoa.getDataNascimento(), pessoaSalva.getDataNascimento());
        }
    }

    @Test
    @DisplayName("Deve inserir endereco secundario para a Pessoa")
    public void deveInserirEnderecoPessoa(){
        Pessoa pessoa = input.pessoaEntity(1);
        PessoaRegistroDTO pessoaRegistroDTO = modelMapper.map(pessoa, PessoaRegistroDTO.class);

        PessoaRegistroDTO pessoaSalva = salvarPessoa(pessoa, pessoaRegistroDTO);

        Endereco novoEndereco = input.mockEndereco(2);
        EnderecoDTO novoEnderecoDTO = modelMapper.map(novoEndereco, EnderecoDTO.class);

        novoEndereco.setEnderecoPrincipal(false);

        Mockito.when(pessoaRepository.getReferenceById(pessoaSalva.getId()))
                .thenReturn(pessoa);
        Mockito.when(enderecoRepository.save(novoEndereco)).thenReturn(novoEndereco);
        var enderecoSalvo = pessoaService.registrarEnderecoPessoa(pessoaSalva.getId(), novoEnderecoDTO);


        assertNotNull(enderecoSalvo);
        assertEquals(novoEnderecoDTO.getLogradouro(), enderecoSalvo.getLogradouro());
        assertEquals(novoEnderecoDTO.getCep(), enderecoSalvo.getCep());
        assertEquals(novoEnderecoDTO.getNumero(), enderecoSalvo.getNumero());
        assertEquals(novoEnderecoDTO.getCidade(), enderecoSalvo.getCidade());
        assertFalse(enderecoSalvo.isEnderecoPrincipal());
    }

    @Test
    @DisplayName("Deve inserir novo endereco principal")
    public void deveInserirNovoEnderecoPrincipalPessoa(){
        Pessoa pessoa = input.pessoaEntity(1);
        PessoaRegistroDTO pessoaRegistroDTO = modelMapper.map(pessoa, PessoaRegistroDTO.class);

        PessoaRegistroDTO pessoaSalva = salvarPessoa(pessoa, pessoaRegistroDTO);

        Endereco novoEndereco = input.mockEndereco(2);
        novoEndereco.setEnderecoPrincipal(true);
        EnderecoDTO novoEnderecoDTO = modelMapper.map(novoEndereco, EnderecoDTO.class);


        Mockito.when(pessoaRepository.getReferenceById(pessoa.getId()))
                .thenReturn(pessoa);
        Mockito.when(enderecoRepository.findByPessoaIdAndEnderecoPrincipalTrue(pessoa))
                .thenReturn(pessoa.getEnderecos().get(0));
        Mockito.when(enderecoRepository.save(pessoa.getEnderecos().get(0)))
                .thenReturn(pessoa.getEnderecos().get(0));

        Mockito.when(enderecoRepository.save(novoEndereco)).thenReturn(novoEndereco);
        EnderecoDTO novoEnderecoSalvo = pessoaService.registrarEnderecoPessoa(pessoa.getId(), novoEnderecoDTO);

        assertEquals("Novo endereco 2", novoEnderecoSalvo.getLogradouro());
        assertEquals("12345678", novoEnderecoSalvo.getCep());
        assertEquals("2", novoEnderecoSalvo.getNumero());
        assertEquals("Cidade 2", novoEnderecoSalvo.getCidade());
        assertTrue(novoEnderecoSalvo.isEnderecoPrincipal());

    }

    @Test
    @DisplayName("Deve listar enderecos por Pessoa")
    public void deveListarEnderecosPorPessoa(){
        Pessoa pessoa = input.pessoaEntity(1);
        pessoa.getEnderecos().add(new Endereco("Rua 2", "87654321", "4321",
                "Cidade 2", false));
        PessoaRegistroDTO pessoaRegistroDTO = modelMapper.map(pessoa, PessoaRegistroDTO.class);

        PessoaRegistroDTO pessoaSalva = salvarPessoa(pessoa, pessoaRegistroDTO);

        Mockito.when(pessoaRepository.getReferenceById(pessoaSalva.getId()))
                .thenReturn(pessoa);
        EnderecosPessoaDTO enderecos = pessoaService.listarEnderecosPessoa(pessoa.getId());

        List<EnderecoDTO> enderecosSalvos = enderecos.getEnderecos();

        assertNotNull(enderecosSalvos);
        assertEquals(2, enderecosSalvos.size());

        EnderecoDTO primeiroEndereco = enderecosSalvos.get(0);

        assertEquals("Rua 1", primeiroEndereco.getLogradouro());
        assertEquals("12345678", primeiroEndereco.getCep());
        assertEquals("1234", primeiroEndereco.getNumero());
        assertEquals("Cidade 1", primeiroEndereco.getCidade());
        assertTrue(primeiroEndereco.isEnderecoPrincipal());

        EnderecoDTO segundoEndereco = enderecosSalvos.get(1);

        assertEquals("Rua 2", segundoEndereco.getLogradouro());
        assertEquals("87654321", segundoEndereco.getCep());
        assertEquals("4321", segundoEndereco.getNumero());
        assertEquals("Cidade 2", segundoEndereco.getCidade());
        assertFalse(segundoEndereco.isEnderecoPrincipal());
    }


    @Test
    @DisplayName("Deve listar o endereco principal da Pessoa")
    public void deveListarOEdenrecoPrincipalDaPessoa(){
        Pessoa pessoa = input.pessoaEntity(1);
        pessoa.getEnderecos().add(new Endereco("Rua 2", "87654321", "4321",
                "Cidade 2", false));
        PessoaRegistroDTO pessoaRegistroDTO = modelMapper.map(pessoa, PessoaRegistroDTO.class);

        PessoaRegistroDTO pessoaSalva = salvarPessoa(pessoa, pessoaRegistroDTO);

        Endereco endereco = pessoa.getEnderecos().get(0);

        Mockito.when(pessoaRepository.getReferenceById(pessoa.getId()))
                .thenReturn(pessoa);
        Mockito.when(enderecoRepository.findByPessoaIdAndEnderecoPrincipalTrue(pessoa)).thenReturn(endereco);
        EnderecoDTO enderecoPrincipalPessoa = pessoaService.listarEnderecoPrincipalPessoa(pessoa.getId());

        assertNotNull(enderecoPrincipalPessoa);
        assertEquals("Rua 1", enderecoPrincipalPessoa.getLogradouro());
        assertEquals( "12345678", enderecoPrincipalPessoa.getCep());
        assertEquals("1234", enderecoPrincipalPessoa.getNumero());
        assertEquals("Cidade 1", enderecoPrincipalPessoa.getCidade());
        assertTrue(enderecoPrincipalPessoa.isEnderecoPrincipal());

    }
}