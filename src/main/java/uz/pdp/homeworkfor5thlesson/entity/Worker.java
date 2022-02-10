package uz.pdp.homeworkfor5thlesson.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.homeworkfor5thlesson.entity.enums.RoleName;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Worker implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;//userning takrorlanmas qismi

    @Size(min = 3,max = 50)
    @Column(nullable = false,length = 50,scale = 3)
    private String firstName;//ismi
    @Size(min = 3,max = 50)
    @Column(nullable = false,length = 50)
    private String lastName;//familyasi

    @Email
    @Column(unique = true,nullable = false)
    private String email;//userning emaili //USERNAME sifatida ketadi

    @Column(nullable = false)
    private String password;//kalit sozi

    @Column(nullable = false,updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;//qachon royxatdan otganligi

    @UpdateTimestamp
    private Timestamp updatedAt;//oxirgi marta qachon taxrirlangani

    @ManyToMany //(fetch = FetchType.EAGER)
    private Set<Role> roles;

    private String emailCode;

    @GeneratedValue
    private UUID turniketId;

    private boolean accountNonExpired=true;
    private boolean accountNonLocked=true;
    private boolean credentialsNonExpired=true;
    private boolean enabled;


    //Bu user details ning metodlari
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}
