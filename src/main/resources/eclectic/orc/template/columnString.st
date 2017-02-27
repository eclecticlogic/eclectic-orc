columnString(schemaType) ::= <<
    vector.setVal(vectorizedRowBatch.size, datum<schemaType.propertyAccess>.getBytes());
>>