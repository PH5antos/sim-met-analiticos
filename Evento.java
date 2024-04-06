public class Evento implements Comparable<Evento>{
  private TipoEvento tipoEvento;
  private double tempo;

  public Evento(TipoEvento tipoEvento, double tempo) {
    this.tipoEvento = tipoEvento;
    this.tempo = tempo;
  }

  public TipoEvento getTipoEvento() {
    return tipoEvento;
  }

  public double getTempo() {
    return tempo;
  }

  @Override
  public int compareTo(Evento segundo) {
    return Double.compare(this.tempo, segundo.getTempo());
  }
}
