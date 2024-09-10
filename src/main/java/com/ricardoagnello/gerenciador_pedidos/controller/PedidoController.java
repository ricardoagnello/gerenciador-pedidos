package com.ricardoagnello.gerenciador_pedidos.controller;

import com.ricardoagnello.gerenciador_pedidos.entity.Pedido;
import com.ricardoagnello.gerenciador_pedidos.service.PedidoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5000"})
public class PedidoController {

    @Autowired
    PedidoService pedidoService;

    @PostMapping("/criar-pedido")
    public ResponseEntity<Pedido> criarPedido(@Valid @RequestBody Pedido pedido){
        Pedido novoPedido = pedidoService.criarPedido(pedido);
        return ResponseEntity.ok(novoPedido);
    }

    @PutMapping("/atualizar-pedido/{pedidoId}")
    public ResponseEntity<Pedido> atualizarPedido(@PathVariable Long pedidoId, @RequestBody Pedido pedidoAtualizado) {
        System.out.println("Tentando atualizar pedido com ID: " + pedidoId);
        try {
            Pedido pedidoAtualizadoResponse = pedidoService.atualizarPedido(pedidoId, pedidoAtualizado);
            return ResponseEntity.ok(pedidoAtualizadoResponse);
        } catch (EntityNotFoundException e) {
            System.err.println("Pedido não encontrado: " + pedidoId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            System.err.println("Erro ao atualizar pedido: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar-pedido-por-id/{pedidoId}")
    public ResponseEntity<Pedido> buscarPedidoPorId(@PathVariable Long pedidoId) {
        Pedido pedido = pedidoService.buscarPedidoPorId(pedidoId);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/listar-pedidos")
    public ResponseEntity<List<Pedido>> listarPedidos() {
        List<Pedido> pedidos = pedidoService.listarPedidos();
        return ResponseEntity.ok(pedidos);
    }

    @DeleteMapping("/deletar-pedido/{pedidoId}")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long pedidoId) {
        pedidoService.deletarPedido(pedidoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar-por-nome")
    public ResponseEntity<List<Pedido>> buscarPedidosPorNomeCliente(@RequestParam String nomeCliente) {
        List<Pedido> pedidos = pedidoService.buscarPedidosPorNomeCliente(nomeCliente);
        return ResponseEntity.ok(pedidos);
    }







}
