import java.util.PriorityQueue;

public class Escalonador {
  private PriorityQueue<Evento> prioQueue;

  public Escalonador() {
    prioQueue = new PriorityQueue<Evento>();
  }

  public void add(Evento evento) {
    prioQueue.add(evento);
  }

  public Evento getNext() {
    return prioQueue.poll();
  }
}
