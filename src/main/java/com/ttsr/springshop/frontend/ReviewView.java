package com.ttsr.springshop.frontend;

import com.ttsr.springshop.configuration.security.CustomPrincipal;
import com.ttsr.springshop.model.Product;
import com.ttsr.springshop.model.Review;
import com.ttsr.springshop.model.repository.ReviewRepository;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;

@Route("review")
public class ReviewView extends VerticalLayout {
    private final ReviewRepository reviewRepository;
    private final Authentication authentication;

    private final Product product;


    public ReviewView(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
        this.authentication = SecurityContextHolder.getContext().getAuthentication();
        this.product = (Product) ComponentUtil.getData(UI.getCurrent(), "product");
        if (this.product == null) {
            UI.getCurrent().navigate("");
        } else {
            var reviews = reviewRepository.findByProductId(product.getId());
            initView(reviews);
        }
    }

    private void initView(List<Review> reviews) {
        Boolean isManager = ((CustomPrincipal)authentication.getPrincipal()).hasAuthority("Manager");
        for (Review review : reviews) {
            if(review.getIsModerated() || isManager) {
                var textArea = new TextArea(review.getUser().getFIO());
                textArea.setValue(review.getText() != null ? review.getText() : "");
                textArea.setReadOnly(true);
                textArea.setSizeFull();
                var moderateButton = new Button("Moderate");

                if(!isManager)
                    moderateButton.setVisible(false);
                else {
                    moderateButton.addClickListener(e -> {
                        review.setIsModerated(true);
                        moderateButton.setDisableOnClick(true);
                    });
                }

                var hr = new HorizontalLayout();
                hr.add(textArea,moderateButton);
                add(hr);
            }
        }

        TextArea newReviewArea = new TextArea();
        newReviewArea.setSizeFull();
        var saveReviewButton = new Button("Save review", e -> {
            var review = new Review();
            review.setId(UUID.randomUUID());
            review.setProduct(product);
            review.setUser(((CustomPrincipal) authentication.getPrincipal()).getUser());
            review.setText(newReviewArea.getValue());
            reviewRepository.save(review);

            Notification.show("Your review is awaiting for moderation");
        });
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(newReviewArea, saveReviewButton);
    }
}
