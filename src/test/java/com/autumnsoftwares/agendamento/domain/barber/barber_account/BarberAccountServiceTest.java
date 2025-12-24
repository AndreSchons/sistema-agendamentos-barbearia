package com.autumnsoftwares.agendamento.domain.barber.barber_account;

import com.autumnsoftwares.agendamento.domain.barber.barber_account.dto.BarberAccountCreateRequestDTO;
import com.autumnsoftwares.agendamento.domain.barber.barber_account.dto.BarberAccountResponseDTO;
import com.autumnsoftwares.agendamento.infra.exception.DataConflictException;
import com.autumnsoftwares.agendamento.infra.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BarberAccountServiceTest {

    @InjectMocks
    private BarberAccountService barberAccountService;

    @Mock
    private BarberAccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BarberAccountMapper accountMapper;

    @Test
    @DisplayName("Should create and return entity successfully")
    void testCreateAndReturnEntity_Success() {
        String email = "test@test.com";
        String password = "password";
        BarberAccount savedAccount = new BarberAccount();

        when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(accountRepository.save(any(BarberAccount.class))).thenReturn(savedAccount);

        BarberAccount result = barberAccountService.createAndReturnEntity(email, password);

        assertNotNull(result);
        verify(accountRepository).save(any(BarberAccount.class));
    }

    @Test
    @DisplayName("Should throw exception when creating entity with existing email")
    void testCreateAndReturnEntity_Failure() {
        String email = "test@test.com";
        String password = "password";

        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(new BarberAccount()));

        assertThrows(DataConflictException.class, () -> barberAccountService.createAndReturnEntity(email, password));
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should create barber account successfully")
    void testCreateBarberAccount_Success() {
        BarberAccountCreateRequestDTO requestDTO = new BarberAccountCreateRequestDTO();
        requestDTO.setEmail("test@test.com");
        requestDTO.setPassword("password");

        BarberAccount account = new BarberAccount();
        BarberAccount savedAccount = new BarberAccount();
        savedAccount.setId(1);
        BarberAccountResponseDTO responseDTO = new BarberAccountResponseDTO();

        when(accountRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.empty());
        when(accountMapper.toEntity(requestDTO)).thenReturn(account);
        when(passwordEncoder.encode(requestDTO.getPassword())).thenReturn("encodedPassword");
        when(accountRepository.save(account)).thenReturn(savedAccount);
        when(accountMapper.toResponseDTO(savedAccount)).thenReturn(responseDTO);

        BarberAccountResponseDTO result = barberAccountService.createBarberAccount(requestDTO);

        assertNotNull(result);
        verify(accountRepository).save(account);
    }

    @Test
    @DisplayName("Should throw exception when creating account with existing email")
    void testCreateBarberAccount_Failure() {
        BarberAccountCreateRequestDTO requestDTO = new BarberAccountCreateRequestDTO();
        requestDTO.setEmail("test@test.com");

        when(accountRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.of(new BarberAccount()));

        assertThrows(DataConflictException.class, () -> barberAccountService.createBarberAccount(requestDTO));
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete account by email successfully")
    void testDeleteByEmail_Success() {
        String email = "test@test.com";
        BarberAccount account = new BarberAccount();
        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));

        barberAccountService.deleteByEmail(email);

        verify(accountRepository).delete(account);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent account by email")
    void testDeleteByEmail_Failure() {
        String email = "notfound@test.com";
        when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> barberAccountService.deleteByEmail(email));
        verify(accountRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should delete account by id successfully")
    void testDeleteById_Success() {
        Integer id = 1;
        BarberAccount account = new BarberAccount();
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        barberAccountService.deleteById(id);

        verify(accountRepository).delete(account);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent account by id")
    void testDeleteById_Failure() {
        Integer id = 99;
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> barberAccountService.deleteById(id));
        verify(accountRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should find account by email successfully")
    void testFindByEmail_Success() {
        String email = "test@test.com";
        BarberAccount account = new BarberAccount();
        BarberAccountResponseDTO responseDTO = new BarberAccountResponseDTO();

        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));
        when(accountMapper.toResponseDTO(account)).thenReturn(responseDTO);

        Optional<BarberAccountResponseDTO> result = barberAccountService.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
    }

    @Test
    @DisplayName("Should return empty when account not found by email")
    void testFindByEmail_Failure() {
        String email = "notfound@test.com";
        when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<BarberAccountResponseDTO> result = barberAccountService.findByEmail(email);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should find account by id successfully")
    void testFindById_Success() {
        Integer id = 1;
        BarberAccount account = new BarberAccount();
        BarberAccountResponseDTO responseDTO = new BarberAccountResponseDTO();

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(accountMapper.toResponseDTO(account)).thenReturn(responseDTO);

        Optional<BarberAccountResponseDTO> result = barberAccountService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
    }

    @Test
    @DisplayName("Should return empty when account not found by id")
    void testFindById_Failure() {
        Integer id = 99;
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        Optional<BarberAccountResponseDTO> result = barberAccountService.findById(id);

        assertTrue(result.isEmpty());
    }
}
