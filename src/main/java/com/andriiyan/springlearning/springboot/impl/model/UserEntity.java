package com.andriiyan.springlearning.springboot.impl.model;

import com.andriiyan.springlearning.springboot.api.model.User;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity implements User {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "scope")
    @Nullable
    private String scope;

    public UserEntity() {
    }

    public UserEntity(@NonNull String name, @NonNull String email, @NonNull String password) {
        this(name, email, password, null);
    }

    public UserEntity(@NonNull String name, @NonNull String email, @NonNull String password, @Nullable String scope) {
        this(0, name, email, password, scope);
    }

    public UserEntity(long id, @NonNull String name, @NonNull String email, @NonNull String password, @Nullable String scope) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.scope = scope;
    }

    public static String asScope(String ... scopes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String scope : scopes) {
            stringBuilder.append(scope);
            stringBuilder.append(",");
        }
        final String result = stringBuilder.toString();
        return result.substring(0, result.length() - 1);
    }

    public Collection<? extends GrantedAuthority> grantedAuthority() {
        if (scope == null || scope.isBlank()) {
            return Collections.emptyList();
        }
        String[] scopes = scope.split(",");
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        for (String s : scopes) {
            authorityList.add(new SimpleGrantedAuthority(s));
        }
        return authorityList;
    }

    @Nullable
    public String getScope() {
        return scope;
    }

    public void setScope(@Nullable String scope) {
        this.scope = scope;
    }

    public void setScopes(@NonNull String ... scopes) {
        setScope(asScope(scopes));
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "UserImpl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;

        UserEntity that = (UserEntity) o;

        if (id != that.id) return false;
        if (!name.equals(that.name)) return false;
        return email.equals(that.email);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }
}
