package br.com.arenco.arenco_clientes.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para exportação de clientes na planilha de importação.
 * Todos os campos são String para manter a formatação original (ex: CPF com pontos).
 */
@Setter
@Getter
public class ClienteExportDTO {

    private String nome;
    private String tipoPessoa;       // "F" (Física) ou "J" (Jurídica)
    private String sexo;             // "M" ou "F" // (Nao temos essa info)
    private String cpfCnpj;
    private String dataNascimento; // contrato
    private String nacionalidade;
    private String estadoCivil; // (Nao temos essa info)
    private String regimeCasamento; // (Nao temos essa info)
    private String nomeConjuge; // contrato
    private String cpfConjuge; // contrato
    private String dataNascimentoConjuge; // (Nao temos essa info)
    private String enderecoResidencial;
    private String numeroEndereco;
    private String complementoEndereco;
    private String bairro;
    private String municipio;
    private String cep;
    private String telefoneCelular;
    private String email;

    // --- Getters e Setters ---

}