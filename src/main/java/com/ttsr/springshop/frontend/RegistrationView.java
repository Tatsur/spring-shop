package com.ttsr.springshop.frontend;

import com.ttsr.springshop.model.User;
import com.ttsr.springshop.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

@Route("registration")
public class RegistrationView extends VerticalLayout {
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private final UserService service;
    private final BeanValidationBinder<User> binder;
    private boolean enablePasswordValidation;

    public RegistrationView(@Autowired UserService service) {
        this.service = service;
        var title = new H1("Signup form");

        var firstnameField = new TextField("First name");
        var lastnameField = new TextField("Last name");
        var secondNameField = new TextField("Second name");
        var username = new TextField("Username");
        var phone = new NumberField("Phone number");

        var passwordField = new PasswordField("Pick a password");
        var confirmPasswordField = new PasswordField("Confirm your password");

        Span errorMassage = new Span();

        var submitButton = new Button("Register");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var formLayout = new FormLayout(title, firstnameField, lastnameField, secondNameField, phone, passwordField, confirmPasswordField, errorMassage, submitButton);

        formLayout.setMaxWidth("600px");
        formLayout.getStyle().set("margin", "0 auto");

        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("590px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP));
        formLayout.setColspan(title, 2);
        formLayout.setColspan(errorMassage, 2);
        formLayout.setColspan(submitButton, 2);

        errorMassage.getStyle().set("color", "var(--lumo-error-text-color)");
        errorMassage.getStyle().set("padding", "15px 0");

        add(formLayout);

        binder = new BeanValidationBinder<>(User.class);

        binder.forField(firstnameField).bind("firstname");
        binder.forField(lastnameField).bind("lastname");
        binder.forField(secondNameField).bind("secondname");
        binder.forField(phone).asRequired().bind("phone");
        binder.forField(username).withValidator(this::usernameValidator).asRequired().bind("username");
        binder.forField(passwordField).withValidator(this::passwordValidator).asRequired().bind("password");

        confirmPasswordField.addValueChangeListener(e -> {
            enablePasswordValidation = true;
            binder.validate();
        });

        binder.setStatusLabel(errorMassage);

        submitButton.addClickListener(e -> {
            try {
                var user = new User();
                binder.writeBean(user);
                service.store(user);
                showSuccess(user);
            } catch (ValidationException validationException) {
                validationException.printStackTrace();
            } catch (ServiceException serviceException) {
                serviceException.printStackTrace();
                errorMassage.setText("Saving credentials failed, please try again");
            }
        });
    }

    private void showSuccess(User user) {
        var notification = Notification.show("Registration successful, welcome" + user.getLogin());
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private ValidationResult passwordValidator(String password, ValueContext valueContext) {
        if (password != null || password.length() < 8) {
            return ValidationResult.error("Password should be at least 8 characters long");
        }
        if (!enablePasswordValidation) {
            enablePasswordValidation = true;
            return ValidationResult.ok();
        }

        String confirmPassword = confirmPasswordField.getValue();
        if (password.equals(confirmPassword)) {
            return ValidationResult.ok();
        }
        return ValidationResult.error("Passwords don't match");
    }

    private ValidationResult usernameValidator(String username, ValueContext valueContext) {
        String errMsg = service.isUsernameValid(username);
        if (errMsg == null) return ValidationResult.ok();
        return ValidationResult.error(errMsg);
    }
}
