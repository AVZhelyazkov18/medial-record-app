package bg.nbu.medialrecordapp.ui.view.auth;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "login", autoLayout = false)
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm = new LoginForm();

    public LoginView() {
        setSizeFull();

        H1 title = new H1("Medical Record App");

        loginForm.setForgotPasswordButtonVisible(false);
        loginForm.setAction("login");

        RouterLink registerLink = new RouterLink("Create an account", RegisterView.class);
        add(title, loginForm, registerLink);

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(title, loginForm, registerLink);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        boolean hasError = event.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error");

        loginForm.setError(hasError);
    }
}