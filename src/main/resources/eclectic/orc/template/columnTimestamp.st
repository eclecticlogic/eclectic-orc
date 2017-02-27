columnTimestamp(schemaType) ::= <<
    java.util.Date date = null;
    if (datum<schemaType.propertyAccess> instanceof java.time.ZonedDateTime) {
            date = java.util.Date.from(datum<schemaType.propertyAccess>.toInstant());
    } else {
        date = datum<schemaType.propertyAccess>;
    }
    vector.set(vectorizedRowBatch.size, date)
>>