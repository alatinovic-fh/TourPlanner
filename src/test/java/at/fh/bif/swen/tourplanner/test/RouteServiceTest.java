package at.fh.bif.swen.tourplanner.test;


import at.fh.bif.swen.tourplanner.service.RouteService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.platform.commons.logging.LogRecordListener;
import org.springframework.cache.support.NullValue;
import org.springframework.util.RouteMatcher;
import nl.altindag.log.LogCaptor;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


class RouteServiceTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final RouteService routeService = new RouteService();



    // Note: Utility                                                                                                    |
    private JsonNode getJson() throws Exception{
        try (InputStream inputStr = getClass().getResourceAsStream("/routeTest.json")) {
            return mapper.readTree(inputStr);
        }
    }


    // Note: reflections helpers. Answer: For the private methods - testing without changing the class ( not for TDD )  |
    private long iGetDistance(JsonNode routeJSON) throws Exception{
        Method method = RouteService.class.getDeclaredMethod("getDistance", JsonNode.class);        //Question: is this the method to be used with the parameter it needs?? ANSWER: YES the method with that name and parameter
        method.setAccessible(true);                                                                         //Question: what is this method.setAccessible --> make the Private method accessible?

        return (long) method.invoke(routeService, routeJSON);
    }


    private Duration iGetDuration(JsonNode routeJSON) throws Exception{
        Method method = RouteService.class.getDeclaredMethod("getDuration", JsonNode.class);
        method.setAccessible(true);

        return (Duration) method.invoke(routeService, routeJSON);
    }

    //__________________________________________________________________________________________________________________


    @Nested
    @DisplayName("getDistance")
    class GetDistance {



        @Test
        @DisplayName("given-ValidRoute_when-getDistance_then-correctKmReturned")
        void givenValidRoute_whenGetDistance_thenCorrectKmReturned() throws Exception{

            //ARRANGE
            JsonNode route = getJson();

            //ACT
            long distanceKm = iGetDistance(route);

            //ASSERT
            assertThat(distanceKm).isEqualTo(194); //Note: rounded down
        }


        @Test
        @DisplayName("given-InvalidRoute_when-getDistance_then-incorrectKmReturned")
        void givenInvalidRoute_whenGetDistance_thenIncorrectKmReturned() throws Exception {

            //ARRANGE
            String jsonTestObj = "{ \"type\": \"FeatureCollection\", \"features\":[]}";
            JsonNode broken = mapper.readTree(jsonTestObj);

            //ACT
            long distanceKm = iGetDistance(broken);

            //ASSERT
            assertThat(distanceKm).isZero();
        }
    }



    @Nested
    @DisplayName("getDuration")
    class GetDuration {



        @Test
        @DisplayName("given-ValidDuration_when-getDuration_then-correctDurationReturned")
        void givenValidDuration_whenGetDuration_then_correctDurationReturned() throws Exception {
            //ARRANGE
            JsonNode route = getJson();

            //ACT
            Duration duration = iGetDuration(route);

            //ASSERT
            assertThat(duration).isEqualTo(Duration.ofSeconds(7822));
        }


        @Test
        @DisplayName("given-InvalidDuration_when-getDuration_then-error")
        void givenInvalidDuration_whenGetDuration_thenThrowError() throws Exception {
            //ARRANGE
            JsonNode routeNull = null;

            //ACT
            Duration duration = iGetDuration(routeNull);

            // ASSERT
            assertThat(duration).isEqualTo(Duration.ZERO);
        }

        @Test
        void givenBrokenRoute_whenGetDuration_thenLogsError() throws Exception {

            LogCaptor logCaptor = LogCaptor.forClass(RouteService.class);
            JsonNode route = null;
            Duration duration = iGetDuration(route);

            assertThat(logCaptor.getLogs())
                    .matches(msg -> msg.contains("ERROR: while finding duration"));
        }

    }
}