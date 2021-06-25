package com.ttsr.springshop.frontend;

import com.ttsr.springshop.dto.ProductDto;
import com.ttsr.springshop.model.Product;
import com.ttsr.springshop.service.CartService;
import com.ttsr.springshop.service.ProductService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;

import java.util.stream.Collectors;

@Route("main")
public class MainView extends VerticalLayout {
    private final Grid<ProductDto> grid = new Grid<>(ProductDto.class);

    private final ProductService productService;
    private final CartService cartService;

    public MainView(ProductService productService,
                    CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;

        initPage();
    }

    private void initPage() {
        initProductGrid();

        add(grid, initCartButton());
    }

    private HorizontalLayout initCartButton() {
        var addToCartButton = new Button("Add to cart", items -> {
            cartService.addProduct(grid.getSelectedItems()
                    .stream()
                    .map(Product::new)
                    .collect(Collectors.toSet()));
            Notification.show("Product added successfully");
        });

        var toCartButton = new Button("Cart", item -> {
            UI.getCurrent().navigate("cart");
        });

        return new HorizontalLayout(addToCartButton, toCartButton);
    }

    private void initProductGrid() {
        var products = productService
                .findAll()
                .stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());

        grid.setItems(products);
        grid.setColumns("name", "count");
        grid.setSizeUndefined();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        ListDataProvider<ProductDto> dataProvider = DataProvider.ofCollection(products);
        grid.setDataProvider(dataProvider);

        grid.addColumn(new ComponentRenderer<>(item ->
        {
            var label = new Label("count");
            var label2= new Label("count");
            var plusButton = new Button("+", i -> {
                item.incrementCount();
                productService.save(new Product(item));
                grid.getDataProvider().refreshItem(new ProductDto(item));
            });

            var minusButton = new Button("-", i -> {
                item.decreaseCount();
                productService.save(new Product(item));
                grid.getDataProvider().refreshItem(new ProductDto(item));
            });


            return new HorizontalLayout(plusButton, minusButton,label,label2);
        }));
    }
}
