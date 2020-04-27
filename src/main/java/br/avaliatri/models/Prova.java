package br.avaliatri.models;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Prova {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private Boolean is_published = false;
    private Boolean is_activated = true;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "prova", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Questao> questoes = new ArrayList<>();
    private Date created_at;
    private Date updated_at;
    private Date deleted_at;
}
