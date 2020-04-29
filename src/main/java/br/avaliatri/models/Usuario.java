package br.avaliatri.models;

import br.avaliatri.enums.Perfil;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    private Date created_at;
    private Date updated_at;
    private Date deleted_at;
    private Boolean is_active = true;
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Prova> provas_criadas;
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ProvaRespondida> provas_respondidas;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable
    private Set<Integer> perfis = new HashSet<>();
    public void addPerfil(Perfil p) {
        this.perfis.add(p.getCod());
    }
}
