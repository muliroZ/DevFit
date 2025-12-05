package com.devfitcorp.devfit.service;

import com.devfitcorp.devfit.model.Plano;
import com.devfitcorp.devfit.repository.PlanoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanoService {

     private final PlanoRepository planoRepository;

     public PlanoService(PlanoRepository planoRepository) {
         this.planoRepository = planoRepository;
     }

     public List<Plano> listarAtivos() {
         return planoRepository.findAllByAtivo(true);
     }
}
