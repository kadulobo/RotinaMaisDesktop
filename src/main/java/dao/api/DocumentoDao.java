// path: src/main/java/dao/api/DocumentoDao.java
package dao.api;

import java.time.LocalDate;
import java.util.List;

import exception.DocumentoException;
import model.Documento;

public interface DocumentoDao {

    void create(Documento documento) throws DocumentoException;

    Documento update(Documento documento) throws DocumentoException;

    void deleteById(Integer id) throws DocumentoException;

    Documento findById(Integer id) throws DocumentoException;

    Documento findWithBlobsById(Integer id) throws DocumentoException;

    List<Documento> findAll();

    List<Documento> findAll(int page, int size);

    List<Documento> findByNome(String nome);

    List<Documento> findByArquivo(byte[] arquivo);

    List<Documento> findByFoto(byte[] foto);

    List<Documento> findByVideo(byte[] video);

    List<Documento> findByData(LocalDate data);

    List<Documento> findByIdUsuario(Integer idUsuario);

    List<Documento> search(Documento filtro);

    List<Documento> search(Documento filtro, int page, int size);
}
