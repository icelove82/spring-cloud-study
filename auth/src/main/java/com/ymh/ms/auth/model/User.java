package com.ymh.ms.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_user")
public class User {

    @Id
    @SequenceGenerator(
            name = "user_id_sequence",
            sequenceName = "user_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_sequence"
    )
    private Integer id;

    @Column(name = "user_name", unique = true, nullable = false)
    private String username;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "create_dttm", nullable = false)
    private LocalDateTime createDttm;

    @Column(name = "modify_dttm")
    private LocalDateTime modifyDttm;

    @Column(name = "last_login_dttm")
    private LocalDateTime lastLoginDttm;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @PrePersist
    protected void onCreate() {
        createDttm = LocalDateTime.now();
        isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        modifyDttm = LocalDateTime.now();
    }
}