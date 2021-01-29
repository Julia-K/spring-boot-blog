package pl.kozlowska.blog.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter @Setter
@Table(name = "authors",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class Author {
    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @NotEmpty(message = "First name is required")
    private String firstName;

    @NonNull
    @NotEmpty(message = "Last name is required")
    private String lastName;

    @NonNull
    @Size(min = 2, message = "Username must be at least 2 characters long.")
    private String username;

    @Email
    private String email;

    private String password;

    @OneToMany(mappedBy = "author", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<PostsAuthors> postsAuthors;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(	name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    public Author(String firstName, String lastName,
                  String username, String email,
                  String password, Collection<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public void setPostsAuthors(List<PostsAuthors> p){
        postsAuthors = p;
    }
}
