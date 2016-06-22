/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.web.filter;

import br.jpa.controller.UsuarioContaJpaController;
import br.jpa.entity.UsuarioConta;
import br.jpa.entity.UsuarioContaPK;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author hideki
 */
public class VisualizarContaFilter implements Filter {

    public void destroy() {
        // TODO Auto-generated method stub

    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpSession session = ((HttpServletRequest) request).getSession(true);

        if (session.getAttribute("uNome") == null) {
            ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/faces/login.xhtml");
        } else if (session.getAttribute("uNome") != null && session.getAttribute("cId") == null) {
            ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/faces/sistema.xhtml");
        } else if (session.getAttribute("cId") != null && session.getAttribute("uNome") != null) {
            String uNome = session.getAttribute("uNome").toString();
            int cId = (int) session.getAttribute("cId");
            UsuarioContaPK usuarioContaPK = new UsuarioContaPK(uNome, cId);
            UsuarioConta uc = UsuarioContaJpaController.getInstance().findUsuarioConta(usuarioContaPK);

            if (!uc.getConta().getCGerente().equals(uNome)) {
                chain.doFilter(request, response);
            } else {
                ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/faces/sistema.xhtml");
            }
        }
    }

    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
    }

}
