package com.ttsr.springshop.frontend;

import com.ttsr.springshop.model.Cart;
import com.ttsr.springshop.model.Product;
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

import java.util.stream.Collectors;

@Route("cart")
public class CartView extends VerticalLayout {
    private final Grid<Cart> grid = new Grid<>(Cart.class);

    private final CartService cartService;

    public CartView(CartService cartService) {
        this.cartService = cartService;

        initCartGrid();
        add(grid,initMainButton());
    }
    private HorizontalLayout initMainButton() {
        var toCartButton = new Button("Shop", item -> {
            cartService.saveCart(cartService.getProducts());
            UI.getCurrent().navigate("main");
        });

        return new HorizontalLayout(toCartButton);
    }

    private void initCartGrid() {
        var products = cartService.getProducts();

        grid.setItems(products);
        grid.setColumns("productName", "count");
        grid.setSizeUndefined();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        ListDataProvider<Cart> dataProvider = DataProvider.ofCollection(products);
        grid.setDataProvider(dataProvider);

        grid.addColumn(new ComponentRenderer<>(item -> {
            var plusButton = new Button("+", i -> {
                cartService.increaseProductCount(item);
                grid.getDataProvider().refreshItem(item);
            });

            var minusButton = new Button("-", i -> {
                cartService.decreaseProductCount(item);
                grid.getDataProvider().refreshItem(item);
            });

            return new HorizontalLayout(plusButton, minusButton);
        }));
    }
}
