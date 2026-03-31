import java.util.*;
class RoomInventory {
    private Map<String, Integer> rooms;
    public RoomInventory() {
        rooms = new HashMap<>();
        rooms.put("DELUXE", 2);
        rooms.put("SUITE", 1);
        rooms.put("STANDARD", 3);
    }
    public void bookRoom(String roomType) {
        rooms.put(roomType, rooms.get(roomType) - 1);
    }
    public void releaseRoom(String roomType) {
        rooms.put(roomType, rooms.get(roomType) + 1);
    }

    public int getAvailability(String roomType) {
        return rooms.getOrDefault(roomType, 0);
    }
}
class Reservation {
    String guestName;
    String roomType;
    String roomId;

    public Reservation(String guestName, String roomType, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }
}

// ================== CANCELLATION SERVICE ==================
class CancellationService {

    // Stack to track rollback (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    // Store active reservations
    private Map<String, Reservation> reservations = new HashMap<>();

    // Add reservation (simulate confirmed booking)
    public void addReservation(Reservation r) {
        reservations.put(r.roomId, r);
    }
    public void cancelBooking(String roomId, RoomInventory inventory) {
        if (!reservations.containsKey(roomId)) {
            System.out.println("Cancellation failed: Reservation not found.");
            return;
        }

        Reservation r = reservations.get(roomId);
        rollbackStack.push(r.roomId);
        inventory.releaseRoom(r.roomType);
        reservations.remove(roomId);
        System.out.println("Booking cancelled successfully.");
        System.out.println("Inventory restored for room type: " + r.roomType);
    }

    public void showRollbackHistory() {
        System.out.println("\nRollback History (Most Recent First):");

        if (rollbackStack.isEmpty()) {
            System.out.println("No cancellations yet.");
            return;
        }

        for (int i = rollbackStack.size() - 1; i >= 0; i--) {
            System.out.println("Released Reservation ID: " + rollbackStack.get(i));
        }
    }
}

public class HotelBookingApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        CancellationService service = new CancellationService();

        Reservation r1 = new Reservation("Alice", "DELUXE", "R101");
        service.addReservation(r1);
        inventory.bookRoom("DELUXE");

        System.out.println("Booking Cancellation\n");

        service.cancelBooking("R101", inventory);

        service.showRollbackHistory();

        System.out.println("\nUpdated Room Availability: "
                + inventory.getAvailability("DELUXE"));
    }
}
