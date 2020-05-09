package features;

import com.embosfer.katas.hotel.caches.BookingPolicyRepository;
import com.embosfer.katas.hotel.caches.BookingRepository;
import com.embosfer.katas.hotel.caches.CompanyRepository;
import com.embosfer.katas.hotel.caches.HotelRepository;
import com.embosfer.katas.hotel.model.*;
import com.embosfer.katas.hotel.services.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HotelBookingStepDef {

    private final CompanyRepository companyRepository = new CompanyRepository();
    private final CompanyService companyService = new CompanyService(companyRepository);
    private final HotelService hotelService = new HotelService(new HotelRepository());
    private final BookingPolicyService bookingPolicyService = new BookingPolicyService(new BookingPolicyRepository(), companyRepository);
    private final BookingService bookingService = new BookingService(new BookingRepository(), hotelService, bookingPolicyService, new DatesValidator());

    private EmployeeId employeeId;
    private Booking booking;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private CompanyId companyId;

    @Given("the employee <{int}> from the company {string}")
    public void theEmployeeFromTheCompany(int id, String cId) {
        employeeId = EmployeeId.of(id);
        companyId = CompanyId.of(cId);
        companyService.addEmployee(companyId, employeeId);
    }

    @And("with employee policy allowing only bookings of room types")
    public void withEmployeePolicyAllowingOnlyBookingsOfRoomTypes(List<String> roomTypes) {
        var roomTypesAllowed = roomTypes.stream().map(this::roomTypeFrom).collect(toUnmodifiableList());
        bookingPolicyService.setEmployeePolicy(employeeId, roomTypesAllowed);
    }

    @And("with company policy allowing only bookings of room types")
    public void withCompanyPolicyAllowingOnlyBookingOfRoomTypes(List<String> roomTypes) {
        var roomTypesAllowed = roomTypes.stream().map(this::roomTypeFrom).collect(toUnmodifiableList());
        bookingPolicyService.setCompanyPolicy(companyId, roomTypesAllowed);
    }

    private RoomType roomTypeFrom(String rType) {
        return RoomType.valueOf(rType.toUpperCase());
    }

    @When("the employee books the room type {string} in the hotel {string} on the dates {string} to {string}")
    public void theEmployeeBooksTheRoomTypeInTheHotelOnTheDates(String rType, String hId, String dateFrom, String dateTo) {
        checkIn = LocalDate.parse(dateFrom);
        checkOut = LocalDate.parse(dateTo);
        booking = bookingService.book(employeeId, HotelId.of(hId), roomTypeFrom(rType), checkIn, checkOut);
    }

    @And("the hotel {string} providing the following rooms")
    public void theHotelWithRoomTypes(String hId, Map<String, Integer> availableRooms) {
        availableRooms
                .forEach((rType, quantity) ->
                        hotelService.setRoomType(HotelId.of(hId), roomTypeFrom(rType), quantity));
    }

    @Then("booking failure of type {string}")
    public void bookingFailure(String failureReason) {
        assertFalse(booking.isOk());
        assertThat(booking.reason()).isEqualTo(Booking.Reason.valueOf(failureReason));
        assertThat(booking.checkIn()).isEqualTo(checkIn);
        assertThat(booking.checkOut()).isEqualTo(checkOut);
    }

    @Then("booking success")
    public void bookingSuccess() {
        assertTrue(booking.isOk());
        assertThat(booking.reason()).isEqualTo(Booking.Reason.SUCCESS);
        assertThat(booking.checkIn()).isEqualTo(checkIn);
        assertThat(booking.checkOut()).isEqualTo(checkOut);
    }
}
