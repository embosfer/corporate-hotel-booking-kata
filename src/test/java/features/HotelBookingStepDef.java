package features;

import com.embosfer.katas.hotel.model.*;
import com.embosfer.katas.hotel.services.BookingService;
import com.embosfer.katas.hotel.services.CompanyService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class HotelBookingStepDef {

    private EmployeeId employeeId;
    private Optional<Booking> booking;

    @Given("the employee <{int}> from the company {string}")
    public void theEmployeeFromTheCompany(int id, String cId) {
        employeeId = EmployeeId.of(id);
        new CompanyService().addEmployee(CompanyId.of(cId), employeeId);
    }

    @When("the employee books the room type {string} in the hotel {string} on the dates {string} to {string}")
    public void theEmployeeBooksTheRoomTypeInTheHotelOnTheDates(String rType, String hId, String dateFrom, String dateTo) {
        booking = new BookingService()
                .book(employeeId, HotelId.of(hId), RoomType.valueOf(rType.toUpperCase()), LocalDate.parse(dateFrom), LocalDate.parse(dateTo));
    }

    @Then("the result is empty")
    public void theResultIsEmpty() {
        assertThat(booking).isEmpty();
    }
}
