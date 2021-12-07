package br.ce.wcaquino.servicos;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import static br.ce.wcaquino.utils.DataUtils.*;
import static builders.FilmeBuilder.umFilme;
import static builders.FilmeBuilder.umFilmeSemEstoque;
import static builders.LocacaoBuilder.umLocacao;
import static builders.UsuarioBuilder.umUsuario;
import static matchers.MatchersProprios.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import org.junit.*;
import static org.junit.Assert.assertThat;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LocacaoServiceTest {

	@InjectMocks @Spy
	private LocacaoService service;

	@Mock
	private SPCService spc;

	@Mock
	private LocacaoDAO dao;

	@Mock
	private EmailService emailService;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	/**
	 * Antes de cada m�todo
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Depois de cada m�todo
	 */
	@After
	public void tearDown() {
	}

	/**Antes da execu��o da classe */
	@BeforeClass
	public static void setupClass() {
	}

	/** Depois da classe ser destruida*/
	@AfterClass
	public static void tearDownClass() {
	}

	@Test
	public void deveAlugarFilmeComSucesso() throws Exception{
		//cen�rio 1
		Usuario user = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5d).agora());

		Mockito.doReturn(DataUtils.obterData(8, 7, 2021)).when(service).obterData();

		//a��o 1
		Locacao locacao = service.alugarFilme(user, filmes);

		//verifica��o
		error.checkThat(locacao.getValor(), is(5.0));

		error.checkThat(isMesmaData(locacao.getDataLocacao(), obterData(8,7,2021)), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterData(9,7,2021)), is(true));
	}

	/**Forma 1 (Elegante) - Teste com exception */
	@Test(expected = FilmeSemEstoqueException.class)
	public void deveLancarExcecaoAoAlugarFilmeSemEstoque() throws Exception {
		//cen�rio 1
		Usuario user = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

		//a��o 1
		service.alugarFilme(user, filmes);
	}

	/**Forma 2 (Robusta) - Teste com exception */
	@Test
	public void deveLancarExcecaoAoAlugarFilmeSemEstoque2() throws Exception {
		//cen�rio 1
		Usuario user = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

		//a��o 1
		try {
			service.alugarFilme(user, filmes);
			Assert.fail("Deveria ter lan�ado uma exce��o");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Filme sem estoque"));
		}
	}

	/**Forma 3 (Nova) - Teste com exception */
	@Test
	public void deveLancarExcecaoAoAlugarFilmeSemEstoque3() throws Exception {
		//cen�rio 1
		Usuario user = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

		//deve ser feito antes da a��o
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");

		//a��o 1
		service.alugarFilme(user, filmes);
	}

	/**Teste geral da loca��o */
	@Test
	public void deveAlugarComOValorDaLocacaoCerto() {
		//cen�rio 1
		Usuario user = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5d).agora());

		//a��o 1
		Locacao locacao = null;
		try {
			locacao = service.alugarFilme(user, filmes);
			//verifica��o
			assertThat(locacao.getValor(), is(5.0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**Testes particulares de cada possibilidade*/
	@Test
	public void naoDeveAlugarComOValorDaLocacaoErrado() {
		//cen�rio 1
		Usuario user = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Capit�o Am�rica", 20, 8.0));

		//a��o 1
		Locacao locacao = null;
		try {
			locacao = service.alugarFilme(user, filmes);
			//verifica��o
			assertThat(locacao.getValor(), is(not(5.0)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testeDataLocacaoCerto() {
		//cen�rio 1
		Usuario user = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		//a��o 1
		Locacao locacao = null;
		try {
			locacao = service.alugarFilme(user, filmes);
			//verifica��o
			assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testeDataLocacaoErrado() {
		//cen�rio 1
		Usuario user = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		//a��o 1
		Locacao locacao = null;
		try {
			locacao = service.alugarFilme(user, filmes);
			//verifica��o
			assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testeDataRetornoCerto() {
		//cen�rio 1
		Usuario user = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		//a��o 1
		Locacao locacao = null;
		try {
			locacao = service.alugarFilme(user, filmes);
			//verifica��o
			assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deveDataRetornoErrada() {
		//cen�rio 1
		Usuario user = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		//a��o 1
		Locacao locacao = null;
		try {
			locacao = service.alugarFilme(user, filmes);

			//verifica��o
			assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(5)), is(false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws Exception {
		//cen�rio 1
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		//a��o 1
		try {
			service.alugarFilme(null, filmes);
			Assert.fail("Deveria ter lan�ado uma exce��o");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
	}

	@Test
	public void naoDeveAlugarFilmeSemFilme() throws Exception {
		//cen�rio 1
		Usuario user = umUsuario().agora();
		//a��o 1
		try {
			service.alugarFilme(user, null);
			Assert.fail("Deveria ter lan�ado uma exce��o");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Lista nula de filmes"));
		}
	}

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		/**Cria nova data aos s�bados*/
		Mockito.doReturn(DataUtils.obterData(10, 7, 2021)).when(service).obterData();

		Locacao retorno = service.alugarFilme(usuario, filmes);

		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
	}

	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

		try {
			service.alugarFilme(usuario, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuario negativado"));
		}
		verify(spc).possuiNegativacao(usuario);
	}

	@Test
	public void deveTratarErroNoSPC() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		when(spc.possuiNegativacao(usuario)).thenThrow(new RuntimeException("Falha catastr�fica"));

		exception.expect(LocadoraException.class);
		exception.expectMessage("Problemas com SPC, tente novamente");

		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Usuario 2").agora();
		Usuario usuario3 = umUsuario().comNome("Usuario 3").agora();
		List<Locacao> locacoesAtrasadas = Arrays.asList(
				umLocacao().comUsuario(usuario).atrasado().agora(),
				umLocacao().comUsuario(usuario2).agora(),
				umLocacao().comUsuario(usuario3).atrasado().agora(),
				umLocacao().comUsuario(usuario3).atrasado().agora()
		);

		when(dao.obterLocacoesPendentes()).thenReturn(locacoesAtrasadas);

		service.notificarAtrasos();

		verify(emailService, times(3)).notificarAtraso(Mockito.any(Usuario.class));

		verify(emailService).notificarAtraso(usuario);
		verify(emailService, never()).notificarAtraso(usuario2); //verifica que nunca ocorreu
		verify(emailService, Mockito.times(2)).notificarAtraso(usuario3);
		verify(emailService, Mockito.atLeastOnce()).notificarAtraso(usuario3);

		/**Verifica se n�o teve mais varia��es al�m das descritas*/
		verifyNoMoreInteractions(emailService);

		/**Verifica se n�o teve nenhuma intera��o no spcService*/
		verifyZeroInteractions(spc);
	}

	@Test
	public void deveProrrogarUmaLocacao() {
		Locacao locacao = umLocacao().agora();
		final int QTD_DIAS = 3;
		service.prorrogarLocacao(locacao, QTD_DIAS);

		ArgumentCaptor<Locacao> argumentCaptor = ArgumentCaptor.forClass(Locacao.class);
		verify(dao).salvar(argumentCaptor.capture());
		Locacao locacaoRetornada = argumentCaptor.getValue();

		/**error.checkThat pega todos os erros, n�o s� o primeiro*/
		error.checkThat(locacaoRetornada.getValor(), is(locacao.getValor()*QTD_DIAS));
		error.checkThat(locacaoRetornada.getDataLocacao(), is(ehHoje()));
		error.checkThat(locacaoRetornada.getDataRetorno(), is(ehHojeComDiferencaDias(QTD_DIAS)));
	}

	@Test /**Testando m�todos privados com a api Reflect do Java */
	public void deveCalcularValorLocacao() throws Exception {
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		/** Utilizando Reflect para acessar m�todos privados*/
		Class<LocacaoService> classLocacaoService = LocacaoService.class;
		Method metodo = classLocacaoService.getDeclaredMethod("calcularValorLocacao", List.class);
		metodo.setAccessible(true);
		Double valorRetornado = (Double) metodo.invoke(service, filmes);

		assertThat(valorRetornado, is(4D));
	}
}
