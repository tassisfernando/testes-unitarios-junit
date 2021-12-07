package br.ce.wcaquino.servicos;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterData;
import static builders.FilmeBuilder.umFilme;
import static builders.UsuarioBuilder.umUsuario;
import static matchers.MatchersProprios.caiNumaSegunda;
import static org.hamcrest.CoreMatchers.is;
import org.junit.*;
import static org.junit.Assert.assertThat;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.reflect.Whitebox.invokeMethod;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService.class})
public class LocacaoService_PowerMockTest {

	@InjectMocks
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
	 * Antes de cada método
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		service = PowerMockito.spy(service);
	}

	/**
	 * Depois de cada método
	 */
	@After
	public void tearDown() {
	}

	/**Antes da execução da classe */
	@BeforeClass
	public static void setupClass() {
	}

	/** Depois da classe ser destruida*/
	@AfterClass
	public static void tearDownClass() {
	}

	@Test
	public void deveAlugarFilmeComSucesso() throws Exception{
		//cenário 1
		Usuario user = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5d).agora());

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 8);
		calendar.set(Calendar.MONTH, Calendar.JULY);
		calendar.set(Calendar.YEAR, 2021);
		/** Mockando método estático com PowerMock*/
		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);

		//ação 1
		Locacao locacao = service.alugarFilme(user, filmes);

		//verificação
		error.checkThat(locacao.getValor(), is(5.0));

		//assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		//error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		/**TODO arrumar o ehHoje() */
		//error.checkThat(locacao.getDataRetorno(), ehHoje());

		//error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
		//error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));

		error.checkThat(isMesmaData(locacao.getDataLocacao(), obterData(8,7,2021)), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterData(9,7,2021)), is(true));
	}

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		/**Cria nova data aos sábados*/
		//PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(obterData(10,7,2021));
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 10);
		calendar.set(Calendar.MONTH, Calendar.JULY);
		calendar.set(Calendar.YEAR, 2021);
		/** Mockando método estático com PowerMock*/
		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);

		Locacao retorno = service.alugarFilme(usuario, filmes);

		//boolean isSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		//Assert.assertTrue(isSegunda);
		//assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		//assertThat(retorno.getDataRetorno(), MatchersProprios.caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());

		/**Verificando execução do método estático com PowerMock */
		PowerMockito.verifyStatic(Mockito.times(2));
		Calendar.getInstance();
		//PowerMockito.verifyNew(Date.class, Mockito.atLeastOnce()).withNoArguments();
	}

	@Test /**Mockando métodos privados!! E verificando sua chamada (POWERMOCK)*/
	public void deveAlugarFilmeSemCalcularValor() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		PowerMockito.doReturn(1D).when(service, "calcularValorLocacao", filmes);

		Locacao locacao = service.alugarFilme(usuario, filmes);

		assertThat(locacao.getValor(), is(1D));
		PowerMockito.verifyPrivate(service).invoke("calcularValorLocacao", filmes);
	}

	@Test /**Testando métodos privados com WhiteBox.invokeMethod */
	public void deveCalcularValorLocacao() throws Exception {
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		Double valorRetornado = invokeMethod(service, "calcularValorLocacao", filmes);

		assertThat(valorRetornado, is(4D));
	}
}
