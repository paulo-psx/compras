package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dominio.Cliente;
import dominio.Produto;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Service
public class DataService {
    private List<Produto> produtos;
    private List<Cliente> clientes;

    @jakarta.annotation.PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        URL produtoUrl = new URL("https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/produtos-mnboX5IPl6VgG390FECTKqHsD9SkLS.json");
        URL clienteUrl = new URL("https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/clientes-Vz1U6aR3GTsjb3W8BRJhcNKmA81pVh.json");

        Produto[] produtoArray = mapper.readValue(produtoUrl, Produto[].class);
        Cliente[] clienteArray = mapper.readValue(clienteUrl, Cliente[].class);

        produtos = Arrays.asList(produtoArray);
        clientes = Arrays.asList(clienteArray);
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }
}