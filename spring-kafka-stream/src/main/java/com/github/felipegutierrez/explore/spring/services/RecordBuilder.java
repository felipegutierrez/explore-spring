package com.github.felipegutierrez.explore.spring.services;

import com.github.felipegutierrez.explore.spring.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.github.felipegutierrez.explore.spring.utils.PosInvoiceConstants.HOME_DELIVERY;

@Slf4j
@Service
public class RecordBuilder {

    public NotificationAvro getNotificationAvro(PosInvoice posInvoice) {
        NotificationAvro notificationAvro = NotificationAvro.newBuilder()
                .setInvoiceNumber(posInvoice.getInvoiceNumber())
                .setCustomerCardNo(posInvoice.getCustomerCardNo())
                .setTotalAmount(posInvoice.getTotalAmount())
                .setEarnedLoyaltyPoints(posInvoice.getTotalAmount() * 0.02)
                .setTotalLoyaltyPoints(posInvoice.getTotalAmount() * 0.02)
                .build();
        return notificationAvro;
    }

    public Notification getNotificationJson(PosInvoiceAvro posInvoice) {
        Notification notification = new Notification();
        notification.setInvoiceNumber(posInvoice.getInvoiceNumber());
        notification.setCustomerCardNo(posInvoice.getCustomerCardNo());
        notification.setTotalAmount(posInvoice.getTotalAmount());
        notification.setEarnedLoyaltyPoints(posInvoice.getTotalAmount() * 0.02);
        notification.setTotalLoyaltyPoints(posInvoice.getTotalAmount() * 0.02);
        return notification;
    }

    public NotificationAvro getNotificationAvro(PosInvoiceAvro posInvoice) {
        NotificationAvro notificationAvro = NotificationAvro.newBuilder()
                .setInvoiceNumber(posInvoice.getInvoiceNumber())
                .setCustomerCardNo(posInvoice.getCustomerCardNo())
                .setTotalAmount(posInvoice.getTotalAmount())
                .setEarnedLoyaltyPoints(posInvoice.getTotalAmount() * 0.02)
                .setTotalLoyaltyPoints(posInvoice.getTotalAmount() * 0.02)
                .build();
        return notificationAvro;
    }

    public PosInvoice getMaskedInvoiceJson(PosInvoice posInvoice) {
        posInvoice.setCustomerCardNo(null);
        if (posInvoice.getDeliveryType().equalsIgnoreCase(HOME_DELIVERY)) {
            posInvoice.getDeliveryAddress().setAddressLine(null);
            posInvoice.getDeliveryAddress().setContactNumber(null);
        }
        return posInvoice;
    }

    public PosInvoiceAvro getMaskedInvoiceAvro(PosInvoiceAvro posInvoice) {
        posInvoice.setCustomerCardNo(null);
        if (posInvoice.getDeliveryType().equalsIgnoreCase(HOME_DELIVERY)) {
            posInvoice.getDeliveryAddress().setAddressLine(null);
            posInvoice.getDeliveryAddress().setContactNumber(null);
        }
        return posInvoice;
    }

    public List<HadoopRecordAvro> getHadoopRecordsAvro(PosInvoice posInvoice) {
        List<HadoopRecordAvro> hadoopRecordAvroList = new ArrayList<HadoopRecordAvro>();
        for (LineItem lineItem : posInvoice.getInvoiceLineItems()) {
            HadoopRecordAvro hadoopRecordAvro = HadoopRecordAvro.newBuilder()
                    .setInvoiceNumber(posInvoice.getInvoiceNumber())
                    .setCreatedTime(posInvoice.getCreatedTime())
                    .setStoreID(posInvoice.getStoreID())
                    .setPosID(posInvoice.getPosID())
                    .setCustomerType(posInvoice.getCustomerType())
                    .setPaymentMethod(posInvoice.getPaymentMethod())
                    .setDeliveryType(posInvoice.getDeliveryType())
                    .setItemCode(lineItem.getItemCode())
                    .setItemDescription(lineItem.getItemDescription())
                    .setItemPrice(lineItem.getItemPrice())
                    .setItemQty(lineItem.getItemQty())
                    .setTotalValue(lineItem.getTotalValue())
                    .build();
            if (posInvoice.getDeliveryType().equalsIgnoreCase(HOME_DELIVERY)) {
                hadoopRecordAvro.setCity(posInvoice.getDeliveryAddress().getCity());
                hadoopRecordAvro.setState(posInvoice.getDeliveryAddress().getState());
                hadoopRecordAvro.setPinCode(posInvoice.getDeliveryAddress().getPinCode());
            }
            hadoopRecordAvroList.add(hadoopRecordAvro);
        }
        return hadoopRecordAvroList;
    }

