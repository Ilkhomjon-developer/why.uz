package api.why.uz.api.why.uz.entity;

import api.why.uz.api.why.uz.enums.SmsType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "sms_history")
@Getter
@Setter
public class SmsHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "message")
    private String message;

    @Column(name = "code")
    private Integer code;

    @Column(name = "attempt_count")
    private Integer attemptCount = 0;

    @Column(name = "sms_type")
    @Enumerated(EnumType.STRING)
    private SmsType smsType;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

}
