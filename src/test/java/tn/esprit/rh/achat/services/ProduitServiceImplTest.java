package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.rh.achat.entities.Produit;
import tn.esprit.rh.achat.entities.Stock;
import tn.esprit.rh.achat.repositories.ProduitRepository;
import tn.esprit.rh.achat.repositories.StockRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProduitServiceImplTest {

    @Mock
    ProduitRepository produitRepository;

    @Mock
    StockRepository stockRepository;

    @InjectMocks
    ProduitServiceImpl produitService;

    @Test
    void retrieveAllProduits_doitRetournerTousLesProduits() {
        when(produitRepository.findAll()).thenReturn(Arrays.asList(new Produit(), new Produit()));

        List<Produit> produits = produitService.retrieveAllProduits();

        assertEquals(2, produits.size());
    }

    @Test
    void retrieveProduit_produitExistant() {
        Produit p = new Produit();
        p.setLibelleProduit("Stylo");
        when(produitRepository.findById(1L)).thenReturn(Optional.of(p));

        Produit resultat = produitService.retrieveProduit(1L);

        assertEquals("Stylo", resultat.getLibelleProduit());
    }

    @Test
    void updateProduit_doitRetournerLeProduitModifie() {
        Produit p = new Produit();
        p.setPrix(15f);
        when(produitRepository.save(p)).thenReturn(p);

        assertEquals(15f, produitService.updateProduit(p).getPrix());
    }

    @Test
    void assignProduitToStock_doitAffecterLeStockAuProduit() {
        Produit p = new Produit();
        Stock s = new Stock("Stock A", 10, 5);
        when(produitRepository.findById(1L)).thenReturn(Optional.of(p));
        when(stockRepository.findById(2L)).thenReturn(Optional.of(s));

        produitService.assignProduitToStock(1L, 2L);

        assertEquals(s, p.getStock());
        verify(produitRepository).save(p);
    }
}
