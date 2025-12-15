package com.autumnsoftwares.agendamento.domain.barber.barber_account;

import com.autumnsoftwares.agendamento.domain.barber.barber_account.role.AccountRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "barber_accounts")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class BarberAccount implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    @Size(min = 8)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private AccountRole role;

    public BarberAccount(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = AccountRole.ADMIN;
    }

    @Override
    public  Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == AccountRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public  String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
