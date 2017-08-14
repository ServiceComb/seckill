package io.servicecomb.poc.demo.seckill;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.junit.Test;

public class SecKillCommandServiceTest {

  private final int numberOfCoupons = 10;
  private final BlockingQueue<Integer> coupons = new ArrayBlockingQueue<>(numberOfCoupons);

  private final SecKillCommandService<Integer> commandService = new SecKillCommandService<>(coupons, numberOfCoupons);
  private final AtomicInteger customerIdGenerator = new AtomicInteger();
  private final AtomicInteger numberOfSuccess = new AtomicInteger();

  @Test
  public void putsAllCustomersInQueue() {
    for (int i = 0; i < 5; i++) {
      SecKillCode success = commandService.addCouponTo(i);
      assertThat(success, not(SecKillCode.Failed));
    }
    assertThat(coupons, contains(0, 1, 2, 3, 4));
  }

  @Test
  public void noMoreItemAddedToQueueOnceFull() {
    keepConsumingCoupons();

    int threads = 200;
    CyclicBarrier barrier = new CyclicBarrier(threads);

    addCouponsAsync(threads
        , () -> {
          try {
            barrier.await();
            SecKillCode code = commandService.addCouponTo(customerIdGenerator.incrementAndGet());
            return !code.equals(SecKillCode.Failed);
          } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
          }
        }, success -> {
          if (success) {
            numberOfSuccess.incrementAndGet();
          }
        });

    assertThat(numberOfSuccess.get(), is(10));
  }

  @Test
  public void failsToAddCustomerIfQueueIsFull() {
    for (int i = 0; i < numberOfCoupons; i++) {
      boolean success = !commandService.addCouponTo(i).equals(SecKillCode.Failed);
      assertThat(success, is(true));
    }

    assertThat(coupons.size(), is(numberOfCoupons));

    boolean success = !commandService.addCouponTo(100).equals(SecKillCode.Failed);
    assertThat(success, is(false));
    assertThat(coupons, contains(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
  }

  private void addCouponsAsync(int threads, Supplier<Boolean> supplier, Consumer<Boolean> consumer) {
    ExecutorService executorService = Executors.newFixedThreadPool(threads);

    List<CompletableFuture<Void>> futures = new ArrayList<>();
    for (int i = 0; i < threads; i++) {
      futures.add(CompletableFuture.supplyAsync(supplier, executorService).thenAccept(consumer));
    }

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
    executorService.shutdown();
  }

  private void keepConsumingCoupons() {
    CompletableFuture.runAsync(() -> {
      while (!Thread.currentThread().isInterrupted()) {
        try {
          coupons.take();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    });
  }
}
