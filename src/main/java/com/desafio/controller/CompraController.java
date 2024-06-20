package com.desafio.controller;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.dto.ClienteFielDTO;
import com.desafio.dto.CompraDTO;

import dominio.Cliente;
import dominio.Compra;
import dominio.Produto;
import services.DataService;

@RestController
@RequestMapping("/api")
public class CompraController {
    private final DataService dataService;

    public CompraController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/compras")
    public List<CompraDTO> getCompras() {
        List<Cliente> clientes = dataService.getClientes();
        List<Produto> produtos = dataService.getProdutos();
        List<CompraDTO> compras = new ArrayList<>();

        for (Cliente cliente : clientes) {
            for (Compra compra : cliente.getCompras()) {
                Produto produto = produtos.stream()
                        .filter(p -> p.getCodigo() == compra.getCodigo())
                        .findFirst()
                        .orElse(null);

                if (produto != null) {
                    CompraDTO dto = new CompraDTO(cliente.getNome(), cliente.getCpf(), produto.getTipoVinho(), compra.getQuantidade(), produto.getPreco() * compra.getQuantidade());
                    compras.add(dto);
                }
            }
        }
        compras.sort(Comparator.comparingDouble(CompraDTO::getValorTotal));
        return compras;
    }

    @GetMapping("/maior-compra/{ano}")
    public CompraDTO getMaiorCompra(@PathVariable int ano) {
        List<Cliente> clientes = dataService.getClientes();
        List<Produto> produtos = dataService.getProdutos();
        List<CompraDTO> compras = new ArrayList<>();

        for (Cliente cliente : clientes) {
            for (Compra compra : cliente.getCompras()) {
                Produto produto = produtos.stream()
                        .filter(p -> p.getCodigo() == compra.getCodigo() && p.getAnoCompra() == ano)
                        .findFirst()
                        .orElse(null);

                if (produto != null) {
                    CompraDTO dto = new CompraDTO(cliente.getNome(), cliente.getCpf(), produto.getTipoVinho(), compra.getQuantidade(), produto.getPreco() * compra.getQuantidade());
                    compras.add(dto);
                }
            }
        }
        return compras.stream().max(Comparator.comparingDouble(CompraDTO::getValorTotal)).orElse(null);
    }

    @GetMapping("/clientes-fieis")
    public List<ClienteFielDTO> getClientesFieis() {
        List<Cliente> clientes = dataService.getClientes();
        Map<String, ClienteFielDTO> clienteFielMap = new HashMap<>();
        List<Produto> produtos = dataService.getProdutos();

        for (Cliente cliente : clientes) {
            double totalCompras = 0;
            for (Compra compra : cliente.getCompras()) {
                Produto produto = produtos.stream()
                        .filter(p -> p.getCodigo() == compra.getCodigo())
                        .findFirst()
                        .orElse(null);

                if (produto != null) {
                    totalCompras += produto.getPreco() * compra.getQuantidade();
                }
            }

            clienteFielMap.put(cliente.getCpf(), new ClienteFielDTO(cliente.getNome(), cliente.getCpf(), totalCompras));
        }
        return clienteFielMap.values().stream()
                .sorted(Comparator.comparingDouble(ClienteFielDTO::getTotalCompras).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    @GetMapping("/recomendacao/{clienteId}/{tipo}")
    public Produto getRecomendacao(@PathVariable String clienteId, @PathVariable String tipo) {
        Cliente cliente = dataService.getClientes().stream()
                .filter(c -> c.getCpf().equals(clienteId))
                .findFirst()
                .orElse(null);

        if (cliente == null) return null;

        Map<String, Long> tipoCount = cliente.getCompras().stream()
                .collect(Collectors.groupingBy(compra -> {
                    Produto produto = dataService.getProdutos().stream()
                            .filter(p -> p.getCodigo() == compra.getCodigo())
                            .findFirst()
                            .orElse(null);
                    return produto != null ? produto.getTipoVinho() : null;
                }, Collectors.counting()));

        String tipoMaisComprado = tipoCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        return dataService.getProdutos().stream()
                .filter(p -> p.getTipoVinho().equals(tipoMaisComprado))
                .findFirst()
                .orElse(null);
    }
}