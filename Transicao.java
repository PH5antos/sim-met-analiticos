public class Transicao {
  private int idOrigem;
  private int idDestino;
  private double probabilidade;
  private TipoEvento tipoEvento;

  public Transicao(int idOrigem, int idDestino, double probabilidade, TipoEvento tipoEvento) {
    this.idOrigem = idOrigem;
    this.idDestino = idDestino;
    this.probabilidade = probabilidade;
    this.tipoEvento = tipoEvento;
  }

  public int getIdOrigem() {
    return idOrigem;
  }

  public int getIdDestino() {
    return idDestino;
  }

  public double getProbabilidade() {
    return probabilidade;
  }

  public TipoEvento getTipoEvento() {
    return tipoEvento;
  }
}
