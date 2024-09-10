package com.ricardoagnello.gerenciador_pedidos.repository;

import com.ricardoagnello.gerenciador_pedidos.entity.ItemPedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    @Modifying
    @Query("DELETE FROM ItemPedido i WHERE i.id.pedidoId = :pedidoId")
    void deleteByPedidoId(@Param("pedidoId") Long pedidoId);
}
