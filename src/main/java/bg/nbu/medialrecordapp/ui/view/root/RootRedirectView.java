package bg.nbu.medialrecordapp.ui.view.root;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

@Route("")
@PermitAll
public class RootRedirectView extends Div implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            event.forwardTo("login");
            return;
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"));

        boolean isDoctor = authentication.getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_DOCTOR"));

        boolean isPatient = authentication.getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_PATIENT"));

        if (isAdmin) {
            event.forwardTo("admin");
        } else if (isDoctor) {
            event.forwardTo("doctor-home");
        } else if (isPatient) {
            event.forwardTo("my-health");
        } else {
            event.forwardTo("login");
        }
    }
}