package tn.esprit.rh.achat.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.rh.achat.entities.Stock;
import tn.esprit.rh.achat.repositories.StockRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceImplTest {

    @Mock
    StockRepository stockRepository;      // faux repository (pas de base de données)

    @InjectMocks
    StockServiceImpl stockService;        // service à tester, avec le faux repository injecté

    @Test
    void addStock_doitRetournerLeStockSauvegarde() {
        Stock s = new Stock("Stock A", 10, 5);
        when(stockRepository.save(s)).thenReturn(s);

        Stock resultat = stockService.addStock(s);

        assertEquals("Stock A", resultat.getLibelleStock());
        verify(stockRepository, times(1)).save(s);
    }

    @Test
    void retrieveStock_stockExistant() {
        Stock s = new Stock("Stock A", 10, 5);
        when(stockRepository.findById(1L)).thenReturn(Optional.of(s));

        assertNotNull(stockService.retrieveStock(1L));
    }

    @Test
    void retrieveStock_stockInexistantRetourneNull() {
        when(stockRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(stockService.retrieveStock(99L));
    }

    @Test
    void deleteStock_doitAppelerDeleteById() {
        stockService.deleteStock(1L);

        verify(stockRepository, times(1)).deleteById(1L);
    }

    @Test
    void retrieveStatusStock_doitContenirLeLibelleDuStockEnRouge() {
        Stock enRouge = new Stock("Stock Rouge", 2, 10);
        when(stockRepository.retrieveStatusStock()).thenReturn(Arrays.asList(enRouge));

        String message = stockService.retrieveStatusStock();

        assertTrue(message.contains("Stock Rouge"));
    }
}
