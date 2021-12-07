package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class CalculadoraTest {

    @Mock
    private Calculadora calcMock;

    /** Chama a implementa��o da classe de fato*/
    @Spy
    private Calculadora calcSpy;

    @Mock
    private EmailService emailService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void devoMostrarDiferencaEntreMockSpy() {
        Mockito.when(calcMock.somar(1, 2)).thenReturn(8);
        //Mockito.when(calcSpy.somar(1, 2)).thenReturn(8);
        Mockito.doReturn(8).when(calcSpy).somar(1, 2);

        Mockito.doNothing().when(calcSpy).imprime();

        System.out.println("Mock: " + calcMock.somar(1,2));
        System.out.println("Spy: " + calcSpy.somar(1,2));

        System.out.println("Mock");
        calcMock.imprime();
        System.out.println("Spy");
        calcSpy.imprime();
    }

    @Test
    public void testeMockitoMatcher() {
        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.when(calcMock.somar(argumentCaptor.capture(), argumentCaptor.capture())).thenReturn(5);


        Assert.assertThat(calcMock.somar(1, 45), CoreMatchers.is(5));
        System.out.println(argumentCaptor.getAllValues());
    }

    @Test
    public void deveSomarDoisValores() {
        /**Cen�rio */
        int a = 5;
        int b = 3;

        /**A��o */
        int resultado = calcMock.somar(a, b);
        /**Verifica��o */
        Assert.assertEquals(8, resultado);
    }

    @Test
    public void deveSubtrairDoisValores() {
        /**Cen�rio */
        int a = 5;
        int b = 3;

        /**A��o */
        int resultado = calcMock.subtrair(a, b);
        /**Verifica��o */
        Assert.assertEquals(2, resultado);
    }

    @Test
    public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
        /**Cen�rio*/
        int a = 6;
        int b = 3;

        /**A��o*/
        int resultado = calcMock.dividir(a, b);
        /**Verifica��o*/
        Assert.assertEquals(2, resultado);
    }

    @Test(expected = NaoPodeDividirPorZeroException.class)
    public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
        /**Cen�rio*/
        int a = 6;
        int b = 0;

        /**A��o*/
        calcMock.dividir(a, b);
        /**Verifica��o*/
    }
}
