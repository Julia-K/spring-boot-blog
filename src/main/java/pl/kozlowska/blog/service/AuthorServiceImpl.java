package pl.kozlowska.blog.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kozlowska.blog.models.Author;
import pl.kozlowska.blog.models.Role;
import pl.kozlowska.blog.repository.AuthorRepository;
import pl.kozlowska.blog.service.interfaces.AuthorService;
import pl.kozlowska.blog.dto.UserRegistrationDto;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    List<Author> authors;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Optional<Author> findById(int id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author save(UserRegistrationDto userDto) {
        Author user = new Author(userDto.getFirstName(),
                userDto.getLastName(), userDto.getUsername(),
                userDto.getEmail(),
                passwordEncoder.encode(userDto.getPassword()),
                Arrays.asList(new Role("ROLE_USER")));
        return authorRepository.save(user);
    }

    @Override
    public Author save(UserRegistrationDto userDto, String role) {
        Author user = new Author(userDto.getFirstName(),
                userDto.getLastName(), userDto.getUsername(),
                userDto.getEmail(),
                passwordEncoder.encode(userDto.getPassword()),
                Arrays.asList(new Role(role)));
        return authorRepository.save(user);
    }

    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Author findByUsername(String username) {
        return authorRepository.findByUsername(username);
    }


    @Override
    public List<Author> saveAll() {
        //authors.forEach(e -> e.setPassword(passwordEncoder.encode("zaq1@WSX")));
        return authorRepository.saveAll(authors);
    }

    public boolean existsByEmail(String email) {
        return authorRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return authorRepository.existsByUsername(username);
    }

    @PostConstruct
    public void init() {
        saveAll();
        save(new UserRegistrationDto("admin", "admin", "admin123", "admin@admin.pl", "admin123"), "ROLE_ADMIN");
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Author author = authorRepository.findByUsername(s);
        if(author==null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return new User(author.getUsername(), author.getPassword(), mapRolesToAuthorities(author.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
