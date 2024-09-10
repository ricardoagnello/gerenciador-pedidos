package com.ricardoagnello.gerenciador_pedidos.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pedidoId;

    @NotNull(message = "Data obrigatória")
    private LocalDate data;

    @NotEmpty(message = "Nome cliente obrigatório")
    private String nomeCliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Valid
    private List<ItemPedido> itens = new ArrayList<>();
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#,##0.00")
    private BigDecimal valorTotalPedido;

    public Pedido(Long pedidoId, LocalDate data, String nomeCliente, List<ItemPedido> itens, BigDecimal valorTotalPedido) {
        this.pedidoId = pedidoId;
        this.data = data;
        this.nomeCliente = nomeCliente;
        this.itens = itens;
        this.valorTotalPedido = valorTotalPedido;
    }

    public BigDecimal calcularValorTotalPedido() {
        if (itens == null || itens.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return itens.stream()
                .map(ItemPedido::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void atualizarValorTotalPedido() {
        this.valorTotalPedido = calcularValorTotalPedido();
    }
}
