package com.formacionbdi.springboot.app.oauth.security.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.formacionbdi.springboot.app.commons.usuarios.models.entity.Usuario;
import com.formacionbdi.springboot.app.oauth.services.IUsuarioService;

import feign.FeignException;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

    @Autowired
    public IUsuarioService service;
	
	private Logger log = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);
	
	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		if(authentication.getName().equalsIgnoreCase("frontendapp")){
            return; // si es igual a frontendapp se salen del método!
        }
		UserDetails user = (UserDetails) authentication.getPrincipal();
		String mensaje = "Success Login: " + user.getUsername();
		System.out.println(mensaje);
		log.info(mensaje);
		
        Usuario usuario = service.findByUsername(authentication.getName());
        if(usuario.getIntentos() != null && usuario.getIntentos() > 0 ) {
            usuario.setIntentos(0);
        }
        service.update(usuario, usuario.getId());
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		if(authentication.getName().equalsIgnoreCase("frontendapp")){
            return; // si es igual a frontendapp se salen del método!
        }
		String mensaje = "Error Login: " + exception.getMessage();
		log.error(mensaje);
		System.out.println(mensaje);
        try {
            Usuario usuario = service.findByUsername(authentication.getName());
            if(usuario.getIntentos() == null) {
                usuario.setIntentos(0);
            }
            log.info("Intentos actual es de: " + usuario.getIntentos());
            usuario.setIntentos(usuario.getIntentos()+1);
            log.info("Intentos despues es de: " + usuario.getIntentos());
            if(usuario.getIntentos() >= 3) {
                log.error(String.format("El usuario %s deshabilitado por máximo intentos", usuario.getUsername()));
                usuario.setEnabled(false);
            }
            service.update(usuario, usuario.getId());
        } catch (FeignException e) {
            log.error(String.format("El usuario %s no existe en el sistema", authentication.getName()));
        }

	}

}
