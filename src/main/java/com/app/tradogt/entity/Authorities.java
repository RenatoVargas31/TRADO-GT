package com.app.tradogt.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@IdClass(AuthoritiesId.class)
@Table(name = "Authorities")
public class Authorities {

    @Id
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario idUsuario;

    @Id
    @Size(max = 50)
    @NotNull
    @Column(name = "authority", nullable = false, length = 50)
    private String authority;
}
