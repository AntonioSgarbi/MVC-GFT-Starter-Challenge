package tech.antoniosgarbi.desafiomvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name = "tb_participant")
@NoArgsConstructor
@AllArgsConstructor
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String level;
    private String email;
    private String letters;
    private String imageUrl;

    public Participant(String name, String letters) {
        this.name = name;
        this.letters = letters;
    }

    public Participant(Long id, String name, String letters) {
        this.id = id;
        this.name = name;
        this.letters = letters;
    }
}
