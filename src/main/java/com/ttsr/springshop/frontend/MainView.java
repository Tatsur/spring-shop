package com.ttsr.springshop.frontend;

import com.ttsr.springshop.model.Product;
import com.ttsr.springshop.model.repository.ProductRepository;
import com.ttsr.springshop.service.CartService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;

@Route("main")
public class MainView extends VerticalLayout {
    private final Grid<Product> grid = new Grid<>(Product.class);

    private final ProductRepository productRepository;
    private final CartService cartService;

    public MainView(ProductRepository productRepository,
                    CartService cartService) {
        this.productRepository = productRepository;
        this.cartService = cartService;

        initPage();
    }

    private void initPage() {
        initProductGrid();

        add(grid, initCartButton());
    }

    private HorizontalLayout initCartButton() {
        var addToCartButton = new Button("Add to cart", items -> {
            cartService.addProduct(grid.getSelectedItems());
            Notification.show("Product added successfully");
        });

        var toCartButton = new Button("Cart", item -> {
            UI.getCurrent().navigate("cart");
        });

        return new HorizontalLayout(addToCartButton, toCartButton);
    }

    private void initProductGrid() {
        var products = productRepository.findAll();

        grid.setItems(products);
        grid.setColumns("name", "count");
        grid.setSizeUndefined();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        ListDataProvider<Product> dataProvider = DataProvider.ofCollection(products);
        grid.setDataProvider(dataProvider);

        grid.addColumn(new ComponentRenderer<>(item -> {
            var plusButton = new Button("+", i -> {
                item.incrementCount();
                productRepository.save(item);
                grid.getDataProvider().refreshItem(item);
            });

            var minusButton = new Button("-", i -> {
                item.decreaseCount();
                productRepository.save(item);
                grid.getDataProvider().refreshItem(item);
            });

            return new HorizontalLayout(plusButton, minusButton);
        }));
    }
}
