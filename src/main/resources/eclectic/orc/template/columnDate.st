columnDate(schemaType) ::= <<
    java.time.LocalDate date = null;
    if (datum<schemaType.propertyAccess> instanceof java.time.LocalDate) {
        date = datum<schemaType.propertyAccess>;
    } else {
        date = java.time.Instant.ofEpochMilli(datum<schemaType.propertyAccess>.getTime()).atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }
    vector.vector[vectorizedRowBatch.size] = date.toEpochDay();
>>