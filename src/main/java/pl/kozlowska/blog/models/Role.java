package pl.kozlowska.blog.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20)
    @NonNull
    private String name;

    public Role(String name) {
        super();
        this.name = name;
    }
}
