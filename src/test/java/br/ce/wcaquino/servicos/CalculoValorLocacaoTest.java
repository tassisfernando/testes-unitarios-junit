package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import static builders.UsuarioBuilder.umUsuario;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized.*;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import org.mockito.Mockito;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    private LocacaoService service;

    @Parameter
    public List<Filme> filmes;

    @Parameter(value = 1)
    public Double valorLocacao;

    @Parameter(value = 2)
    public String cenario;

    @Before
    public void setup() {
        service = new LocacaoService();
        SPCService spc = Mockito.mock(SPCService.class);
    }

    private static Filme filme1 = new Filme("Filme 1", 2, 4.0);
    private static Filme filme2 = new Filme("Filme 2", 2, 4.0);
    private static Filme filme3 = new Filme("Filme 3", 2, 4.0);
    private static Filme filme4 = new Filme("Filme 4", 2, 4.0);
    private static Filme filme5 = new Filme("Filme 5", 2, 4.0);
    private static Filme filme6 = new Filme("Filme 6", 2, 4.0);
    private static Filme filme7 = new Filme("Filme 7", 2, 4.0);

    @Parameters(name = "{2}")
    public static Collection<Object[]> getParametros() {
        return Arrays.asList(new Object[][] {
                { Arrays.asList(filme1, filme2), 8.0, "2 Filmes: sem desconto" },
                { Arrays.asList(filme1, filme2, filme3), 11.0, "3 Filmes: 25% no terceiro" },
                { Arrays.asList(filme1, filme2, filme3, filme4), 13.0, "4 Filmes: 50% no quarto" },
                { Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5 Filmes: 75% no quinto" },
                { Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0, "6 Filmes: 100% no sexto" },
                { Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0, "7 filmes: sem desconto" },
        });
    }

    @Test
    public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
        //cenário
        Usuario usuario = umUsuario().agora();

        //ação
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificação
        Assert.assertThat(locacao.getValor(), is(valorLocacao));
    }
}
