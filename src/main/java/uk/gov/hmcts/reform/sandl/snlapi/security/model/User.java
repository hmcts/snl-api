package uk.gov.hmcts.reform.sandl.snlapi.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_data")
@Data
@Audited
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String username;

    @NotNull
    private String password;

    @NotNull
    private LocalDateTime passwordLastUpdated;

    @NotNull
    private String fullName;

    private String email;

    private boolean enabled = true;

    private boolean resetRequired = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @NotNull
    private Set<String> roles = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @NotAudited
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @NotNull
    private Set<Token> tokens = new HashSet<>();

    public void addToken(Token token) {
        token.setUser(this);
        tokens.add(token);
    }

    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @Table(name = "user_tokens")
    @Data
    public static class Token {
        @Id
        private UUID id;

        @ManyToOne
        @JsonIgnore
        private User user;

        @Column(name = "user_id", updatable = false, insertable = false)
        private int userId;

        @NotNull
        private LocalDateTime maxExpiry;

        public void setUser(@NonNull User user) {
            this.user = user;
            this.userId = user.getId();
        }
    }
}
