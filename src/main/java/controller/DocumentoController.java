// path: src/main/java/controller/DocumentoController.java
package controller;

import java.time.LocalDate;
import java.util.List;

import dao.api.DocumentoDao;
import exception.DocumentoException;
import infra.Logger;
import model.Documento;

public class DocumentoController {

    private final DocumentoDao dao;

    public DocumentoController(DocumentoDao dao) {
        this.dao = dao;
    }

    public void criar(Documento documento) {
        Logger.info("DocumentoController.criar - inicio");
        if (documento == null) {
            throw new DocumentoException("Documento não pode ser nulo");
        }
        if (documento.getNome() == null || documento.getNome().isEmpty()) {
            throw new DocumentoException("Nome do Documento é obrigatório");
        }
        dao.create(documento);
        Logger.info("DocumentoController.criar - sucesso");
    }

    public Documento atualizar(Documento documento) {
        Logger.info("DocumentoController.atualizar - inicio");
        if (documento == null || documento.getIdDocumento() == null) {
            throw new DocumentoException("Documento ou Id não pode ser nulo");
        }
        if (documento.getNome() == null || documento.getNome().isEmpty()) {
            throw new DocumentoException("Nome do Documento é obrigatório");
        }
        Documento updated = dao.update(documento);
        Logger.info("DocumentoController.atualizar - sucesso");
        return updated;
    }

    public void remover(Integer id) {
        Logger.info("DocumentoController.remover - inicio");
        if (id == null) {
            throw new DocumentoException("Id do Documento é obrigatório");
        }
        dao.deleteById(id);
        Logger.info("DocumentoController.remover - sucesso");
    }

    public Documento buscarPorId(Integer id) {
        Logger.info("DocumentoController.buscarPorId - inicio");
        if (id == null) {
            throw new DocumentoException("Id do Documento é obrigatório");
        }
        Documento d = dao.findById(id);
        Logger.info("DocumentoController.buscarPorId - sucesso");
        return d;
    }

    public Documento buscarComArquivosPorId(Integer id) {
        Logger.info("DocumentoController.buscarComArquivosPorId - inicio");
        if (id == null) {
            throw new DocumentoException("Id do Documento é obrigatório");
        }
        Documento d = dao.findWithBlobsById(id);
        Logger.info("DocumentoController.buscarComArquivosPorId - sucesso");
        return d;
    }

    public List<Documento> listar() {
        Logger.info("DocumentoController.listar - inicio");
        List<Documento> list = dao.findAll();
        Logger.info("DocumentoController.listar - sucesso");
        return list;
    }

    public List<Documento> listar(int page, int size) {
        Logger.info("DocumentoController.listar(page) - inicio");
        List<Documento> list = dao.findAll(page, size);
        Logger.info("DocumentoController.listar(page) - sucesso");
        return list;
    }

    public List<Documento> buscarPorNome(String nome) {
        Logger.info("DocumentoController.buscarPorNome - inicio");
        if (nome == null || nome.isEmpty()) {
            throw new DocumentoException("Nome não pode ser vazio");
        }
        List<Documento> list = dao.findByNome(nome);
        Logger.info("DocumentoController.buscarPorNome - sucesso");
        return list;
    }

    public List<Documento> buscarPorArquivo(byte[] arquivo) {
        Logger.info("DocumentoController.buscarPorArquivo - inicio");
        if (arquivo == null) {
            throw new DocumentoException("Arquivo não pode ser nulo");
        }
        List<Documento> list = dao.findByArquivo(arquivo);
        Logger.info("DocumentoController.buscarPorArquivo - sucesso");
        return list;
    }

    public List<Documento> buscarPorFoto(byte[] foto) {
        Logger.info("DocumentoController.buscarPorFoto - inicio");
        if (foto == null) {
            throw new DocumentoException("Foto não pode ser nula");
        }
        List<Documento> list = dao.findByFoto(foto);
        Logger.info("DocumentoController.buscarPorFoto - sucesso");
        return list;
    }

    public List<Documento> buscarPorVideo(byte[] video) {
        Logger.info("DocumentoController.buscarPorVideo - inicio");
        if (video == null) {
            throw new DocumentoException("Video não pode ser nulo");
        }
        List<Documento> list = dao.findByVideo(video);
        Logger.info("DocumentoController.buscarPorVideo - sucesso");
        return list;
    }

    public List<Documento> buscarPorData(LocalDate data) {
        Logger.info("DocumentoController.buscarPorData - inicio");
        if (data == null) {
            throw new DocumentoException("Data não pode ser nula");
        }
        List<Documento> list = dao.findByData(data);
        Logger.info("DocumentoController.buscarPorData - sucesso");
        return list;
    }

    public List<Documento> buscarPorIdUsuario(Integer idUsuario) {
        Logger.info("DocumentoController.buscarPorIdUsuario - inicio");
        if (idUsuario == null) {
            throw new DocumentoException("Id do usuário não pode ser nulo");
        }
        List<Documento> list = dao.findByIdUsuario(idUsuario);
        Logger.info("DocumentoController.buscarPorIdUsuario - sucesso");
        return list;
    }

    public List<Documento> pesquisar(Documento filtro) {
        Logger.info("DocumentoController.pesquisar - inicio");
        List<Documento> list = dao.search(filtro);
        Logger.info("DocumentoController.pesquisar - sucesso");
        return list;
    }

    public List<Documento> pesquisar(Documento filtro, int page, int size) {
        Logger.info("DocumentoController.pesquisar(page) - inicio");
        List<Documento> list = dao.search(filtro, page, size);
        Logger.info("DocumentoController.pesquisar(page) - sucesso");
        return list;
    }
}
