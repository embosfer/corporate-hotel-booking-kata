package features;

import com.embosfer.katas.hotel.caches.EmployeeRepository;
import com.embosfer.katas.hotel.caches.HotelRepository;
import com.embosfer.katas.hotel.model.*;
import com.embosfer.katas.hotel.services.BookingService;
import com.embosfer.katas.hotel.services.CompanyService;
import com.embosfer.katas.hotel.services.HotelService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class HotelBookingStepDef {

    private EmployeeId employeeId;
    private Booking booking;

    @Given("the employee <{int}> from the company {string}")
    public void theEmployeeFromTheCompany(int id, String cId) {
        employeeId = EmployeeId.of(id);
        new CompanyService(new EmployeeRepository()).addEmployee(CompanyId.of(cId), employeeId);
    }

    @When("the employee books the room type {string} in the hotel {string} on the dates {string} to {string}")
    public void theEmployeeBooksTheRoomTypeInTheHotelOnTheDates(String rType, String hId, String dateFrom, String dateTo) {
        booking = new BookingService(new HotelService(new HotelRepository()))
                .book(employeeId, HotelId.of(hId), RoomType.valueOf(rType.toUpperCase()), LocalDate.parse(dateFrom), LocalDate.parse(dateTo));
    }

    @Then("booking failure")
    public void bookingFailure() {
        assertThat(booking).isInstanceOf(BookingFailure.class);
    }
}
