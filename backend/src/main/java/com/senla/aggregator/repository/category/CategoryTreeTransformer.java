package com.senla.aggregator.repository.category;

import com.senla.aggregator.model.Category;
import org.hibernate.query.TupleTransformer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CategoryTreeTransformer implements TupleTransformer<Category> {
    private final Map<UUID, Category> categoryMap = new HashMap<>();
    private Category root;

    @Override
    public Category transformTuple(Object[] tuple, String[] aliases) {
        UUID id = (UUID) tuple[0];
        String name = (String) tuple[1];
        UUID parentId = (UUID) tuple[2];
        int level = (int) tuple[3];

        Category category = new Category();
        category.setId(id);
        category.setName(name);

        categoryMap.put(id, category);

        if (level == 0) root = category;
        else {
            categoryMap.get(parentId)
                    .getChildren()
                    .add(category);
        }

        return root;
    }
}
