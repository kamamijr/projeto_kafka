package com.scd.inventory;

import com.scd.inventory.model.InventoryItem;
import com.scd.inventory.repository.InventoryItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final InventoryItemRepository itemRepository;

    public DataInitializer(InventoryItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void run(String... args) {
        // Só inicializa se o banco estiver vazio
        if (itemRepository.count() == 0) {
            List<InventoryItem> seeds = List.of(
                new InventoryItem("PROD-100", "Câmera DSLR", 15),
                new InventoryItem("PROD-200", "Tablet 10\"", 40),
                new InventoryItem("PROD-300", "Fone de Ouvido Bluetooth", 80),
                new InventoryItem("PROD-400", "Carregador Solar Portátil", 25),
                new InventoryItem("PROD-500", "Bateria Externa 20.000mAh", 60),
                new InventoryItem("PROD-600", "Suporte para Monitor", 30),
                new InventoryItem("PROD-700", "Hub USB 4 Portas", 50),
                new InventoryItem("PROD-800", "SSD Externo 1TB", 20)
            );

            itemRepository.saveAll(seeds);
            System.out.println("✔️  Inventário inicializado com itens de exemplo.");
        }
    }
}
