// path: src/main/java/dao/api/FornecedorDao.java
package dao.api;

import java.util.List;

import exception.FornecedorException;
import model.Fornecedor;

public interface FornecedorDao {

    void create(Fornecedor fornecedor) throws FornecedorException;

    Fornecedor update(Fornecedor fornecedor) throws FornecedorException;

    void deleteById(Integer id) throws FornecedorException;

    Fornecedor findById(Integer id) throws FornecedorException;

    Fornecedor findWithFotoById(Integer id) throws FornecedorException;

    List<Fornecedor> findAll();

    List<Fornecedor> findAll(int page, int size);

    List<Fornecedor> findByNome(String nome);

    List<Fornecedor> findByFoto(byte[] foto);

    List<Fornecedor> findByEndereco(String endereco);

    List<Fornecedor> findByOnline(Boolean online);

    List<Fornecedor> search(Fornecedor filtro);

    List<Fornecedor> search(Fornecedor filtro, int page, int size);
}
