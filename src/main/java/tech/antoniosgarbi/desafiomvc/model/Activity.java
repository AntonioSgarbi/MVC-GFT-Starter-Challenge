package tech.antoniosgarbi.desafiomvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "tb_activity")
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Não é possível cadastrar atividade sem nome")
    private String name;
    @NotNull(message = "Não é possível cadastrar atividade sem data de início")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date start;
    @NotNull(message = "Não é possível cadastrar atividade sem data de fim")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date end;
    @ManyToMany
    private List<Participant> delivered;
    @ManyToMany
    private List<Participant> delayed;

}
