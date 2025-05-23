package uz.dizgo.erp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.dizgo.erp.service.AuthService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //REQUESTDAN TOKENNI OLISH
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer")) {
            token = token.substring(7); //  ( Bearer < ...
            boolean validateToken = jwtProvider.validateToken(token);

            if (validateToken) {
                String username = jwtProvider.getUsernameFromToken(token);

                UserDetails userDetails = authService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(passwordAuthenticationToken);
                SecurityContextHolder.getContext().setAuthentication(passwordAuthenticationToken);
            } // else nothing
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
