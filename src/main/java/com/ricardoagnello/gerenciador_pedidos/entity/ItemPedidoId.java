package com.ricardoagnello.gerenciador_pedidos.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Embeddable
public class ItemPedidoId implements Serializable {

    private Long pedidoId;
    private Integer numeroItem;

    public ItemPedidoId(Long pedidoId, Integer numeroItem) {
        this.pedidoId = pedidoId;
        this.numeroItem = numeroItem;
    }
}
