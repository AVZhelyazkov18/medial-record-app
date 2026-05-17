package bg.nbu.medialrecordapp.service.auth;

import bg.nbu.medialrecordapp.data.entity.auth.WebAccount;
import bg.nbu.medialrecordapp.repository.WebAccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class WebAccountServiceImpl implements WebAccountService, UserDetailsService {
    private final WebAccountRepository webAccountRepository;

    private WebAccount getWebAccountByUsernameFromRepository(String usernmae) {
        return webAccountRepository.find
    }

    public WebAccountServiceImpl(WebAccountRepository webAccountRepository) {
        this.webAccountRepository = webAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        WebAccount webAccount = getWebAccountByUsernameFromRepository();

        return User
                .builder()
                .username(username)
                .password("")
                .roles("USER")
                .build();
    }
}
