package tech.antoniosgarbi.desafiomvc.config;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafiomvc.model.UserModel;
import tech.antoniosgarbi.desafiomvc.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Random;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return new User(userModel.getUsername(), userModel.getPassword(),
                true, true, true,true, new ArrayList<>());
    }

    public String createUser(UserModel userModel) {
        Random random = new Random();

        String password = String.valueOf(random.nextInt(100000000,1000000000));
        
        String encripted = WebSecurityConfig.passwordEncoder().encode(password);
               
        userModel.setPassword(encripted);
        this.userRepository.save(userModel);
        System.out.println(password);
        return password;
    }

    public Page<UserModel> findAll(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }
}

