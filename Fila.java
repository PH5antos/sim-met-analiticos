public class Fila {
  private int servers;
  private int capacity;
  private double minArrival;
  private double maxArrival;
  private double minService;
  private double maxService;
  private int customers;
  private int loss;
  private double[] times;

  public Fila(int servers, int capacity, double minArrival, double maxArrival, double minService, double maxService) {
    this.servers = servers;
    this.capacity = capacity;
    this.minArrival = minArrival;
    this.maxArrival = maxArrival;
    this.minService = minService;
    this.maxService = maxService;

    times = new double[capacity + 1];
  }

  public int status() {
    return customers;
  }

  public int capacity() {
    return capacity;
  }

  public int servers() {
    return servers;
  }

  public int loss() {
    return loss++;
  }

  public void in() {
    ++customers;
  }

  public void out() {
    --customers;
  }

  public double[] getTimes() {
    return times;
  }

  public void updateTimes(double time, double globalTime) {
    times[customers] += time - globalTime;
  }

  public double getMinArrival() {
    return minArrival;
  }

  public double getMaxArrival() {
    return maxArrival;
  }

  public double getMinService() {
    return minService;
  }

  public double getMaxService() {
    return maxService;
  }
}
