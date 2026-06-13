package com.hackgov.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SessaoService sessaoService;

    @PostMapping("/login")
    public ResponseEntity<?> efetuarLogin(@RequestBody LoginDTO dados) {
        Usuario usuario = usuarioRepository.findByLogin(dados.login());

        // Validação real contra os dados gravados no Oracle
        if (usuario == null || !usuario.getSenha().equals(dados.senha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha incorretos.");
        }

        // Gera o Token Dinâmico e Real
        String token = sessaoService.criarSessao(usuario);

        // Retorna o token gerado, o perfil real do banco e o nome de usuário
        return ResponseEntity.ok(new LoginResponseDTO(token, usuario.getPerfil(), usuario.getLogin()));
    }
}

record LoginDTO(String login, String senha) {}
record LoginResponseDTO(String token, String perfil, String login) {}