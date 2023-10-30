import core.Line;
import core.Station;
import junit.framework.TestCase;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RouteCalculatorTest extends TestCase {
    List<Station> noTransferRoute;
    List<Station> oneTransferRoute;
    List<Station> twoTransferRoute;
    StationIndex stationIndex = new StationIndex();
    RouteCalculator calculator;
    Station cucumber;
    Station petrovskaya;
    Station pear;
    Station plum;

    @Override
    protected void setUp() throws Exception {

        Line line1 = new Line(1, "Первая");
        Line line2 = new Line(2, "Вторая");
        Line line3 = new Line(3, "Третья");

        petrovskaya = new Station("Петровская", line1);
        Station watermelon = new Station("Арбузная", line1);
        cucumber = new Station("Огуречная", line1);
        Station carrot = new Station("Морковная", line2);
        Station apple = new Station("Яблочная", line2);
        pear = new Station("Грушевая", line2);
        Station cherry = new Station("Вишнёвая", line3);
        plum = new Station("Сливовая", line3);
        Station koshkino = new Station("Кошкино", line3);

        Stream.of(line1, line2, line3).forEach(stationIndex::addLine);
        Stream.of(petrovskaya, watermelon, cucumber, carrot, apple, pear, cherry, plum, koshkino)
                .peek(s -> s.getLine().addStation(s)).forEach(stationIndex::addStation);
        stationIndex.addConnection(Stream.of(petrovskaya, apple).collect(Collectors.toList()));
        stationIndex.addConnection(Stream.of(pear, cherry).collect(Collectors.toList()));
        stationIndex.getConnectedStations(petrovskaya);
        stationIndex.getConnectedStations(pear);

        calculator = new RouteCalculator(stationIndex);

        noTransferRoute = Stream.of(cucumber, watermelon, petrovskaya)
                .collect(Collectors.toList());
        oneTransferRoute = Stream.of(cucumber, watermelon, petrovskaya, apple, pear)
                .collect(Collectors.toList());
        twoTransferRoute = Stream.of(cucumber, watermelon, petrovskaya, apple, pear, cherry, plum)
                .collect(Collectors.toList());

    }

    public void testCalculateDurationOfNoTransferRoute() {
        double actualNoTransferRoute = RouteCalculator.calculateDuration(noTransferRoute);
        double expectedNoTransferRoute = 5;
        assertEquals(expectedNoTransferRoute, actualNoTransferRoute);
    }
    public void testCalculateDurationOfOneTransferRoute() {
        double actualOneTransferRoute = RouteCalculator.calculateDuration(oneTransferRoute);
        double expectedOneTransferRoute = 11;
        assertEquals(expectedOneTransferRoute, actualOneTransferRoute);
    }
    public void testCalculateDurationOfTwoTransferRoute() {
        double actualTwoTransferRoute = RouteCalculator.calculateDuration(twoTransferRoute);
        double expectedTwoTransferRoute = 17;
        assertEquals(expectedTwoTransferRoute, actualTwoTransferRoute);
    }

    public void testGetShortestRouteOfNoTransferRoute() {
        List<Station> actualNoTransferRoute = calculator.getShortestRoute(cucumber, petrovskaya);
        List<Station> expectedNoTransferRoute = noTransferRoute;
        assertEquals(expectedNoTransferRoute, actualNoTransferRoute);
    }
    public void testGetShortestRouteOfOneTransferRoute() {
        List<Station> actualOneTransferRoute = calculator.getShortestRoute(cucumber, pear);
        List<Station> expectedOneTransferRoute = oneTransferRoute;
        assertEquals(expectedOneTransferRoute, actualOneTransferRoute);
    }
    public void testGetShortestRouteOfTwoTransferRoute() {
        List<Station> actualTwoTransferRoute = calculator.getShortestRoute(cucumber, plum);
        List<Station> expectedTwoTransferRoute = twoTransferRoute;
        assertEquals(expectedTwoTransferRoute, actualTwoTransferRoute);
    }
}
