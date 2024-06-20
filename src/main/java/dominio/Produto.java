package dominio;

import lombok.Data;

@Data
public class Produto {
    private int codigo;
    private String tipoVinho;
    private double preco;
    private String safra;
    private int anoCompra;
}
