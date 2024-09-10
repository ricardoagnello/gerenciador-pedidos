package com.ricardoagnello.gerenciador_pedidos.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class ItemPedido {

    @EmbeddedId
    private ItemPedidoId id;

    @ManyToOne
    @MapsId("pedidoId")
    @JoinColumn(name = "pedido_id")
    @JsonBackReference
    private Pedido pedido;

    @NotEmpty(message = "Descrição obrigatória")
    private String descricao;

    @NotNull(message = "Quantidade obrigatória")
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
    private Integer quantidade;

    @NotNull(message = "O valor unitário é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor unitário deve ser maior que zero")
    private BigDecimal valorUnitario;



    public ItemPedido(ItemPedidoId id, Pedido pedido, String descricao, Integer quantidade, BigDecimal valorUnitario) {
        this.id = id;
        this.pedido = pedido;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    // Calcula o valor total do item
    public BigDecimal getValorTotal(){
        if (quantidade == null || valorUnitario == null) {
            return BigDecimal.ZERO;
        }
        return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}
