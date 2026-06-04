package bg.nbu.medialrecordapp.service.auth;

import bg.nbu.medialrecordapp.data.dto.RegisterPatientRequestDTO;
import bg.nbu.medialrecordapp.data.dto.RegisterRequestDTO;
import bg.nbu.medialrecordapp.data.dto.WebAccount.WebAccountInsertRequestDTO;
import bg.nbu.medialrecordapp.data.entity.Patient;
import bg.nbu.medialrecordapp.data.entity.auth.WebAccount;
import bg.nbu.medialrecordapp.exception.CredentialsNotFoundException;
import bg.nbu.medialrecordapp.exception.EmailAlreadyExistsException;
import bg.nbu.medialrecordapp.exception.EmailNotFoundException;
import bg.nbu.medialrecordapp.repository.PatientRepository;
import bg.nbu.medialrecordapp.repository.WebAccountRepository;
import bg.nbu.medialrecordapp.service.patient.PatientServiceImpl;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WebAccountServiceImpl implements WebAccountService {
    private final WebAccountRepository webAccountRepository;
    private final PatientServiceImpl patientService;
    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        WebAccount webAccount = webAccountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", email)));

        System.out.println(webAccount.getRole().name());

        return User
                .builder()
                .username(email)
                .password(webAccount.getPassword())
                .roles(webAccount.getRole().name())
                .build();
    }

    @Override
    @Transactional
    public void register(RegisterRequestDTO registerRequestDto) {
        String firstName = registerRequestDto.getFirstName();
        String lastName = registerRequestDto.getLastName();
        String email = registerRequestDto.getEmail();
        String password = registerRequestDto.getPassword();

        if (email == null || email.isBlank())
            throw new EmailNotFoundException();

        if (firstName == null || firstName.isBlank())
            throw new CredentialsNotFoundException("Credentials for first name are missing.");

        if (lastName == null || lastName.isBlank())
            throw new CredentialsNotFoundException("Credentials for last name are missing.");

        if (password == null || password.isBlank())
            throw new CredentialsNotFoundException("Username or password is missing.");

        if (webAccountRepository.findByEmail(email).isPresent())
            throw new EmailAlreadyExistsException();

        registerRequestDto.setPassword(passwordEncoder.encode(password));

        WebAccount account = new WebAccount(registerRequestDto);
        webAccountRepository.save(account);

        Patient patient = patientService.registerPatientWithAccount(new RegisterPatientRequestDTO(firstName, lastName), account);
        patientRepository.save(patient);
    }

    @Override
    public WebAccount save(WebAccount account) {
        return webAccountRepository.save(account);
    }

    @Override
    public WebAccount save(WebAccountInsertRequestDTO dto) {
        WebAccount account = new WebAccount();
        account.setEmail(dto.getEmail());
        account.setPassword(passwordEncoder.encode("user"));
        account.setRole(dto.getRole());

        return webAccountRepository.save(account);
    }

    @Override
    public void delete(WebAccount account) { webAccountRepository.delete(account); }

    @Override
    public List<WebAccount> findAll() {
        return webAccountRepository.findAll();
    }

    @Override
    public WebAccount findByEmail(String doctorEmail) {
        return webAccountRepository.findByEmail(doctorEmail).orElse(null);
    }
}
