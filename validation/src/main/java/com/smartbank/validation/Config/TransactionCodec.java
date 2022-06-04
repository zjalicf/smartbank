package com.smartbank.validation.Config;

import com.datastax.driver.core.UDTValue;
import com.datastax.driver.core.UserType;
import com.datastax.driver.core.exceptions.InvalidTypeException;
import com.datastax.oss.driver.api.core.ProtocolVersion;
import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.smartbank.validation.Enum.Status;
import com.smartbank.validation.Enum.TransactionType;
import com.smartbank.validation.Model.Transaction;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.kafka.common.metrics.Stat;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TransactionCodec extends MappingCodec<UdtValue, Transaction> {

    public TransactionCodec(@NonNull TypeCodec<UdtValue> innerCodec) {
        super(innerCodec, GenericType.of(Transaction.class));
    }

    @NonNull
    @Override
    public UserDefinedType getCqlType() {
        return (UserDefinedType) super.getCqlType();
    }

    @Nullable
    @Override
    protected Transaction innerToOuter(@Nullable UdtValue value) {
        if (value == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Instant time = Instant.parse(String.valueOf(value.getInstant("time")));

        return new Transaction(
                value.getUuid("transaction_id"),
                value.getUuid("requester_id"),
                value.getUuid("receiver_id"),
                value.getDouble("amount"),
                Status.valueOf(value.getString("status")),
                TransactionType.valueOf(value.getString("transaction_type")),
                time);
    }

    @Nullable
    @Override
    protected UdtValue outerToInner(@Nullable Transaction value) {
        return value == null ? null : getCqlType().newValue()
                .setUuid("transaction_id", value.getTransactionId())
                .setUuid("requester_id", value.getRequesterId())
                .setUuid("receiver_id", value.getReceiverId())
                .setDouble("amount", value.getAmount())
                .setString("status", value.getStatus().toString())
                .setString("transaction_type", value.getTransactionType().toString())
                .setInstant("time", value.getTime());
    }
}
