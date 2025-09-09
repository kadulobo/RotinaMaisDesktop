// path: src/main/java/controller/UsuarioController.java
package controller;

import java.util.List;

import dao.api.UsuarioDao;
import exception.UsuarioException;
import infra.Logger;
import model.Usuario;

public class UsuarioController {

    private final UsuarioDao dao;

    public UsuarioController(UsuarioDao dao) {
        this.dao = dao;
    }

    public void criar(Usuario usuario) {
        Logger.info("UsuarioController.criar - inicio");
        if (usuario == null) {
            throw new UsuarioException("Usuario não pode ser nulo");
        }
        if (usuario.getIdUsuario() == null) {
            throw new UsuarioException("Id do Usuario é obrigatório");
        }
        if (usuario.getNome() == null || usuario.getNome().isEmpty()) {
            throw new UsuarioException("Nome do Usuario é obrigatório");
        }
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            throw new UsuarioException("Senha do Usuario é obrigatória");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new UsuarioException("Email do Usuario é obrigatório");
        }
        dao.create(usuario);
        Logger.info("UsuarioController.criar - sucesso");
    }

    public Usuario atualizar(Usuario usuario) {
        Logger.info("UsuarioController.atualizar - inicio");
        if (usuario == null || usuario.getIdUsuario() == null) {
            throw new UsuarioException("Usuario ou Id não pode ser nulo");
        }
        if (usuario.getNome() == null || usuario.getNome().isEmpty()) {
            throw new UsuarioException("Nome do Usuario é obrigatório");
        }
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            throw new UsuarioException("Senha do Usuario é obrigatória");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new UsuarioException("Email do Usuario é obrigatório");
        }
        Usuario updated = dao.update(usuario);
        Logger.info("UsuarioController.atualizar - sucesso");
        return updated;
    }

    public void remover(Integer id) {
        Logger.info("UsuarioController.remover - inicio");
        if (id == null) {
            throw new UsuarioException("Id do Usuario é obrigatório");
        }
        dao.deleteById(id);
        Logger.info("UsuarioController.remover - sucesso");
    }

    public Usuario buscarPorId(Integer id) {
        Logger.info("UsuarioController.buscarPorId - inicio");
        if (id == null) {
            throw new UsuarioException("Id do Usuario é obrigatório");
        }
        Usuario u = dao.findById(id);
        Logger.info("UsuarioController.buscarPorId - sucesso");
        return u;
    }

    public List<Usuario> listar() {
        Logger.info("UsuarioController.listar - inicio");
        List<Usuario> list = dao.findAll();
        Logger.info("UsuarioController.listar - sucesso");
        return list;
    }

    public List<Usuario> listar(int page, int size) {
        Logger.info("UsuarioController.listar(page) - inicio");
        List<Usuario> list = dao.findAll(page, size);
        Logger.info("UsuarioController.listar(page) - sucesso");
        return list;
    }

    public List<Usuario> buscarPorNome(String nome) {
        Logger.info("UsuarioController.buscarPorNome - inicio");
        if (nome == null || nome.isEmpty()) {
            throw new UsuarioException("Nome não pode ser vazio");
        }
        List<Usuario> list = dao.findByNome(nome);
        Logger.info("UsuarioController.buscarPorNome - sucesso");
        return list;
    }

    public List<Usuario> buscarPorSenha(String senha) {
        Logger.info("UsuarioController.buscarPorSenha - inicio");
        if (senha == null || senha.isEmpty()) {
            throw new UsuarioException("Senha não pode ser vazia");
        }
        List<Usuario> list = dao.findBySenha(senha);
        Logger.info("UsuarioController.buscarPorSenha - sucesso");
        return list;
    }

    public List<Usuario> buscarPorEmail(String email) {
        Logger.info("UsuarioController.buscarPorEmail - inicio");
        if (email == null || email.isEmpty()) {
            throw new UsuarioException("Email não pode ser vazio");
        }
        List<Usuario> list = dao.findByEmail(email);
        Logger.info("UsuarioController.buscarPorEmail - sucesso");
        return list;
    }

    public List<Usuario> pesquisar(Usuario filtro) {
        Logger.info("UsuarioController.pesquisar - inicio");
        List<Usuario> list = dao.search(filtro);
        Logger.info("UsuarioController.pesquisar - sucesso");
        return list;
    }

    public List<Usuario> pesquisar(Usuario filtro, int page, int size) {
        Logger.info("UsuarioController.pesquisar(page) - inicio");
        List<Usuario> list = dao.search(filtro, page, size);
        Logger.info("UsuarioController.pesquisar(page) - sucesso");
        return list;
    }
}
