package com.desafio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@AllArgsConstructor
public class ClienteFielDTO {
    private String nome;
    private String cpf;
    private double totalCompras;
}