    public List<HadoopRecord> getHadoopRecordsJson(PosInvoiceAvro posInvoice) {
        List<HadoopRecord> hadoopRecordList = new ArrayList<HadoopRecord>();
        for (LineItemAvro lineItemAvro : posInvoice.getInvoiceLineItems()) {
            HadoopRecord hadoopRecord = new HadoopRecord();
            hadoopRecord.setInvoiceNumber(posInvoice.getInvoiceNumber());
            hadoopRecord.setCreatedTime(posInvoice.getCreatedTime());
            hadoopRecord.setStoreID(posInvoice.getStoreID());
            hadoopRecord.setPosID(posInvoice.getPosID());
            hadoopRecord.setCustomerType(posInvoice.getCustomerType());
            hadoopRecord.setPaymentMethod(posInvoice.getPaymentMethod());
            hadoopRecord.setDeliveryType(posInvoice.getDeliveryType());
            hadoopRecord.setItemCode(lineItemAvro.getItemCode());
            hadoopRecord.setItemDescription(lineItemAvro.getItemDescription());
            hadoopRecord.setItemPrice(lineItemAvro.getItemPrice());
            hadoopRecord.setItemQty(lineItemAvro.getItemQty());
            hadoopRecord.setTotalValue(lineItemAvro.getTotalValue());
            if (posInvoice.getDeliveryType().equalsIgnoreCase(HOME_DELIVERY)) {
                hadoopRecord.setCity(posInvoice.getDeliveryAddress().getCity());
                hadoopRecord.setState(posInvoice.getDeliveryAddress().getState());
                hadoopRecord.setPinCode(posInvoice.getDeliveryAddress().getPinCode());
            }
            hadoopRecordList.add(hadoopRecord);
        }
        return hadoopRecordList;
    }

    public List<HadoopRecordAvro> getHadoopRecordsAvro(PosInvoiceAvro posInvoice) {
        List<HadoopRecordAvro> hadoopRecordAvroList = new ArrayList<HadoopRecordAvro>();
        for (LineItemAvro lineItemAvro : posInvoice.getInvoiceLineItems()) {
            HadoopRecordAvro hadoopRecordAvro = HadoopRecordAvro.newBuilder()
                    .setInvoiceNumber(posInvoice.getInvoiceNumber())
                    .setCreatedTime(posInvoice.getCreatedTime())
                    .setStoreID(posInvoice.getStoreID())
                    .setPosID(posInvoice.getPosID())
                    .setCustomerType(posInvoice.getCustomerType())
                    .setPaymentMethod(posInvoice.getPaymentMethod())
                    .setDeliveryType(posInvoice.getDeliveryType())
                    .setItemCode(lineItemAvro.getItemCode())
                    .setItemDescription(lineItemAvro.getItemDescription())
                    .setItemPrice(lineItemAvro.getItemPrice())
                    .setItemQty(lineItemAvro.getItemQty())
                    .setTotalValue(lineItemAvro.getTotalValue())
                    .build();
            if (posInvoice.getDeliveryType().equalsIgnoreCase(HOME_DELIVERY)) {
                hadoopRecordAvro.setCity(posInvoice.getDeliveryAddress().getCity());
                hadoopRecordAvro.setState(posInvoice.getDeliveryAddress().getState());
                hadoopRecordAvro.setPinCode(posInvoice.getDeliveryAddress().getPinCode());
            }
            hadoopRecordAvroList.add(hadoopRecordAvro);
        }
        return hadoopRecordAvroList;
    }

    public DepartmentAggregate initDepartmentAggregate() {
        DepartmentAggregate departmentAggregate = new DepartmentAggregate();
        departmentAggregate.setEmployeeCount(0);
        departmentAggregate.setTotalSalary(0);
        departmentAggregate.setAvgSalary(0D);
        return departmentAggregate;
    }

    public DepartmentAggregate aggregateDepartmentAggregate(Employee emp, DepartmentAggregate aggValue) {
        DepartmentAggregate departmentAggregate = new DepartmentAggregate();
        departmentAggregate.setEmployeeCount(aggValue.getEmployeeCount() + 1);
        departmentAggregate.setTotalSalary(aggValue.getTotalSalary() + emp.getSalary());
        departmentAggregate.setAvgSalary((aggValue.getTotalSalary() + emp.getSalary()) / (aggValue.getEmployeeCount() + 1D));
        return departmentAggregate;
    }

    public DepartmentAggregate addedDepartmentAggregate(Employee emp, DepartmentAggregate aggValue){
        log.info("addedDepartmentAggregate Employee: {} DepartmentAggregate: {}", emp, aggValue);
        DepartmentAggregate departmentAggregate = new DepartmentAggregate();
        departmentAggregate.setEmployeeCount(aggValue.getEmployeeCount() + 1);
        departmentAggregate.setTotalSalary(aggValue.getTotalSalary() + emp.getSalary());
        departmentAggregate.setAvgSalary((aggValue.getTotalSalary() + emp.getSalary()) / (aggValue.getEmployeeCount() + 1D));
        return departmentAggregate;
    }

    public DepartmentAggregate subtractDepartmentAggregate(Employee emp, DepartmentAggregate aggValue){
        log.info("subtractDepartmentAggregate Employee: {} DepartmentAggregate: {}", emp, aggValue);
        DepartmentAggregate departmentAggregate = new DepartmentAggregate();
        departmentAggregate.setEmployeeCount(aggValue.getEmployeeCount() - 1);
        departmentAggregate.setTotalSalary(aggValue.getTotalSalary() - emp.getSalary());
        departmentAggregate.setAvgSalary((aggValue.getTotalSalary() - emp.getSalary()) / (aggValue.getEmployeeCount() - 1D));
        return departmentAggregate;
    }

    public TransactionStatus getTransactionStatus(PaymentRequest request, PaymentConfirmation confirmation){
        log.info("Evaluating request: {} with confirmation: {}", request.getOTP(), confirmation.getOTP());

        String status = "Failure";
        if(request.getOTP().equals(confirmation.getOTP())) {
            status = "Success";
        }

        TransactionStatus transactionStatus = new TransactionStatus();
        transactionStatus.setTransactionID(request.getTransactionID());
        transactionStatus.setStatus(status);
        return transactionStatus;
    }
}
