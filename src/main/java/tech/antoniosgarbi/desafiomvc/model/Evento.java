package tech.antoniosgarbi.desafiomvc.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    @OneToMany
    private Set<Atividade> atividades;

}
