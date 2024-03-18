package com.casumo.races.db;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "races_user")
public class RacesUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "races_user_seq_generator")
    @SequenceGenerator(name = "races_user_seq_generator", sequenceName = "races_user_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserType type;

    private String username;

    private String fullName;

    private String password;

    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Bet> bets = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RacesUser that = (RacesUser) o;
        return id == that.id  && type == that.type
                && Objects.equals(username, that.username)
                && Objects.equals(fullName, that.fullName)
                && Objects.equals(password, that.password)
                && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, username, fullName, password, email);
    }
}
