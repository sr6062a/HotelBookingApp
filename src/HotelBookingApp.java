import java.util.*;
class Reservation {
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}
class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public RoomInventory() {
        rooms.put("SINGLE", 2);
        rooms.put("DOUBLE", 2);
        rooms.put("SUITE", 1);
    }
    public synchronized boolean allocateRoom(String roomType) {
        if (rooms.containsKey(roomType) && rooms.get(roomType) > 0) {
            rooms.put(roomType, rooms.get(roomType) - 1);
            return true;
        }
        return false;
    }

    public void showInventory() {
        System.out.println("\nRemaining Inventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + ": " + rooms.get(type));
        }
    }
}
class RoomAllocationService {

    public void allocateRoom(Reservation r, RoomInventory inventory) {
        boolean success = inventory.allocateRoom(r.roomType);

        if (success) {
            System.out.println("Booking confirmed for Guest: " + r.guestName +
                    ", Room: " + r.roomType);
        } else {
            System.out.println("Booking failed for Guest: " + r.guestName +
                    ", Room not available: " + r.roomType);
        }
    }
}
class ConcurrentBookingProcessor implements Runnable {
    private Queue<Reservation> bookingQueue;
    private RoomInventory inventory;
    private RoomAllocationService allocationService;

    public ConcurrentBookingProcessor(Queue<Reservation> bookingQueue,
                                      RoomInventory inventory,
                                      RoomAllocationService allocationService) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    @Override
    public void run() {

        while (true) {
            Reservation reservation;
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) break;
                reservation = bookingQueue.poll();
            }

            // CRITICAL SECTION 2 → Inventory update
            synchronized (inventory) {
                allocationService.allocateRoom(reservation, inventory);
            }
        }
    }
}

public class HotelBookingApp {

    public static void main(String[] args) {

        Queue<Reservation> bookingQueue = new LinkedList<>();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();
        bookingQueue.add(new Reservation("Anil", "SINGLE"));
        bookingQueue.add(new Reservation("Ravi", "DOUBLE"));
        bookingQueue.add(new Reservation("Priya", "SUITE"));
        bookingQueue.add(new Reservation("Kiran", "SINGLE"));
        bookingQueue.add(new Reservation("Sneha", "DOUBLE"));
        bookingQueue.add(new Reservation("Aman", "SUITE"));

        Thread t1 = new Thread(new ConcurrentBookingProcessor(
                bookingQueue, inventory, allocationService));

        Thread t2 = new Thread(new ConcurrentBookingProcessor(
                bookingQueue, inventory, allocationService));

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        }

        inventory.showInventory();
    }
}