import java.util.*;

public class Parking_management{

    static class ParkingSpot {
        String licensePlate;
        long entryTime;
        String status;

        ParkingSpot() {
            status = "EMPTY";
        }
    }

    static ParkingSpot[] parkingLot = new ParkingSpot[10];
    static int totalProbes = 0;
    static int totalParks = 0;

    static {
        for (int i = 0; i < parkingLot.length; i++) {
            parkingLot[i] = new ParkingSpot();
        }
    }

    public static int hashFunction(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % parkingLot.length;
    }

    public static void parkVehicle(String licensePlate) {
        int index = hashFunction(licensePlate);
        int probes = 0;

        while (parkingLot[index].status.equals("OCCUPIED")) {
            index = (index + 1) % parkingLot.length;
            probes++;
        }

        parkingLot[index].licensePlate = licensePlate;
        parkingLot[index].entryTime = System.currentTimeMillis();
        parkingLot[index].status = "OCCUPIED";

        totalProbes += probes;
        totalParks++;

        System.out.println("Assigned spot #" + index + " (" + probes + " probes)");
    }

    public static void exitVehicle(String licensePlate) {
        int index = hashFunction(licensePlate);
        int start = index;

        while (!parkingLot[index].status.equals("EMPTY")) {
            if (parkingLot[index].status.equals("OCCUPIED") &&
                    parkingLot[index].licensePlate.equals(licensePlate)) {

                long exitTime = System.currentTimeMillis();
                long durationMillis = exitTime - parkingLot[index].entryTime;
                double hours = durationMillis / (1000.0 * 60 * 60);
                double fee = hours * 50; // Rs.50 per hour

                parkingLot[index].status = "DELETED";
                System.out.printf("Spot #%d freed, Duration: %.2f hours, Fee: Rs.%.2f\n", index, hours, fee);
                return;
            }

            index = (index + 1) % parkingLot.length;
            if (index == start) break;
        }

        System.out.println("Vehicle not found");
    }

    public static void getStatistics() {
        int occupied = 0;
        for (ParkingSpot spot : parkingLot) {
            if (spot.status.equals("OCCUPIED")) {
                occupied++;
            }
        }

        double occupancy = (occupied * 100.0) / parkingLot.length;
        double avgProbes = totalParks == 0 ? 0 : (double) totalProbes / totalParks;

        System.out.printf("Occupancy: %.2f%%, Avg Probes: %.2f\n", occupancy, avgProbes);
    }

    public static void main(String[] args) throws InterruptedException {
        parkVehicle("ABC1234");
        parkVehicle("ABC1235");
        parkVehicle("XYZ9999");

        Thread.sleep(2000);

        exitVehicle("ABC1234");
        getStatistics();
    }
}