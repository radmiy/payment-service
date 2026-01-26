package com.radmiy.payment.service.app.service;

import com.radmiy.payment.service.api.AsyncSender;
import com.radmiy.payment.service.api.Message;
import com.radmiy.payment.service.api.dto.XPaymentAdapterRequestMessage;
import com.radmiy.payment.service.app.exception.ServiceException;
import com.radmiy.payment.service.app.mapper.PaymentMapper;
import com.radmiy.payment.service.app.mapper.PaymentMapperImpl;
import com.radmiy.payment.service.app.mapper.PaymentMapperImpl_;
import com.radmiy.payment.service.app.mapper.XPaymentAdapterMapper;
import com.radmiy.payment.service.app.mapper.XPaymentAdapterMapperImpl;
import com.radmiy.payment.service.app.model.Payment;
import com.radmiy.payment.service.app.model.PaymentStatus;
import com.radmiy.payment.service.app.model.dto.PaymentDto;
import com.radmiy.payment.service.app.repository.PaymentRepository;
import com.radmiy.payment.service.app.repository.filter.PaymentFilter;
import com.radmiy.payment.service.app.repository.filter.PaymentFilterFactory;
import com.radmiy.payment.service.app.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static com.radmiy.payment.service.app.exception.Error.PAYMENT_IS_NUL;
import static com.radmiy.payment.service.app.exception.Error.PAYMENT_NOT_EXIST;
import static com.radmiy.payment.service.app.model.PaymentStatus.APPROVED;
import static com.radmiy.payment.service.app.model.PaymentStatus.NOT_SENT;
import static com.radmiy.payment.service.app.model.PaymentStatus.PENDING;
import static com.radmiy.payment.service.app.model.PaymentStatus.RECEIVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class PaymentServiceTest {

    @Spy
    private final PaymentMapper delegate = new PaymentMapperImpl_();

    @Spy
    private final XPaymentAdapterMapper xPaymentAdapterMapper = new XPaymentAdapterMapperImpl();

    @Spy
    private final PaymentMapperImpl paymentMapper = new PaymentMapperImpl();

    private final Map<Optional<Payment>, Optional<PaymentDto>> map = new HashMap<>();

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private AsyncSender<XPaymentAdapterRequestMessage> sender;

    @Captor
    private ArgumentCaptor<UUID> paymentUuid;


    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(paymentMapper, "delegate", delegate);
        ReflectionTestUtils.setField(paymentService, "paymentMapper", paymentMapper);
        ReflectionTestUtils.setField(paymentService, "xPaymentAdapterMapper", xPaymentAdapterMapper);
        initPayments();
    }

    @Test
    void getPaymentTest() {
        // given
        Payment expected = map.keySet().iterator()
                .next()
                .get();
        UUID guid = expected.getGuid();

        when(paymentRepository.findById(any())).thenReturn(Optional.of(expected));

        // when
        PaymentDto actual = paymentService.getPayment(guid);

        // then
        assertNotNull(actual);
        assertEquals(expected.getGuid(), actual.getGuid());
    }

    @Test
    void getAllPaymentsTest() {
        // given
        List<Payment> expected = map.keySet().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        when(paymentRepository.findAll()).thenReturn(expected);

        // when
        List<PaymentDto> payments = paymentService.getPayments();

        // then
        assertNotNull(payments);
        assertEquals(5, payments.size());

    }

    @Test
    void updatePaymentStatusTest() {
        // given
        Payment expected = map.keySet().iterator()
                .next()
                .get();
        UUID guid = expected.getGuid();
        PaymentStatus status = NOT_SENT;
        expected.setStatus(status);
        when(paymentRepository.findById(isA(UUID.class))).thenReturn(Optional.of(expected));
        when(paymentRepository.save(isA(Payment.class))).thenReturn(expected);
        when(paymentRepository.existsById(isA(UUID.class))).thenReturn(true);

        // when
        PaymentDto actual = paymentService.updateStatus(guid, status);

        // then
        assertNotNull(actual);
        assertEquals(status, actual.getStatus());
    }

    @Test
    void updatePaymentNoteTest() {
        // given
        Payment expected = map.keySet().iterator()
                .next()
                .get();
        UUID guid = expected.getGuid();
        String note = "test";
        expected.setNote(note);
        when(paymentRepository.findById(isA(UUID.class))).thenReturn(Optional.of(expected));
        when(paymentRepository.save(isA(Payment.class))).thenReturn(expected);
        when(paymentRepository.existsById(isA(UUID.class))).thenReturn(true);

        // when
        PaymentDto actual = paymentService.updateNote(guid, note);

        // then
        assertNotNull(actual);
        assertEquals(note, actual.getNote());
    }

    @ParameterizedTest
    @MethodSource("statusProvider")
    void getPaymentsFilteredByStatusTest(PaymentStatus status) {
        // given
        PaymentFilter paymentFilter = PaymentFilter.builder()
                .status(status)
                .build();
        Specification<Payment> spec =
                PaymentFilterFactory.fromFilter(paymentFilter);

        List<Payment> expected = map.keySet().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(payment -> payment.getStatus() == status)
                .toList();
        when(paymentRepository.findAll(isA(spec.getClass()))).thenReturn(expected);

        // when
        List<PaymentDto> actual = paymentService.search(paymentFilter);

        // then
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertEquals(1, actual.size());
        assertEquals(expected.get(0).getStatus(), actual.get(0).getStatus());
        verify(paymentRepository, times(1)).findAll(isA(spec.getClass()));
    }

    static Stream<PaymentStatus> statusProvider() {
        return Stream.of(
                RECEIVED,
                PENDING,
                PaymentStatus.APPROVED,
                PaymentStatus.DECLINED,
                NOT_SENT
        );
    }

    @ParameterizedTest
    @MethodSource("filterProvider")
    void getPaymentsFilteredTest(PaymentFilter paymentFilter) {
        // given
        Specification<Payment> spec =
                PaymentFilterFactory.fromFilter(paymentFilter);

        List<Payment> expectedPayments = new ArrayList<>();
        when(paymentRepository.findAll(any(spec.getClass()))).thenReturn(expectedPayments);

        // when
        List<PaymentDto> actual = paymentService.search(paymentFilter);

        // then
        assertNotNull(actual);
        verify(paymentRepository, times(1)).findAll(any(spec.getClass()));
    }

    static Stream<PaymentFilter> filterProvider() {
        return Stream.of(
                PaymentFilter.builder()
                        .currency("USD")
                        .build(),
                PaymentFilter.builder()
                        .status(APPROVED)
                        .build(),
                PaymentFilter.builder()
                        .maxAmount(new BigDecimal(2000))
                        .minAmount(new BigDecimal(1000))
                        .build(),
                PaymentFilter.builder()
                        .createdBefore(OffsetDateTime.of(
                                2025, 11, 1, 13, 0, 0, 0, ZoneOffset.UTC
                        ).toInstant())
                        .createdAfter(OffsetDateTime.of(
                                2025, 11, 1, 11, 0, 0, 0, ZoneOffset.UTC
                        ).toInstant())
                        .build()
        );
    }

    @ParameterizedTest
    @MethodSource("filterAndPageProvider")
    void getPaymentsFilteredTest(PaymentFilter paymentFilter, PageRequest pageRequest) {
        // given
        Specification<Payment> spec =
                PaymentFilterFactory.fromFilter(paymentFilter);
        Page page = new PageImpl(new ArrayList<PaymentDto>());

        when(paymentRepository.findAll(any(spec.getClass()), any(pageRequest.getClass()))).thenReturn(page);

        // when
        Page<PaymentDto> actual = paymentService.searchPaged(paymentFilter, pageRequest);

        // then
        assertNotNull(actual);
        verify(paymentRepository, times(1)).findAll(any(spec.getClass()), any(pageRequest.getClass()));
    }

    static Stream<Arguments> filterAndPageProvider() {
        return Stream.of(
                Arguments.of(
                        PaymentFilter.builder()
                                .currency("USD")
                                .build(),
                        PageRequest.of(0, 25, Sort.Direction.ASC, "status")
                ),
                Arguments.of(
                        PaymentFilter.builder()
                                .status(APPROVED)
                                .build(),
                        PageRequest.of(0, 25, Sort.Direction.DESC, "currency")
                )
        );
    }


    @Test
    void createPaymentTest() {
        // given
        UUID guid = UUID.randomUUID();
        Payment expectedPayment = Payment.builder()
                .guid(guid)
                .amount(BigDecimal.valueOf(1000))
                .currency("RUB")
                .status(APPROVED)
                .note("Test note")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        when(paymentRepository.save(any())).thenReturn(expectedPayment);

        PaymentDto newPayment = PaymentDto.builder()
                .amount(BigDecimal.valueOf(1000))
                .currency("RUB")
                .status(APPROVED)
                .note("Test note")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        // when
        PaymentDto result = paymentService.createPayment(newPayment);

        // then
        assertNotNull(result);
        assertEquals(expectedPayment.getNote(), result.getNote());
    }

    @Test
    void deletePaymentTest() {
        // given
        UUID guid = map.keySet().iterator()
                .next()
                .get()
                .getGuid();
        when(paymentRepository.existsById(isA(UUID.class))).thenReturn(true);

        // when
        paymentService.deletePayment(guid);

        // then
        verify(paymentRepository).deleteById(paymentUuid.capture());
        UUID actualGuid = paymentUuid.getValue();
        assertEquals(guid, actualGuid);
    }

    @Test
    void notExistPaymentErrorTest() {
        // given
        UUID guid = UUID.randomUUID();
        when(paymentRepository.existsById(isA(UUID.class))).thenReturn(false);

        // when
        ServiceException actualException =
                assertThrows(ServiceException.class, () -> paymentService.deletePayment(guid));

        // then
        assertEquals(PAYMENT_NOT_EXIST.getMessage().formatted(guid), actualException.getMessage());
    }

    @Test
    void nullPaymentErrorTest() {
        // given
        UUID guid = UUID.randomUUID();
        PaymentDto dto = null;
        when(paymentRepository.existsById(isA(UUID.class))).thenReturn(true);

        // when
        ServiceException actualException =
                assertThrows(ServiceException.class, () -> paymentService.updatePayment(guid, dto));

        // then
        assertEquals(PAYMENT_IS_NUL.getMessage(), actualException.getMessage());
    }

    private void initPayments() {
        UUID guid = UUID.randomUUID();
        Payment payment = Payment.builder()
                .guid(guid)
                .amount(BigDecimal.valueOf(1000))
                .currency("RUB")
                .status(APPROVED)
                .note("Test note 1")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        PaymentDto dto = paymentMapper.toDto(payment);
        map.put(Optional.of(payment), Optional.of(dto));

        guid = UUID.randomUUID();
        payment = Payment.builder()
                .guid(guid)
                .amount(BigDecimal.valueOf(2000))
                .currency("USD")
                .status(PaymentStatus.DECLINED)
                .note("Test note 2")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        dto = paymentMapper.toDto(payment);
        map.put(Optional.of(payment), Optional.of(dto));

        guid = UUID.randomUUID();
        payment = Payment.builder()
                .guid(guid)
                .amount(BigDecimal.valueOf(3000))
                .currency("EUR")
                .status(RECEIVED)
                .note("Test note 3")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        dto = paymentMapper.toDto(payment);
        map.put(Optional.of(payment), Optional.of(dto));

        guid = UUID.randomUUID();
        payment = Payment.builder()
                .guid(guid)
                .amount(BigDecimal.valueOf(3000))
                .currency("EUR")
                .status(PENDING)
                .note("Test note 3")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        dto = paymentMapper.toDto(payment);
        map.put(Optional.of(payment), Optional.of(dto));

        guid = UUID.randomUUID();
        payment = Payment.builder()
                .guid(guid)
                .amount(BigDecimal.valueOf(3000))
                .currency("EUR")
                .status(NOT_SENT)
                .note("Test note 3")
                .createdAt(OffsetDateTime.of(
                        2025, 11, 1, 12, 0, 0, 0, ZoneOffset.UTC
                ))
                .updatedAt(OffsetDateTime.now())
                .build();
        dto = paymentMapper.toDto(payment);
        map.put(Optional.of(payment), Optional.of(dto));
    }
}
