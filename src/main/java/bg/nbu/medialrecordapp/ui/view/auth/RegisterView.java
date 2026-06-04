package bg.nbu.medialrecordapp.ui.view.auth;

import bg.nbu.medialrecordapp.data.dto.RegisterRequestDTO;
import bg.nbu.medialrecordapp.exception.CredentialsNotFoundException;
import bg.nbu.medialrecordapp.exception.EmailAlreadyExistsException;
import bg.nbu.medialrecordapp.service.auth.WebAccountServiceImpl;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "register", autoLayout = false)
@PageTitle("Register")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {
    private final WebAccountServiceImpl authService;

    public RegisterView(WebAccountServiceImpl authService) {
        this.authService = authService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 title = new H1("Register");

        TextField firstName = new TextField("First name");
        TextField lastName = new TextField("Last name");

        EmailField email = new EmailField("Email");

        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm Password");

        HorizontalLayout nameLayout = createRow(firstName, lastName);
        HorizontalLayout passwordLayout = createRow(password, confirmPassword);

        Button registerButton = new Button("Register", _ -> {
            if (
                    firstName.isEmpty() ||
                            lastName.isEmpty() ||
                            email.isEmpty() ||
                            password.isEmpty() ||
                            confirmPassword.isEmpty()
            ) {
                Notification.show("Please fill in all fields");
                return;
            }

            if (!password.getValue().equals(confirmPassword.getValue())) {
                Notification.show("Passwords do not match");
                return;
            }

            RegisterRequestDTO registerRequestDto = new RegisterRequestDTO(
                    firstName.getValue(),
                    lastName.getValue(),
                    email.getValue(),
                    password.getValue(),
                    confirmPassword.getValue()
            );

            try {
                this.authService.register(registerRequestDto);
            } catch (CredentialsNotFoundException exc) {
                Notification.show(exc.getMessage());
                return;
            } catch (EmailAlreadyExistsException exc) {
                Notification.show("Account with this email already exists.");
                return;
            }

            Notification.show("Registration successful");

            getUI().ifPresent(ui -> ui.navigate("login"));
        });

        registerButton.setWidth("400px");

        RouterLink loginLink = new RouterLink("Already have an account? Login", LoginView.class);
        loginLink.getStyle()
                .set("font-size", "var(--lumo-font-size-s)")
                .set("margin-top", "var(--lumo-space-s)");

        add(title, nameLayout, email, passwordLayout, registerButton, loginLink);
    }

    private HorizontalLayout createRow(Component left, Component right) {
        HorizontalLayout row = new HorizontalLayout(left, right);
        row.setWidth("400px");
        row.setAlignItems(Alignment.CENTER);

        left.getElement().getStyle().set("flex", "1");
        right.getElement().getStyle().set("flex", "1");

        return row;
    }
}