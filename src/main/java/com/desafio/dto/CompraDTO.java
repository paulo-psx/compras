package com.desafio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@AllArgsConstructor
public class CompraDTO {
    private String nomeCliente;
    private String cpfCliente;
    private String tipoVinho;
    private int quantidade;
    private double valorTotal;
}