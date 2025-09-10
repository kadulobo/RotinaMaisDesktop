package test;

import controller.MovimentacaoController;
import dao.impl.MovimentacaoDaoNativeImpl;
import model.Movimentacao;
import controller.UsuarioController;
import controller.CaixaController;
import controller.PeriodoController;
import dao.impl.UsuarioDaoNativeImpl;
import dao.impl.CaixaDaoNativeImpl;
import dao.impl.PeriodoDaoNativeImpl;

import java.math.BigDecimal;
import java.util.List;

/**
 * Classe de teste simples para {@link MovimentacaoController}.
 * Exercita operações básicas e consultas com dados fictícios.
 */
public class MovimentacaoControllerTest {

    public static void main(String[] args) {
        MovimentacaoDaoNativeImpl dao = new MovimentacaoDaoNativeImpl();
        MovimentacaoController controller = new MovimentacaoController(dao);

        // Criar movimentação fictícia
        Movimentacao mov = new Movimentacao();
        mov.setTipo(1);
        mov.setDesconto(BigDecimal.ZERO);
        mov.setVantagem(new BigDecimal("5.0"));
        mov.setLiquido(new BigDecimal("100.0"));
        mov.setStatus(1);
        mov.setPonto(10);
        UsuarioController usuarioController = new UsuarioController(new UsuarioDaoNativeImpl());
        CaixaController caixaController = new CaixaController(new CaixaDaoNativeImpl());
        PeriodoController periodoController = new PeriodoController(new PeriodoDaoNativeImpl());
        mov.setUsuario(TestUtils.getRandom(usuarioController.listar()));
        mov.setCaixa(TestUtils.getRandom(caixaController.listar()));
        mov.setPeriodo(TestUtils.getRandom(periodoController.listar()));
        controller.criar(mov);

        // Recuperar movimentação persistida e atualizar
        List<Movimentacao> movs = controller.listar();
        Movimentacao buscado = controller.buscarPorId(movs.get(0).getIdMovimentacao());
        buscado.setStatus(2);
        controller.atualizar(buscado);

        // Buscar e listar
        movs = controller.listar();
        controller.listar(0, 10);
        controller.buscarPorDesconto(BigDecimal.ZERO);
        controller.buscarPorVantagem(new BigDecimal("5.0"));
        controller.buscarPorLiquido(new BigDecimal("100.0"));
        controller.buscarPorTipo(1);
        controller.buscarPorStatus(1);
        controller.buscarPorPonto(10);
        controller.buscarPorIdUsuario(mov.getUsuario().getIdUsuario());
        controller.buscarPorIdCaixa(mov.getCaixa().getIdCaixa());
        controller.buscarPorIdPeriodo(mov.getPeriodo().getIdPeriodo());
        controller.pesquisar(mov);
        controller.pesquisar(mov, 0, 10);

        System.out.println("Encontrada: " + buscado);
        System.out.println("Total movimentações: " + movs.size());

        // Remover movimentação
        controller.remover(buscado.getIdMovimentacao());

        // Recriar movimentação para manter dados na tabela
        for (int i = 0; i < 1000; i++) {
        	mov.setPonto(i);
        	mov.setVantagem(mov.getVantagem().add(BigDecimal.ONE));
        	mov.setDesconto(mov.getDesconto().add(BigDecimal.ONE));
            controller.criar(mov);
		}
 
    }
}
