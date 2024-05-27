import java.util.ArrayList;

public class Simulador{
  public static final long     seed        = 77332;
  public static final long     a           = 7531;
  public static final long     c           = 93765;
  public static final long     M           = 194856;
  public static       long     previous    = ((a * seed + c) % M);

  public static ArrayList<Fila> filaLista = new ArrayList<Fila>();
  public static ArrayList<Transicao> transicaoLista = new ArrayList<Transicao>();
  public static Escalonador escalonador = new Escalonador();

  public static double tempoGlobal = 0.0;
  public static int randomCount = 100000;
  
  public static void main(String[] args){
    init();
    
    while (randomCount-- > 0) {
      Evento evento = escalonador.getNext();
      
      if (evento.getTipoEvento() == TipoEvento.Chegada) {
        Chegada(evento);
      }
      else if (evento.getTipoEvento() == TipoEvento.Saida) {
        Saida(evento);
      }
      else if (evento.getTipoEvento() == TipoEvento.Passagem) {
        Passagem(evento);
      }
    }


    System.out.println("\n---------- Simulacao ------------");
    System.out.println("Tempo global: " + tempoGlobal);

    for (int i = 0; i < filaLista.size(); ++i) {
      System.out.println("\n----- Fila " + (i + 1) + " -----");

      double[] times = filaLista.get(i).getTimes();

      for (int j = 0; j < filaLista.get(i).capacity() + 1; j++) {
        System.out.println(j + ": " + times[j] + " (" + times[j] / tempoGlobal * 100 + "%)");
      }

      System.out.println("\nClientes perdidos: " + filaLista.get(i).loss());
    }
    System.out.println("----------  FIM  ----------");
  }

  public static void init() {
    
    //read input

    filaLista.add(new Fila(1, 10, 2, 4, 1, 2));
    filaLista.add(new Fila(2, 5, 0, 0, 4, 8));
    filaLista.add(new Fila(2, 10, 0, 0, 5, 15));

    transicaoLista.add(new Transicao(0, 1, 0.8, TipoEvento.Passagem));
    transicaoLista.add(new Transicao(0, 2, 0.2, TipoEvento.Passagem));

    transicaoLista.add(new Transicao(1, 0, 0.3, TipoEvento.Passagem));
    transicaoLista.add(new Transicao(1, 1, 0.5, TipoEvento.Passagem));
    transicaoLista.add(new Transicao(1, -1, 0.2, TipoEvento.Saida));

    transicaoLista.add(new Transicao(2, 2, 0.7, TipoEvento.Passagem));
    transicaoLista.add(new Transicao(2, -1, 0.3, TipoEvento.Saida));

    transicaoLista.sort((obj1, obj2) -> Double.compare(obj1.getProbabilidade(), obj2.getProbabilidade()));

    escalonador.add(new Evento(TipoEvento.Chegada, 2.0, -1, 0));
  }

  public static void Chegada(Evento evento) {
    AtualizaTempo(evento.getTempo());
    
    if (filaLista.get(evento.getIdDestino()).status() < filaLista.get(evento.getIdDestino()).capacity()) {
      filaLista.get(evento.getIdDestino()).in();
      if (filaLista.get(evento.getIdDestino()).status() <= filaLista.get(evento.getIdDestino()).servers()) {
        defineCaminho(evento.getIdDestino());
      }
    }
    else {
      filaLista.get(evento.getIdDestino()).loss();
    }
    escalonador.add(new Evento(TipoEvento.Chegada, nextEventTime(evento), evento.getIdOrigem(), evento.getIdDestino()));
  }
  
  public static void Saida(Evento evento) {
    AtualizaTempo(evento.getTempo());
    
    filaLista.get(evento.getIdOrigem()).out();
    if (filaLista.get(evento.getIdOrigem()).status() >= filaLista.get(evento.getIdOrigem()).servers()) {
      escalonador.add(new Evento(TipoEvento.Saida, nextEventTime(evento), evento.getIdOrigem(), evento.getIdDestino()));
    }
  }
  
  public static void Passagem(Evento evento) {
    AtualizaTempo(evento.getTempo());
    
    filaLista.get(evento.getIdOrigem()).out();
    if (filaLista.get(evento.getIdOrigem()).status() >= filaLista.get(evento.getIdOrigem()).servers()) {
      escalonador.add(new Evento(TipoEvento.Passagem, nextEventTime(evento), evento.getIdOrigem(), evento.getIdDestino()));
    }
    if (filaLista.get(evento.getIdDestino()).status() < filaLista.get(evento.getIdDestino()).capacity()) {
      filaLista.get(evento.getIdDestino()).in();
      if (filaLista.get(evento.getIdDestino()).status() <= filaLista.get(evento.getIdDestino()).servers()) {
        defineCaminho(evento.getIdDestino());
      }
    }
    else {
      filaLista.get(evento.getIdDestino()).loss();
    }
  }
  
  public static void AtualizaTempo(double tempo) {
    for (Fila fila : filaLista) {
      fila.updateTimes(tempo, tempoGlobal);
    }
    
    tempoGlobal = tempo;
  }

  public static double nextEventTime(Evento evento) {
    if (evento.getTipoEvento() == TipoEvento.Chegada) {
      return ((filaLista.get(evento.getIdDestino()).getMaxArrival() - filaLista.get(evento.getIdDestino()).getMinArrival()) * nextRandom() + filaLista.get(evento.getIdDestino()).getMinArrival()) + tempoGlobal;
    }
    else if (evento.getTipoEvento() == TipoEvento.Saida) {
      return ((filaLista.get(evento.getIdOrigem()).getMaxService() - filaLista.get(evento.getIdOrigem()).getMinService()) * nextRandom() + filaLista.get(evento.getIdOrigem()).getMinService()) + tempoGlobal;
    }
    else if (evento.getTipoEvento() == TipoEvento.Passagem) {
      return ((filaLista.get(evento.getIdOrigem()).getMaxService() - filaLista.get(evento.getIdOrigem()).getMinService()) * nextRandom() + filaLista.get(evento.getIdOrigem()).getMinService()) + tempoGlobal;
    }
    return -1.0;
  }

  public static double nextRandom() {
    previous = ((a * previous + c) % M);
    return (double) previous/M;
  }

  public static void defineCaminho(int idFila) {
    double prob = nextRandom(), sum = 0.0;
    for (Transicao transicao : transicaoLista) {
      if (idFila == transicao.getIdOrigem()) {
        sum += transicao.getProbabilidade();
        if (prob < sum) {
          escalonador.add(new Evento(transicao.getTipoEvento(), nextEventTime(new Evento(transicao.getTipoEvento(), 0.0, transicao.getIdOrigem(), transicao.getIdDestino())), transicao.getIdOrigem(), transicao.getIdDestino()));
          break;
        }
      }
    }
  }
}