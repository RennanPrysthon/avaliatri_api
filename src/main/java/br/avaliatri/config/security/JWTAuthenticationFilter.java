package br.avaliatri.config.security;

import br.avaliatri.excecoes.ResponseError;
import br.avaliatri.models.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    Logger logger = Logger.getLogger(JWTAuthenticationFilter.class.getName());

    private AuthenticationManager authenticationManager;
    private JWTUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(
                                            HttpServletRequest req,
                                            HttpServletResponse res) throws AuthenticationException {
        try {
            CredenciaisDTO creds = new ObjectMapper()
                                            .readValue(req.getInputStream(), CredenciaisDTO.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());

            Authentication auth = authenticationManager.authenticate(authToken);
            return auth;

        } catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(
                                            HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        logger.log(Level.INFO, "Usuario " + ((Usuario) auth.getPrincipal()).getUsername() + " logado com sucesso");

        String username = ((Usuario) auth.getPrincipal()).getUsername();
        String token = jwtUtil.generateToken(username);
        AuthResponse authResponse = new AuthResponse("Bearer " + token, ((Usuario) auth.getPrincipal()).getId());
        objectMapper.writeValue(res.getWriter(), authResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        logger.log(Level.SEVERE, "Erro de autenticacao: " + failed.getMessage());
        ResponseError standartError = new ResponseError(HttpStatus.UNAUTHORIZED.value(), failed.getMessage(), new Date(System.currentTimeMillis()).getTime());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        objectMapper.writeValue(response.getWriter(), standartError);
    }
}
