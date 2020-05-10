package features;

import com.embosfer.katas.hotel.booking.Booking;
import com.embosfer.katas.hotel.booking.BookingService;
import com.embosfer.katas.hotel.booking.DatesValidator;
import com.embosfer.katas.hotel.company.CompanyId;
import com.embosfer.katas.hotel.company.CompanyService;
import com.embosfer.katas.hotel.company.EmployeeId;
import com.embosfer.katas.hotel.hotel.HotelId;
import com.embosfer.katas.hotel.hotel.HotelService;
import com.embosfer.katas.hotel.hotel.RoomType;
import com.embosfer.katas.hotel.policy.BookingPolicyRepository;
import com.embosfer.katas.hotel.booking.BookingRepository;
import com.embosfer.katas.hotel.company.CompanyRepository;
import com.embosfer.katas.hotel.hotel.HotelRepository;
import com.embosfer.katas.hotel.policy.BookingPolicyService;
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
    private final BookingPolicyRepository bookingPolicyRepository = new BookingPolicyRepository();
    private final BookingRepository bookingRepository = new BookingRepository();

    private final CompanyService companyService = new CompanyService(companyRepository, bookingPolicyRepository, bookingRepository);
    private final HotelService hotelService = new HotelService(new HotelRepository());
    private final BookingPolicyService bookingPolicyService = new BookingPolicyService(bookingPolicyRepository, companyRepository);
    private final BookingService bookingService = new BookingService(bookingRepository, hotelService, bookingPolicyService, new DatesValidator());

    private Booking booking;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private CompanyId companyId;

    @Given("the employee <{int}> from the company {string}")
    public void theEmployeeFromTheCompany(int id, String cId) {
        companyId = CompanyId.of(cId);
        companyService.addEmployee(companyId, EmployeeId.of(id));
    }

    @And("the employee <{int}> with employee policy allowing only bookings of room types")
    public void withEmployeePolicyAllowingOnlyBookingsOfRoomTypes(int id, List<String> roomTypes) {
        var roomTypesAllowed = roomTypes.stream().map(this::roomTypeFrom).collect(toUnmodifiableList());
        bookingPolicyService.setEmployeePolicy(EmployeeId.of(id), roomTypesAllowed);
    }

    @And("with company policy allowing only bookings of room types")
    public void withCompanyPolicyAllowingOnlyBookingOfRoomTypes(List<String> roomTypes) {
        var roomTypesAllowed = roomTypes.stream().map(this::roomTypeFrom).collect(toUnmodifiableList());
        bookingPolicyService.setCompanyPolicy(companyId, roomTypesAllowed);
    }

    private RoomType roomTypeFrom(String rType) {
        return RoomType.valueOf(rType.toUpperCase());
    }

    @When("the employee <{int}> books the room type {string} in the hotel {string} on the dates {string} to {string}")
    public void theEmployeeBooksTheRoomTypeInTheHotelOnTheDates(int id, String rType, String hId, String dateFrom, String dateTo) {
        checkIn = LocalDate.parse(dateFrom);
        checkOut = LocalDate.parse(dateTo);
        booking = bookingService.book(EmployeeId.of(id), HotelId.of(hId), roomTypeFrom(rType), checkIn, checkOut);
    }

    @And("the hotel {string} providing the following rooms")
    public void theHotelWithRoomTypes(String hId, Map<String, Integer> availableRooms) {
        availableRooms
                .forEach((rType, quantity) ->
                        hotelService.setRoomType(HotelId.of(hId), roomTypeFrom(rType), quantity));
    }

    @When("the employee <{int}> is deleted from the system")
    public void theEmployeeIsDeletedFromTheSystem(int id) {
        companyService.deleteEmployee(EmployeeId.of(id));
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
