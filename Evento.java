public class Evento implements Comparable<Evento>{
  private TipoEvento tipoEvento;
  private double tempo;
  private int idFilaOrigem;
  private int idFilaDestino;

  public Evento(TipoEvento tipoEvento, double tempo, int idFilaOrigem, int idFilaDestino) {
    this.tipoEvento = tipoEvento;
    this.tempo = tempo;
    this.idFilaOrigem = idFilaOrigem;
    this.idFilaDestino = idFilaDestino;
  }

  public TipoEvento getTipoEvento() {
    return tipoEvento;
  }

  public double getTempo() {
    return tempo;
  }

  public int getIdOrigem() {
    return idFilaOrigem;
  }

  public int getIdDestino() {
    return idFilaDestino;
  }

  @Override
  public int compareTo(Evento segundo) {
    return Double.compare(this.tempo, segundo.getTempo());
  }
}
