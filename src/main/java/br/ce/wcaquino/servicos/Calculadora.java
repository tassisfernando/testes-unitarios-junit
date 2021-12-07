package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class Calculadora {

    /**
     * @param a - parcela1
     * @param b - parcela2
     * @return soma dos dois inteiros
     */
    public int somar(int a, int b) {
        return a + b;
    }

    /**
     * @param a - parcela1
     * @param b - parcela2
     * @return diferença entre dois inteiros
     */
    public int subtrair(int a, int b) {
        return a - b;
    }

    public int dividir(int a, int b) throws NaoPodeDividirPorZeroException {
        if(b == 0)
            throw new NaoPodeDividirPorZeroException();
        return a/b;
    }

    public void imprime() {
        System.out.println("Passei aqui");
    }
}
