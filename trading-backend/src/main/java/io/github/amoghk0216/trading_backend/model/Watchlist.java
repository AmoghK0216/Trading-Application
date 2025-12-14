package io.github.amoghk0216.trading_backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Table(name = "watchlists")
@NoArgsConstructor
@Getter
@Setter
public class Watchlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String coinId;

    @Column(nullable = false)
    private String coinName;

    @Column(nullable = false)
    private String coinSymbol;

    @Column(nullable = false)
    private Instant addedAt;

    @PrePersist
    protected void onCreate() {
        this.addedAt = Instant.now();
    }

    public Watchlist(User user, String coinId, String coinName, String coinSymbol){
        this.user = user;
        this.coinId = coinId;
        this.coinName = coinName;
        this.coinSymbol = coinSymbol;
    }

}
