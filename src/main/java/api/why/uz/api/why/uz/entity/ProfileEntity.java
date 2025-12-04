package api.why.uz.api.why.uz.entity;

import api.why.uz.api.why.uz.enums.GeneralStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "profile")
@Getter
@Setter
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;  // phone or email

    @Column(name = "password")
    private String password;


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private GeneralStatus status;

    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;


    @Column(name = "created_date")
    private LocalDateTime createdDate;


}
