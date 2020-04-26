package features;

import com.embosfer.katas.hotel.caches.EmployeeRepository;
import com.embosfer.katas.hotel.caches.HotelRepository;
import com.embosfer.katas.hotel.model.*;
import com.embosfer.katas.hotel.services.BookingService;
import com.embosfer.katas.hotel.services.CompanyService;
import com.embosfer.katas.hotel.services.DatesValidator;
import com.embosfer.katas.hotel.services.HotelService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class HotelBookingStepDef {

    private final HotelService hotelService = new HotelService(new HotelRepository());
    private EmployeeId employeeId;
    private Booking booking;

    @Given("the employee <{int}> from the company {string}")
    public void theEmployeeFromTheCompany(int id, String cId) {
        employeeId = EmployeeId.of(id);
        new CompanyService(new EmployeeRepository()).addEmployee(CompanyId.of(cId), employeeId);
    }

    @When("the employee books the room type {string} in the hotel {string} on the dates {string} to {string}")
    public void theEmployeeBooksTheRoomTypeInTheHotelOnTheDates(String rType, String hId, String dateFrom, String dateTo) {
        booking = new BookingService(hotelService, new DatesValidator())
                .book(employeeId, HotelId.of(hId), RoomType.valueOf(rType.toUpperCase()), LocalDate.parse(dateFrom), LocalDate.parse(dateTo));
    }

    @Then("booking failure of type {string}")
    public void bookingFailure(String failureReason) {
        assertFalse(booking.isOk());
        assertThat(booking.reason()).isEqualTo(Booking.Reason.valueOf(failureReason));
    }

    @And("the hotel {string} with available rooms")
    public void theHotelWithRoomTypes(String hId, Map<String, Integer> availableRooms) {
        availableRooms
                .forEach((rType, quantity) ->
                        hotelService.setRoomType(HotelId.of(hId), RoomType.valueOf(rType.toUpperCase()), quantity));
    }

}
