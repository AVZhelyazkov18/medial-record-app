package bg.nbu.medialrecordapp.service.auth;

import bg.nbu.medialrecordapp.data.dto.RegisterRequestDTO;
import bg.nbu.medialrecordapp.data.dto.WebAccount.WebAccountInsertRequestDTO;
import bg.nbu.medialrecordapp.data.entity.auth.WebAccount;
import bg.nbu.medialrecordapp.service.UIInterfaceMethods;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface WebAccountService extends UserDetailsService, UIInterfaceMethods<WebAccount> {
    void register(RegisterRequestDTO registerRequestDto);
    WebAccount save(WebAccountInsertRequestDTO dto);
    void delete(WebAccount account);
    List<WebAccount> findAll();
    WebAccount findByEmail(String doctorEmail);
}
