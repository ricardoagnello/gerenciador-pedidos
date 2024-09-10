package com.ricardoagnello.gerenciador_pedidos.service;

import com.ricardoagnello.gerenciador_pedidos.entity.ItemPedido;
import com.ricardoagnello.gerenciador_pedidos.entity.ItemPedidoId;
import com.ricardoagnello.gerenciador_pedidos.entity.Pedido;
import com.ricardoagnello.gerenciador_pedidos.repository.ItemPedidoRepository;
import com.ricardoagnello.gerenciador_pedidos.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    // Sobrecarga do método criarPedido que aceita um objeto Pedido
    @Transactional
    public Pedido criarPedido(Pedido pedido) {
        return criarPedido(pedido.getNomeCliente(), pedido.getData(), pedido.getItens());
    }

    // Método criarPedido original que aceita o nome do cliente, a data e a lista de itens
    @Transactional
    public Pedido criarPedido(String nomeCliente, LocalDate data, List<ItemPedido> itens) {
        // Cria um novo pedido
        Pedido novoPedido = new Pedido();
        novoPedido.setNomeCliente(nomeCliente);
        novoPedido.setData(data); // Define a data do pedido

        // Salva o pedido no banco de dados para garantir que ele tenha um ID gerado
        pedidoRepository.save(novoPedido);

        // Número sequencial do item, inicializado para 1
        int numeroSequencial = 1;

        // Verifica se a lista de itens não é nula
        if (itens != null) {
            // Associa e salva os itens ao pedido
            for (ItemPedido item : itens) {
                // Inicializa o ID do ItemPedido se ele for nulo
                if (item.getId() == null) {
                    item.setId(new ItemPedidoId());
                }

                // Associa o pedido ao item e define o número sequencial do item
                item.getId().setPedidoId(novoPedido.getPedidoId()); // Define o pedidoId no ID do item
                item.getId().setNumeroItem(numeroSequencial++);     // Define o número sequencial do item
                item.setPedido(novoPedido);                         // Associa o pedido ao item

                // Salva o item com o pedido associado e número sequencial
                itemPedidoRepository.save(item);
            }
        }

        // Atualiza a lista de itens no pedido
        novoPedido.setItens(itens);

        // Atualiza o valor total do pedido
        novoPedido.atualizarValorTotalPedido();

        // Atualiza o pedido com o valor total e a lista de itens
        pedidoRepository.save(novoPedido);

        return novoPedido;
    }


    @Transactional
    public Pedido atualizarPedido(Long pedidoId, Pedido pedidoAtualizado) {
        // Recupera o pedido existente ou lança exceção se não encontrado
        Pedido pedidoExistente = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com o ID: " + pedidoId));

        // Atualiza os dados do pedido existente
        pedidoExistente.setNomeCliente(pedidoAtualizado.getNomeCliente());
        pedidoExistente.setData(pedidoAtualizado.getData());

        // Atualiza a lista de itens do pedido
        List<ItemPedido> novosItens = pedidoAtualizado.getItens();

        // Remove os itens antigos associados ao pedido
        if (pedidoExistente.getItens() != null) {
            pedidoExistente.getItens().clear(); // Limpa a lista de itens antigos
        }

        // Adiciona e salva os novos itens
        int numeroSequencial = 1;
        if (novosItens != null) {
            for (ItemPedido item : novosItens) {
                // Inicializa o ID do ItemPedido se for nulo
                if (item.getId() == null) {
                    item.setId(new ItemPedidoId());
                }

                // Associa o pedido ao item e define o número sequencial do item
                item.getId().setPedidoId(pedidoExistente.getPedidoId()); // Define o pedidoId no ID do item
                item.getId().setNumeroItem(numeroSequencial++);         // Define o número sequencial do item
                item.setPedido(pedidoExistente);                         // Associa o pedido ao item

                // Adiciona o item à lista de itens do pedido
                pedidoExistente.getItens().add(item);
            }
        }

        // Atualiza o valor total do pedido
        pedidoExistente.atualizarValorTotalPedido();

        // Salva o pedido atualizado
        return pedidoRepository.save(pedidoExistente);
    }









    @Transactional(readOnly = true)
    public Pedido buscarPedidoPorId(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com o ID: " + pedidoId));
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    @Transactional
    public void deletarPedido(Long pedidoId) {
        // Primeiro, exclua os itens do pedido
        itemPedidoRepository.deleteByPedidoId(pedidoId);

        // Em seguida, exclua o pedido
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));

        pedidoRepository.delete(pedido);
    }

    public List<Pedido> buscarPedidosPorNomeCliente(String nomeCliente) {
        return pedidoRepository.findByNomeClienteIgnoreCase(nomeCliente);
    }



}
