import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


/**
 * GreedyScheduler is a driver class.
 * It uses greedy algorithms to schedule meetings based on different criteria:
 * start time, meeting length, and end time.
 *
 * Author: Adam Shergill A01316226
 * Set #: 3G
 */
public class GreedyScheduler {

    // The main method is the driver class of the program.
    public static void main(String[] args) {

        // Please change the name of the file to run the program for different files.
        List<Meeting> meetings = readMeetingsFromFile("meeting1.txt");

        List<Meeting> meetingsByStart = scheduleByStart(new ArrayList<>(meetings));
        List<Meeting> meetingsByLength = scheduleByLength(new ArrayList<>(meetings));
        List<Meeting> meetingsByEnd = scheduleByEnd(new ArrayList<>(meetings));

        System.out.println("Scheduled by Start Time: " + meetingsByStart.size());
        printMeetings(meetingsByStart);

        System.out.println("Scheduled by Length: " + meetingsByLength.size());
        printMeetings(meetingsByLength);

        System.out.println("Scheduled by End Time: " + meetingsByEnd.size());
        printMeetings(meetingsByEnd);
    }

    /**
     * Reads meeting data from a file and creates a list of Meeting objects.
     *
     * @param filename The name of the file to read from.
     * @return A list of Meeting objects.
     */
    private static List<Meeting> readMeetingsFromFile(String filename) {
        List<Meeting> meetings = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                String name = scanner.nextLine().trim();
                String[] times = scanner.nextLine().trim().split("\\s+");
                Integer start = Integer.parseInt(times[0]);
                Integer end = Integer.parseInt(times[1]);
                meetings.add(new Meeting(name, start, end));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return meetings;
    }

    /**
     * Schedules meetings based on the earliest start time.
     *
     * @param meetings A list of meetings to schedule.
     * @return A list of scheduled meetings.
     */
    private static List<Meeting> scheduleByStart(List<Meeting> meetings) {
        meetings.sort(Comparator.comparing(Meeting::getStart));
        return greedySchedule(meetings);
    }

    /**
     * Schedules meetings based on the shortest length.
     *
     * @param meetings A list of meetings to schedule.
     * @return A list of scheduled meetings.
     */
    private static List<Meeting> scheduleByLength(List<Meeting> meetings) {
        List<Meeting> scheduled = new ArrayList<>();
        // Sort by meeting length, and if equal, then by start time
        meetings.sort(Comparator.comparingInt(Meeting::getLength).thenComparing(Meeting::getStart));

        while (!meetings.isEmpty()) {
            Meeting current = meetings.get(0);
            scheduled.add(current);
            meetings.removeIf(m -> m.overlapsWith(current));
        }
        return scheduled;
    }

    /**
     * Schedules meetings based on the earliest end time.
     *
     * @param meetings A list of meetings to schedule.
     * @return A list of scheduled meetings.
     */
    private static List<Meeting> scheduleByEnd(List<Meeting> meetings) {
        meetings.sort(Comparator.comparing(Meeting::getEnd));
        return greedySchedule(meetings);
    }

    /**
     * Greedy algorithm to schedule meetings without overlap.
     *
     * @param meetings A list of meetings to schedule.
     * @return A list of non-overlapping scheduled meetings.
     */
    private static List<Meeting> greedySchedule(List<Meeting> meetings) {
        List<Meeting> scheduled = new ArrayList<>();
        Meeting lastScheduled = null;
        for (Meeting current : meetings) {
            if (lastScheduled == null || !current.overlapsWith(lastScheduled)) {
                scheduled.add(current);
                lastScheduled = current;
            }
        }
        return scheduled;
    }

    /**
     * Prints the details of the scheduled meetings.
     *
     * @param meetings A list of meetings to print.
     */
    private static void printMeetings(List<Meeting> meetings) {
        for (Meeting m : meetings) {
            System.out.println(m.getName() + ": " + m.getStart() + " to " + m.getEnd());
        }
    }
}