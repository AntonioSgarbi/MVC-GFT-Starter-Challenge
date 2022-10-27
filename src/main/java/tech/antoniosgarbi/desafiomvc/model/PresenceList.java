package tech.antoniosgarbi.desafiomvc.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class PresenceList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    private List<Participant> participants;
}
