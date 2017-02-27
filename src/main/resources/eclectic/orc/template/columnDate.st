columnDate(schemaType) ::= <<
    LocalDate date = null;
    if (datum<schemaType.propertyAccess> instanceof java.time.LocalDate) {
        date = datum<schemaType.propertyAccess>;
    } else {
        LocalDate date = java.time.Instant.ofEpochMilli(datum<schemaType.propertyAccess>.getTime()).atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }
    column[vectorizedRowBatch.size] = date.toEpochDay();
>>