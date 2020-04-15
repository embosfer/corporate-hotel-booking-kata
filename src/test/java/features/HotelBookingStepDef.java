package features;

import com.embosfer.katas.hotel.model.*;
import com.embosfer.katas.hotel.services.BookingService;
import com.embosfer.katas.hotel.services.CompanyService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class HotelBookingStepDef {

    private EmployeeId employeeId;
    private HotelId hotelId;
    private RoomType roomType;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Optional<Booking> booking;

    @Given("the employee <{int}> from the company {string}")
    public void theEmployeeFromTheCompany(int id, String cId) {
        employeeId = EmployeeId.of(id);
        new CompanyService().addEmployee(CompanyId.of(cId), employeeId);
    }

    @And("the hotel {string}")
    public void theHotel(String id) {
        hotelId = HotelId.of(id);
    }

    @And("the room type {string}")
    public void theRoomType(String type) {
        roomType = RoomType.valueOf(type.toUpperCase());
    }

    @And("the dates {string} to {string}")
    public void theDatesTo(String dateFrom, String dateTo) {
        checkIn = LocalDate.parse(dateFrom);
        checkOut = LocalDate.parse(dateTo);
    }

    @When("the employee books")
    public void theEmployeeBooks() {
        booking = new BookingService().book(employeeId, hotelId, roomType, checkIn, checkOut);
    }

    @Then("the result is empty")
    public void theResultIsEmpty() {
        assertThat(booking).isEmpty();
    }
}
