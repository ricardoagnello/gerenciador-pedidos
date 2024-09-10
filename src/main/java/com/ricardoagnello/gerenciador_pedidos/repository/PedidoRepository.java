package com.ricardoagnello.gerenciador_pedidos.repository;

import com.ricardoagnello.gerenciador_pedidos.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p WHERE LOWER(p.nomeCliente) LIKE LOWER(CONCAT('%', :nomeCliente, '%'))")
    List<Pedido> findByNomeClienteIgnoreCase(@Param("nomeCliente") String nomeCliente);

}
