package dominio;

import java.util.List;

import lombok.Data;


@Data
public class Cliente {
    private String nome;
    private String cpf;
    private List<Compra> compras;
}