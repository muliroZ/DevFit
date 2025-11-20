package com.devfitcorp.devfit.repository;
import com.devfitcorp.devfit.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
