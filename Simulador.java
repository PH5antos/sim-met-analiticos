import java.util.Scanner;
import java.util.ArrayList;

public class Simulador{
  public static final long     seed        = 77332;
  public static final long     a           = 7531;
  public static final long     c           = 93765;
  public static final long     M           = 194856;
  public static       long     previous    = ((a * seed + c) % M);

  public static ArrayList<Fila> filaLista = new ArrayList<Fila>();
  public static Escalonador escalonador = new Escalonador();

  public static double tempoGlobal = 0.0;
  
  public static void main(String[] args){
    init();

    escalonador.add(new Evento(TipoEvento.Chegada, 1.5));
    
    int count = 100000;
    while (count-- > 0) {
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
    Scanner scanner = new Scanner(System.in);

    double minArrival = 0, maxArrival = 0, MinService, MaxService;
    int servers, capacity, numeroFilas = 2;

    //System.out.println("Informe o numero de filas");
    //numeroFilas = Integer.parseInt(scanner.nextLine());

    for (int i = 0; i < numeroFilas; ++i) {
      System.out.println("----- Fila" + (i + 1) + " -----");
      System.out.println("Informe o numero de servidores");
      servers = Integer.parseInt(scanner.nextLine());
      System.out.println("Informe a capacidade");
      capacity = Integer.parseInt(scanner.nextLine());
      if (i == 0) {
        System.out.println("Informe o MinArrival");
        minArrival = Double.parseDouble(scanner.nextLine());
        System.out.println("Informe o MaxArrival");
        maxArrival = Double.parseDouble(scanner.nextLine());
      }
      System.out.println("Informe o MinService");
      MinService = Double.parseDouble(scanner.nextLine());
      System.out.println("Informe o MaxService");
      MaxService = Double.parseDouble(scanner.nextLine());

      filaLista.add(new Fila(servers, capacity, minArrival, maxArrival, MinService, MaxService));
    }

    scanner.close();
  }

  public static void Chegada(Evento evento) {
    AtualizaTempo(evento.getTempo());
    
    if (filaLista.get(0).status() < filaLista.get(0).capacity()) {
      filaLista.get(0).in();
      if (filaLista.get(0).status() <= filaLista.get(0).servers()) {
        escalonador.add(new Evento(TipoEvento.Passagem, nextEventTime(TipoEvento.Passagem)));
      }
    }
    else {
      filaLista.get(0).loss();
    }
    escalonador.add(new Evento(TipoEvento.Chegada, nextEventTime(TipoEvento.Chegada)));
  }
  
  public static void Saida(Evento evento) {
    AtualizaTempo(evento.getTempo());
    
    filaLista.get(1).out();
    if (filaLista.get(1).status() >= filaLista.get(1).servers()) {
      escalonador.add(new Evento(TipoEvento.Saida, nextEventTime(TipoEvento.Saida)));
    }
  }
  
  public static void Passagem(Evento evento) {
    AtualizaTempo(evento.getTempo());
    
    filaLista.get(0).out();
    if (filaLista.get(0).status() >= filaLista.get(0).servers()) {
      escalonador.add(new Evento(TipoEvento.Passagem, nextEventTime(TipoEvento.Passagem)));
    }
    if (filaLista.get(1).status() < filaLista.get(1).capacity()) {
      filaLista.get(1).in();
      if (filaLista.get(1).status() <= filaLista.get(1).servers()) {
        escalonador.add(new Evento(TipoEvento.Saida, nextEventTime(TipoEvento.Saida)));
      }
    }
    else {
      filaLista.get(1).loss();
    }
  }
  
  public static void AtualizaTempo(double tempo) {
    for (Fila fila : filaLista) {
      fila.updateTimes(tempo, tempoGlobal);
    }
    
    tempoGlobal = tempo;
  }

  public static double nextEventTime(TipoEvento tipoEvento) {
    if (tipoEvento == TipoEvento.Chegada) {
      return ((filaLista.get(0).getMaxArrival() - filaLista.get(0).getMinArrival()) * nextRandom() + filaLista.get(0).getMinArrival()) + tempoGlobal;
    }
    else if (tipoEvento == TipoEvento.Saida) {
      return ((filaLista.get(1).getMaxService() - filaLista.get(1).getMinService()) * nextRandom() + filaLista.get(1).getMinService()) + tempoGlobal;
    }
    else if (tipoEvento == TipoEvento.Passagem) {
      return ((filaLista.get(0).getMaxService() - filaLista.get(0).getMinService()) * nextRandom() + filaLista.get(0).getMinService()) + tempoGlobal;
    }
    return -1.0;
  }

  public static double nextRandom() {
    previous = ((a * previous + c) % M);
    return (double) previous/M;
  }
}