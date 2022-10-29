package tech.antoniosgarbi.desafiomvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "tb_group")
@NoArgsConstructor
@AllArgsConstructor
public class Group implements Comparable<Group>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer score;
    @ManyToMany
    private List<Participant> members;

    public Group(List<Participant> members) {
        this.members = members;
    }

    @Override
    public int compareTo(Group g) {
        return this.score - g.getScore();
    }
    
}
