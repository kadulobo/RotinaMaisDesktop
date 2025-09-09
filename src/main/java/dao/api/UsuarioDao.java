// path: src/main/java/dao/api/UsuarioDao.java
package dao.api;

import java.util.List;

import exception.UsuarioException;
import model.Usuario;

public interface UsuarioDao {
    void create(Usuario usuario) throws UsuarioException;

    Usuario update(Usuario usuario) throws UsuarioException;

    void deleteById(Integer id) throws UsuarioException;

    Usuario findById(Integer id) throws UsuarioException;

    Usuario findWithBlobsById(Integer id) throws UsuarioException;

    List<Usuario> findAll();

    List<Usuario> findAll(int page, int size);

    List<Usuario> findByNome(String nome);

    List<Usuario> findBySenha(String senha);

    List<Usuario> findByEmail(String email);

    List<Usuario> findByFoto(byte[] foto);

    List<Usuario> search(Usuario filtro);

    List<Usuario> search(Usuario filtro, int page, int size);
}
