package com.senla.aggregator.seeder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.senla.aggregator.model.Category;
import com.senla.aggregator.model.Product;
import com.senla.aggregator.model.Vendor;
import com.senla.aggregator.repository.ProductRepository;
import com.senla.aggregator.repository.VendorRepository;
import com.senla.aggregator.repository.category.CategoryRepository;
import com.senla.aggregator.repository.seed.UsedSeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class ProductSeeder implements CommandLineRunner {
    private static final String SEEDER_NAME = "product_seeder";

    private final Faker faker;
    private final Random random;
    private final ObjectMapper objectMapper;
    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UsedSeedRepository usedSeedRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (usedSeedRepository.wasSeedUsed(SEEDER_NAME)) return;

        List<Category> categories = categoryRepository.findAll(PageRequest.of(0, 100))
                .getContent();
        List<Vendor> vendors = vendorRepository.findAll(PageRequest.of(0, 100))
                .getContent();
        List<Product> products = prepareProducts(vendors, categories);

        productRepository.saveAll(products);
        usedSeedRepository.markSeedAsUsed(SEEDER_NAME);

        log.info("***** {} products successfully seeded! *****", products.size());
    }

    private List<Product> prepareProducts(List<Vendor> vendors, List<Category> categories) {
        int vendorsSize = vendors.size();
        int categoriesSize = categories.size();
        List<Product> products = new ArrayList<>();

        for (int i = 0; i < 10_000; i++) {
            Product product = new Product();
            product.setName(faker.commerce().productName() + i);
            product.setDescription(faker.lorem().paragraph());
            product.setVendor(vendors.get(random.nextInt(vendorsSize)));
            product.setCategories(List.of(categories.get(random.nextInt(categoriesSize))));
            product.setCharacteristics(fakeCharacteristics());
            product.setVerified(true);

            products.add(product);
        }

        return products;
    }

    private JsonNode fakeCharacteristics() {
        ObjectNode characteristics = objectMapper.createObjectNode();

        for (int i = 0; i < 5; i++)
            characteristics.put(
                    faker.hacker().noun(),
                    faker.lorem().sentence()
            );

        return characteristics;
    }
}
