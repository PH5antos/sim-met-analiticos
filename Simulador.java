import java.util.ArrayList;

public class Simulador{
  public static final long     seed        = 77332;
  public static final long     a           = 7531;
  public static final long     c           = 93765;
  public static final long     M           = 194856;
  public static       long     previous    = ((a * seed + c) % M);

  public static final int      K           = 5;
  public static final int      servidores  = 1;

  public static       int      filaCount   = 0;
  public static       int      perdas      = 0;

  public static       double   TempoGlobal = 0.0;
  public static       double[] times       = new double[K+1];

  public static final int      chegadaMin  = 2;
  public static final int      chegadaMax  = 5;
  public static final int      saidaMin    = 3;
  public static final int      saidaMax    = 5;

  public static ArrayList<Evento> Escalonador = new ArrayList<Evento>();

  enum TipoEvento {
    Chegada,
    Saida
  }
  
  static class Evento{
    TipoEvento tipoEvento;
    double tempo;

    public Evento(TipoEvento tipoEvento) {
      this.tipoEvento = tipoEvento;

      if (tipoEvento == TipoEvento.Chegada) {
        tempo = ((chegadaMax - chegadaMin) * NextRandom() + chegadaMin) + TempoGlobal;
        return;
      }
      tempo = ((saidaMax - saidaMin) * NextRandom() + saidaMin) + TempoGlobal;
    }
  }
  
  public static void main(String[] args){
    Escalonador.add(new Evento(TipoEvento.Chegada));
    Escalonador.get(0).tempo = 2.0;
    
    int count = 1000;
    while (count-- > 0) {
      Evento evento = NextEvent();

      if (evento.tipoEvento == TipoEvento.Chegada) {
        Chegada(evento.tempo);
        continue;
      }
      Saida(evento.tempo);
    }

    System.out.println("Tempo global: " + TempoGlobal + "\n");
    for (int i = 0; i < K+1; i++) {
      System.out.println(i + ": " + times[i] + " (" + times[i]/TempoGlobal*100 + "%)");
    }
    System.out.println("\nClientes perdidos: " + perdas);
  }

  public static double NextRandom() {
    previous = ((a * previous + c) % M);
    return (double) previous/M;
  }

  // otimizar: auto sorting?
  public static Evento NextEvent() {
    Evento next = Escalonador.get(0);
    for (Evento current : Escalonador) {
      if (current.tempo < next.tempo) next = current;
    }
    Escalonador.remove(next);
    return next;
  }

  public static void Chegada(double tempo) {
    AtualizaTempo(tempo);

    if (filaCount < K) {
      ++filaCount;
      if (filaCount <= servidores) {
        Escalonador.add(new Evento(TipoEvento.Saida));
      }
    }
    else {
      ++perdas;
    }
    Escalonador.add(new Evento(TipoEvento.Chegada));
  }
  
  public static void Saida(double tempo) {
    AtualizaTempo(tempo);

    --filaCount;
    if (filaCount >= servidores) {
      Escalonador.add(new Evento(TipoEvento.Saida));
    }
  }
  
  public static void AtualizaTempo(double tempo) {
    times[filaCount] += tempo - TempoGlobal;

    TempoGlobal = tempo;
  }
}