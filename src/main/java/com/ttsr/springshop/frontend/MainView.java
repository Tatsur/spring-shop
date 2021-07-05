package com.ttsr.springshop.frontend;

import com.ttsr.springshop.dto.ProductDto;
import com.ttsr.springshop.model.Product;
import com.ttsr.springshop.service.CartService;
import com.ttsr.springshop.service.ProductService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Route("main")
public class MainView extends VerticalLayout {
    private final Grid<ProductDto> grid = new Grid<>(ProductDto.class);

    private final ProductService productService;
    private final CartService cartService;

    private List<ProductDto> productDtoList = new ArrayList<>();
    private final Map<String,String> filterParams = new HashMap<>();

    public MainView(ProductService productService,
                    CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;

        initPage();
    }

    private void initPage() {
        initFilter();
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
                .findAll(filterParams)
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
    private void initFilter(){
        var formLayout = new FormLayout();
        var nameEq = new TextField("product name:");
        var priceGreater = new BigDecimalField("price greater then:");
        var priceLess = new BigDecimalField("price less then:");
        var filterButton = new Button("Filter", e ->{
            filterParams.put("name",nameEq.getValue());
            filterParams.put("greater", String.valueOf(priceGreater.getValue()));
            filterParams.put("less", String.valueOf(priceLess.getValue()));
            productDtoList = productService
                    .findAll(filterParams)
                    .stream()
                    .map(ProductDto::new)
                    .collect(Collectors.toList());
            grid.getDataProvider().refreshAll();
        });
        formLayout.add(nameEq,priceGreater,priceLess,filterButton);
        add(formLayout);
    }
}
