package com.AgendamentoOn.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.AgendamentoOn.Model.Usuario;
import com.AgendamentoOn.Repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void salvar(Usuario usuario, BindingResult bindingResult) {
        // Verifica se já existe usuário com esse email
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "error.usuario", "Email já cadastrado");
        }

        /*
         * 
         * if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
         * bindingResult.rejectValue("email", "error.usuario", "Email já cadastrado");
         * }
         * 
         */

        // Se tiver erros, não salva
        if (bindingResult.hasErrors()) {
            return;
        }

        // Criptografa a senha
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        // Salva o usuário
        usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Usuario usuarioAtualizado) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioAtualizado.getId());
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setNome(usuarioAtualizado.getNome());
            usuario.setEmail(usuarioAtualizado.getEmail());
            usuario.setTelefone(usuarioAtualizado.getTelefone());

            if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
                usuario.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
            }
            // Alterações serão persistidas automaticamente por causa do @Transactional
            return usuario;
        }
        return null;
    }
    

    public Optional<Usuario> getUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> getTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario AtualizarUsuario(Long id, Usuario usuario) {
        if (usuarioRepository.existsById(id)) {
            usuario.setId(id);
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

}
